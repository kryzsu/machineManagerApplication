package hu.mycompany.machinemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutOfOrderMapperTest {

    private OutOfOrderMapper outOfOrderMapper;

    @BeforeEach
    public void setUp() {
        outOfOrderMapper = new OutOfOrderMapperImpl();
    }
}
