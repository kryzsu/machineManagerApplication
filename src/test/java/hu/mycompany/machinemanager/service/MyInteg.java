package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
public class MyInteg {

    @Autowired
    private MachineRepository machineRepository;

    @Test
    public void test1() {
        List<Machine> machineList = machineRepository.findAll();
        Assert.assertTrue(machineList.size() > 0);
    }
}
