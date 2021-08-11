package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HolidayMapperTest {
    private HolidayMapper holidayMapper;

    @BeforeEach
    public void setUp() {
        holidayMapper = new HolidayMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(holidayMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(holidayMapper.fromId(null)).isNull();
    }
}
