package com.reservation.infrastructure;//package com.reservaton.infrastructure;
//
////import com.reservation.application.ReservationApiApplication;
//import com.reservaton.infrastructure.model.Foo;
//import com.tngtech.archunit.core.domain.JavaClass;
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.junit.AnalyzeClasses;
//import com.tngtech.archunit.junit.ArchTest;
//import org.junit.jupiter.api.Test;
//
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
//
//public class CodeConventionRulesTest {
//
//    @Test
//    public void loggerShouldBePrivateStaticField() {
//        JavaClasses javaClasses = new ClassFileImporter()
//                .importPackagesOf(ReservationApiInfrastructureApplication.class, ReservationApiApplication.class);
//        classes().that().resideInAPackage("com.reservation.application.port.inbound..")
//                .should()
//                .haveOnlyFinalFields()
//                .check(javaClasses);
//    }
//
//}
