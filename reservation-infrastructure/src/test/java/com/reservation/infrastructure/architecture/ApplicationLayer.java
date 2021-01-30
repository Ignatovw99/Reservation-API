package com.reservation.infrastructure.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.ArrayList;
import java.util.List;

class ApplicationLayer extends ArchitectureUnit {

    private final HexagonalArchitecture archContext;

    private final List<String> inboundPortsPackages;

    private final List<String> outboundPortsPackages;

    private final List<String> servicesPackages;

    ApplicationLayer(String unitPackage, HexagonalArchitecture archContext) {
        super(unitPackage);
        this.archContext = archContext;
        inboundPortsPackages = new ArrayList<>();
        outboundPortsPackages = new ArrayList<>();
        servicesPackages = new ArrayList<>();
    }

    private List<String> getAllInnerApplicationPackages() {
        List<String> packages = new ArrayList<>();
        packages.addAll(inboundPortsPackages);
        packages.addAll(outboundPortsPackages);
        return packages;
    }

    ApplicationLayer inboundPort(String packageName) {
        inboundPortsPackages.add(getAbsolutePackageName(packageName));
        return this;
    }

    ApplicationLayer outboundPort(String packageName) {
        outboundPortsPackages.add(getAbsolutePackageName(packageName));
        return this;
    }

    ApplicationLayer service(String packageName) {
        servicesPackages.add(getAbsolutePackageName(packageName));
        return this;
    }

    HexagonalArchitecture and() {
        return archContext;
    }

    void doesNotDependOn(String packageName, JavaClasses classes) {
        rejectDependency(unitPackage, packageName, classes);
    }

    void inboundAndOutboundPortsDoesNotDependOnEachOther(JavaClasses javaClasses) {
        rejectAnyDependency(inboundPortsPackages, outboundPortsPackages, javaClasses);
        rejectAnyDependency(outboundPortsPackages, inboundPortsPackages, javaClasses);
    }

    void doesNotContainEmptyPackages() {
        rejectEmptyPackages(getAllInnerApplicationPackages());
    }
}
