package hu.mycompany.machinemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import hu.mycompany.machinemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class OutOfOrderDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutOfOrderDTO.class);
        OutOfOrderDTO outOfOrderDTO1 = new OutOfOrderDTO();
        outOfOrderDTO1.setId(1L);
        OutOfOrderDTO outOfOrderDTO2 = new OutOfOrderDTO();
        assertThat(outOfOrderDTO1).isNotEqualTo(outOfOrderDTO2);
        outOfOrderDTO2.setId(outOfOrderDTO1.getId());
        assertThat(outOfOrderDTO1).isEqualTo(outOfOrderDTO2);
        outOfOrderDTO2.setId(2L);
        assertThat(outOfOrderDTO1).isNotEqualTo(outOfOrderDTO2);
        outOfOrderDTO1.setId(null);
        assertThat(outOfOrderDTO1).isNotEqualTo(outOfOrderDTO2);
    }
}
