package hu.mycompany.machinemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hu.mycompany.machinemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RawmaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rawmaterial.class);
        Rawmaterial rawmaterial1 = new Rawmaterial();
        rawmaterial1.setId(1L);
        Rawmaterial rawmaterial2 = new Rawmaterial();
        rawmaterial2.setId(rawmaterial1.getId());
        assertThat(rawmaterial1).isEqualTo(rawmaterial2);
        rawmaterial2.setId(2L);
        assertThat(rawmaterial1).isNotEqualTo(rawmaterial2);
        rawmaterial1.setId(null);
        assertThat(rawmaterial1).isNotEqualTo(rawmaterial2);
    }
}
