package com.reservation.common.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.reservation.application.domain.entity.base.DomainEntity")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class DomainEntityProcessor extends AbstractProcessor {

    private static final String ANNOTATION_SIMPLE_NAME = "DomainEntity";

    private static final String ANNOTATED_TARGET_TYPE_ERROR_MESSAGE = "Only classes can be annotated with @%s";

    private static final String GETTER_METHOD_PREFIX = "get";

    private Types typeUtils;

    private Elements elementUtils;

    private Filer filer;

    private Messager messager;

    private void error(Element element, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                element);
    }

    private List<Map.Entry<String, String>> extractPropertiesFromClass(Element classElement) {
        return classElement.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD && element.getSimpleName().toString().startsWith(GETTER_METHOD_PREFIX) && ((ExecutableElement) element).getParameters().isEmpty())
                .map(x -> (ExecutableElement) x)
                .map(getter -> {
                    String returnTypeFullName = getter.getReturnType().toString();
                    String returnTypeSimpleName = returnTypeFullName.substring(returnTypeFullName.lastIndexOf('.') + 1);

                    String getterName = getter.getSimpleName().toString();
                    String propertyName = getterName.replace(GETTER_METHOD_PREFIX, "");
                    if (propertyName.length() < 1) {
                        return null;
                    }
                    propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
                    return new AbstractMap.SimpleEntry<>(propertyName, returnTypeSimpleName);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<String, String>> extractPropertiesFromParentClassOf(TypeMirror classElement) {
        if (classElement == null) {
            return new ArrayList<>();
        }
        List<Map.Entry<String, String>> en = new ArrayList<>();
        List<? extends TypeMirror> supertypes = typeUtils.directSupertypes(classElement);
        for (TypeMirror supertype : supertypes) {
            if (supertype.getKind() == TypeKind.DECLARED) {
                Element supertypeElement = ((DeclaredType) supertype).asElement();
                if (supertypeElement.getKind().isClass() && !supertypeElement.getSimpleName().contentEquals(Object.class.getSimpleName())) {
                    List<Map.Entry<String, String>> entries = extractPropertiesFromClass(supertypeElement);
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

    private <E extends Element> List<Map.Entry<String, String>> extractAllPropertiesOfDomainClass(E domainEntity) {

        List<Map.Entry<String, String>> propertiesWithType = extractPropertiesFromParentClassOf(domainEntity.asType());
        propertiesWithType.addAll(extractPropertiesFromClass(domainEntity));
        return propertiesWithType;
    }

    private <E extends Element> void processAnnotatedDomainEntity(E domainEntity) {
        if (!ensureValidTargetClassOfAnnotation(domainEntity)) {
            return;
        }

        String className = domainEntity.getSimpleName().toString();
        List<Map.Entry<String, String>> domainClassPropertiesWithDataType = extractAllPropertiesOfDomainClass(domainEntity);

        //TODO: generate Domain entity class with all its properties and their data types
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement domainAnnotation : annotations) {

            Set<? extends Element> annotatedDomainEntities = roundEnv.getElementsAnnotatedWith(domainAnnotation);

            annotatedDomainEntities
                    .forEach(this::processAnnotatedDomainEntity);

            //TODO: generate a wrapper class which contains all domain entities and has a return method of all classes with its properties

        }
        return true;
    }
}
