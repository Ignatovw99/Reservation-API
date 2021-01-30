package com.reservation.infrastructure.architecture;//package com.reservaton.infrastructure.architecture;
//
//import com.reservation.application.ReservationApiApplication;
//import com.reservaton.infrastructure.ReservationApiInfrastructureApplication;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.junit.AnalyzeClasses;
//import com.tngtech.archunit.junit.ArchTest;
//import org.junit.jupiter.api.Test;
//
//public class ArchitectureTest {
//
//    @Test
//    public void validateHexagonalContextDependencyRules() {
//        HexagonalArchitecture.boundedContext("com.reservation")
////                .withDomain("domain")
////                    .model("model")
////                    .service("service")
////                .and()
//                .withApplication("application")
//                    .service("service")
//                    .inboundPort("port.inbound")
//                    .outboundPort("port.outbound")
//                .and()
////                .withInfrastructure("infrastructure")
//                .checkArchitecture(new ClassFileImporter()
//                        .importPackagesOf(ReservationApiInfrastructureApplication.class, ReservationApiApplication.class));
//    }
//}
