package com.reservation.common.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.reservation.application.domain.entity.base.DomainEntity")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class DomainEntityProcessor extends AbstractProcessor {

    private static final String DOMAIN_ENTITY_PACKAGE = "com.reservation.application.domain.entity";

    private static final String DOMAIN_ENTITY_PROPERTIES_WRAPPER_NAME = "DomainEntityPropertiesWrapper";

    private static final String ANNOTATION_SIMPLE_NAME = "DomainEntity";

    private static final String DOMAIN_ENTITY_GENERATED_CLASS_SUFFIX = "DE";

    private static final String ANNOTATED_TARGET_TYPE_ERROR_MESSAGE = "Only classes can be annotated with @%s";

    private static final String GETTER_METHOD_PREFIX = "get";

    private static final String ENTITY_PROPERTIES_GETTER_METHOD_NAME = "getEntityProperties";

    private static final String ENTITIES_PROPERTIES_WRAPPER_GETTER_METHOD_NAME = "getPropertiesOfDomainEntities";

    private Types typeUtils;

    private Filer filer;

    private Messager messager;

    private void error(Element element, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                element);
    }

    private String getDomainEntityClassName(Element domainEntityElement) {
        return ((TypeElement) domainEntityElement).getQualifiedName().toString();
    }

    private List<Map.Entry<String, TypeMirror>> extractPropertiesFromClass(Element classElement) {
        return classElement.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getSimpleName().toString().startsWith(GETTER_METHOD_PREFIX) && ((ExecutableElement) element).getParameters().isEmpty())
                .map(x -> (ExecutableElement) x)
                .map(getter -> {
                    String getterName = getter.getSimpleName().toString();
                    String propertyName = getterName.replace(GETTER_METHOD_PREFIX, "");
                    if (propertyName.length() < 1) {
                        return null;
                    }
                    propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
                    return new AbstractMap.SimpleEntry<>(propertyName, getter.getReturnType());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<String, TypeMirror>> extractPropertiesFromParentClassOf(TypeMirror classElement) {
        if (classElement == null) {
            return new ArrayList<>();
        }
        List<Map.Entry<String, TypeMirror>> en = new ArrayList<>();
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(classElement);
        for (TypeMirror supertype : supertypes) {
            if (supertype.getKind() == TypeKind.DECLARED) {
                Element supertypeElement = ((DeclaredType) supertype).asElement();
                if (supertypeElement.getKind().isClass() && !supertypeElement.getSimpleName().contentEquals(Object.class.getSimpleName())) {
                    List<Map.Entry<String, TypeMirror>> entries = extractPropertiesFromClass(supertypeElement);
                    en.addAll(entries);
                }
                en.addAll(extractPropertiesFromParentClassOf(supertype));
            }
        }
        return en;
    }

    private <E extends Element> boolean ensureValidTargetClassOfAnnotation(E domainEntity) {
        if (!domainEntity.getKind().isClass()) {
            error(domainEntity, ANNOTATED_TARGET_TYPE_ERROR_MESSAGE, ANNOTATION_SIMPLE_NAME);
            return false;
        }
        return true;
    }

    private <E extends Element> List<Map.Entry<String, TypeMirror>> extractAllPropertiesOfDomainClass(E domainEntity) {

        List<Map.Entry<String, TypeMirror>> propertiesWithType = extractPropertiesFromParentClassOf(domainEntity.asType());
        propertiesWithType.addAll(extractPropertiesFromClass(domainEntity));
        return propertiesWithType;
    }

    private <E extends Element> void processAnnotatedDomainEntity(E domainEntity) {
        if (!ensureValidTargetClassOfAnnotation(domainEntity)) {
            return;
        }

        List<Map.Entry<String, TypeMirror>> domainClassPropertiesWithDataType =
                extractAllPropertiesOfDomainClass(domainEntity);

        generateCode(domainEntity, domainClassPropertiesWithDataType);
        //TODO: extract the code to separate classes and services and helper methods with its constants and clean the code and separate classes in appropriate packages
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement domainAnnotation : annotations) {

            Set<? extends Element> annotatedDomainEntities = roundEnv.getElementsAnnotatedWith(domainAnnotation);
            List<String> domainEntityGeneratedClassNames = new ArrayList<>();
            annotatedDomainEntities
                    .forEach(currentDomainEntity -> {
                        processAnnotatedDomainEntity(currentDomainEntity);
                        String domainEntityClassName = getDomainEntityClassName(currentDomainEntity);
                        domainEntityGeneratedClassNames.add(domainEntityClassName + DOMAIN_ENTITY_GENERATED_CLASS_SUFFIX);
                    });

            generateDomainEntityPropertiesWrapperClass(domainEntityGeneratedClassNames);
        }
        return true;
    }

    private void generateDomainEntityPropertiesWrapperClass(List<String> domainEntityGeneratedClassNames) {
        //TODO add check if list is empty -> then the file should be generated
        TypeSpec.Builder wrapperClassBuilder = TypeSpec
                .classBuilder(DOMAIN_ENTITY_PROPERTIES_WRAPPER_NAME)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec fieldWithProperties = FieldSpec
                .builder(
                        ParameterizedTypeName.get(ClassName.get(List.class), TypeName.get(DomainEntityProperty.class)),
                        "propertiesOfEntities",
                        Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T<>()", ArrayList.class)
                .build();
        wrapperClassBuilder.addField(fieldWithProperties);

        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();

        domainEntityGeneratedClassNames.forEach(generatedEntityClassFullName -> {
            String generatedEntityClassSimpleName = generatedEntityClassFullName
                    .substring(generatedEntityClassFullName.lastIndexOf('.') + 1);

            staticBlockBuilder.addStatement("propertiesOfEntities.addAll($L())", generatedEntityClassSimpleName + '.' + ENTITY_PROPERTIES_GETTER_METHOD_NAME);
        });
        wrapperClassBuilder.addStaticBlock(staticBlockBuilder.build());

        MethodSpec allPropertiesGetterMethodBuilder = MethodSpec.methodBuilder(ENTITIES_PROPERTIES_WRAPPER_GETTER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), TypeName.get(DomainEntityProperty.class)))
                .addStatement("return propertiesOfEntities")
                .build();

        wrapperClassBuilder.addMethod(allPropertiesGetterMethodBuilder);

        JavaFile javaFile = JavaFile
                .builder(DOMAIN_ENTITY_PACKAGE, wrapperClassBuilder.build())
                .indent("    ")
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <E extends Element> void generateCode(E domainEntity, List<Map.Entry<String, TypeMirror>> domainClassPropertiesWithDataType) {
        String className = getDomainEntityClassName(domainEntity);

        String packageName = "";
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String domainEntityClassName = className + DOMAIN_ENTITY_GENERATED_CLASS_SUFFIX;
        String domainEntitySimpleClassName = domainEntityClassName.substring(lastDot + 1);

        TypeName domainEntityTypeName = TypeName.get(domainEntity.asType());

        TypeSpec.Builder builder = TypeSpec
                .classBuilder(domainEntitySimpleClassName)
                .addModifiers(Modifier.PUBLIC);

        List<String> propertyFields = new ArrayList<>();

        domainClassPropertiesWithDataType.forEach(prop -> {
            propertyFields.add(prop.getKey());

            FieldSpec name = FieldSpec
                    .builder(ClassName.get(DomainEntityProperty.class),  prop.getKey())
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T($T.class, $T.class, $S)", DomainEntityProperty.class, ClassName.get(domainEntity.asType()), ClassName.get(prop.getValue()), prop.getKey())
                    .build();

            builder.addField(name);
        });

        MethodSpec getAllProperties = MethodSpec
                .methodBuilder(ENTITY_PROPERTIES_GETTER_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(DomainEntityProperty.class)))
                .addStatement("return List.of($L)", String.join(", ", propertyFields))
                .build();

        builder.addMethod(getAllProperties);

        JavaFile javaFile = JavaFile
                .builder(packageName, builder.build())
                .indent("    ")
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
