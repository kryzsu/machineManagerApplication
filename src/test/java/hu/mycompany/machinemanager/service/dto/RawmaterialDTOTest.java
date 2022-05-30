package hu.mycompany.machinemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import hu.mycompany.machinemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RawmaterialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawmaterialDTO.class);
        RawmaterialDTO rawmaterialDTO1 = new RawmaterialDTO();
        rawmaterialDTO1.setId(1L);
        RawmaterialDTO rawmaterialDTO2 = new RawmaterialDTO();
        assertThat(rawmaterialDTO1).isNotEqualTo(rawmaterialDTO2);
        rawmaterialDTO2.setId(rawmaterialDTO1.getId());
        assertThat(rawmaterialDTO1).isEqualTo(rawmaterialDTO2);
        rawmaterialDTO2.setId(2L);
        assertThat(rawmaterialDTO1).isNotEqualTo(rawmaterialDTO2);
        rawmaterialDTO1.setId(null);
        assertThat(rawmaterialDTO1).isNotEqualTo(rawmaterialDTO2);
    }
}
