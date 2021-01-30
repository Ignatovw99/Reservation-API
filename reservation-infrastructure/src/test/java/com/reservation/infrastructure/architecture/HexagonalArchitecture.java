package com.reservation.infrastructure.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.ArrayList;
import java.util.List;

public class HexagonalArchitecture extends ArchitectureUnit {

    ApplicationLayer applicationLayer;

    DomainLayer domainLayer;

    String infrastructurePackage;

    public static HexagonalArchitecture boundedContext(String basePackage) {
        return new HexagonalArchitecture(basePackage);
    }

    public HexagonalArchitecture(String unitPackage) {
        super(unitPackage);
    }

    public ApplicationLayer withApplication(String applicationPackage) {
        applicationLayer = new ApplicationLayer(getAbsolutePackageName(applicationPackage), this);
        return applicationLayer;
    }

    public HexagonalArchitecture withInfrastructure(String infrastructurePackage) {
        this.infrastructurePackage = getAbsolutePackageName(infrastructurePackage);
        return this;
    }

    public DomainLayer withDomain(String domainPackage) {
        domainLayer = new DomainLayer(getAbsolutePackageName(domainPackage), this);
        return domainLayer;
    }

    public HexagonalArchitecture and() {
        return this;
    }

    public void checkArchitecture(JavaClasses classes) {
        System.out.println();
        applicationLayer.doesNotContainEmptyPackages();
        applicationLayer.doesNotDependOn(infrastructurePackage, classes);
        applicationLayer.inboundAndOutboundPortsDoesNotDependOnEachOther(classes);
        domainLayer.domainDoesNotDependOnOtherPackages(classes);
    }
}
