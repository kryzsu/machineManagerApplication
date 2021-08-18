package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewMapperTest {
    private ViewMapper viewMapper;

    @BeforeEach
    public void setUp() {
        viewMapper = new ViewMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(viewMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(viewMapper.fromId(null)).isNull();
    }
}
