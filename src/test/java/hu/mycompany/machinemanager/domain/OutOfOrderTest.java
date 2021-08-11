package hu.mycompany.machinemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import hu.mycompany.machinemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class OutOfOrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutOfOrder.class);
        OutOfOrder outOfOrder1 = new OutOfOrder();
        outOfOrder1.setId(1L);
        OutOfOrder outOfOrder2 = new OutOfOrder();
        outOfOrder2.setId(outOfOrder1.getId());
        assertThat(outOfOrder1).isEqualTo(outOfOrder2);
        outOfOrder2.setId(2L);
        assertThat(outOfOrder1).isNotEqualTo(outOfOrder2);
        outOfOrder1.setId(null);
        assertThat(outOfOrder1).isNotEqualTo(outOfOrder2);
    }
}
