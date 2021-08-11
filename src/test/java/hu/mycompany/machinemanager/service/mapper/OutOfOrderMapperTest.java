package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OutOfOrderMapperTest {
    private OutOfOrderMapper outOfOrderMapper;

    @BeforeEach
    public void setUp() {
        outOfOrderMapper = new OutOfOrderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(outOfOrderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(outOfOrderMapper.fromId(null)).isNull();
    }
}
