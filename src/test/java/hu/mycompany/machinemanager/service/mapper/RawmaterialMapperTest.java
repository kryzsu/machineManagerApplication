package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RawmaterialMapperTest {

    private RawmaterialMapper rawmaterialMapper;

    @BeforeEach
    public void setUp() {
        rawmaterialMapper = new RawmaterialMapperImpl();
    }
}
