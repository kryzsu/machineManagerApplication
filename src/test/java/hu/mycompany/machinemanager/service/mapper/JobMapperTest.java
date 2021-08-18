package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JobMapperTest {
    private JobMapper jobMapper;

    @BeforeEach
    public void setUp() {
        jobMapper = new JobMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(jobMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(jobMapper.fromId(null)).isNull();
    }
}
