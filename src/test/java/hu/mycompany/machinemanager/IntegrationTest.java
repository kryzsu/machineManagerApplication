package hu.mycompany.machinemanager;

import hu.mycompany.machinemanager.MachineManagerApplicationApp;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = MachineManagerApplicationApp.class)
public @interface IntegrationTest {
}
