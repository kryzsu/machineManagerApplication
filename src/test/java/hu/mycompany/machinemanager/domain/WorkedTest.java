package hu.mycompany.machinemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hu.mycompany.machinemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Worked.class);
        Worked worked1 = new Worked();
        worked1.setId(1L);
        Worked worked2 = new Worked();
        worked2.setId(worked1.getId());
        assertThat(worked1).isEqualTo(worked2);
        worked2.setId(2L);
        assertThat(worked1).isNotEqualTo(worked2);
        worked1.setId(null);
        assertThat(worked1).isNotEqualTo(worked2);
    }
}
