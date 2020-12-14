package hu.mycompany.machinemanager;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("hu.mycompany.machinemanager");

        noClasses()
            .that()
            .resideInAnyPackage("hu.mycompany.machinemanager.service..")
            .or()
            .resideInAnyPackage("hu.mycompany.machinemanager.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..hu.mycompany.machinemanager.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
