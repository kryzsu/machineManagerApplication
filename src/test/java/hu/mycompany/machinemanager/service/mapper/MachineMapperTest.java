package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MachineMapperTest {
    private MachineMapper machineMapper;

    @BeforeEach
    public void setUp() {
        machineMapper = new MachineMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(machineMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(machineMapper.fromId(null)).isNull();
    }
}
