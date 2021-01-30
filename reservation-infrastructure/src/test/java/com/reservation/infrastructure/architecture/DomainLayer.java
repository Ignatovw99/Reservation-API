package com.reservation.infrastructure.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DomainLayer extends ArchitectureUnit {

    private final HexagonalArchitecture archContext;

    private final List<String> domainPackages;

    DomainLayer(String unitPackage, HexagonalArchitecture archContext) {
        super(unitPackage);
        domainPackages = new ArrayList<>();
        this.archContext = archContext;
    }

    DomainLayer model(String domainModelPackage) {
        domainPackages.add(getAbsolutePackageName(domainModelPackage));
        return this;
    }

    DomainLayer service(String domainServicePackage) {
        domainPackages.add(getAbsolutePackageName(domainServicePackage));
        return this;
    }

    HexagonalArchitecture and() {
        return archContext;
    }

    void domainDoesNotDependOnOtherPackages(JavaClasses classes) {
        rejectAnyDependency(
                domainPackages, Collections.singletonList(archContext.applicationLayer.unitPackage),
                classes
        );
        rejectAnyDependency(
                domainPackages, Collections.singletonList(archContext.infrastructurePackage),
                classes
        );
    }
}
