package com.reservation.infrastructure.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.greaterThan;
import static com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureUnit {

    private static final String ALL_CLASSES_MATCHING_LITERAL = "..";

    final String unitPackage;

    ArchitectureUnit(String unitPackage) {
        this.unitPackage = unitPackage;
    }

    static String matchAllClassesInPackage(String packageName) {
        return packageName.concat(ALL_CLASSES_MATCHING_LITERAL);
    }

    static void rejectDependency(String fromPackageName, String toPackageName, JavaClasses classes) {
        noClasses()
                .that()
                .resideInAPackage(matchAllClassesInPackage(fromPackageName))
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(matchAllClassesInPackage(toPackageName))
                .check(classes);
    }

    static void rejectAnyDependency(List<String> fromPackages,
                                    List<String> toPackages,
                                    JavaClasses javaClasses) {
        for (String fromPackage : fromPackages) {
            for (String toPackage : toPackages) {
                rejectDependency(fromPackage, toPackage, javaClasses);
            }
        }
    }

    private JavaClasses classesInPackage(String packageName) {
        return new ClassFileImporter()
                .importPackages(packageName);
    }

    String getAbsolutePackageName(String relativePackage) {
        return unitPackage
                .concat(".")
                .concat(relativePackage);
    }

    void rejectEmptyPackage(String packageName) {
        classes()
                .that()
                .resideInAPackage(matchAllClassesInPackage(packageName))
                .should(containNumberOfElements(greaterThan(0)))
                .check(classesInPackage(packageName));
    }

    void rejectEmptyPackages(List<String> packageNames) {
        for (String packageName : packageNames) {
            rejectEmptyPackage(packageName);
        }
    }

}
