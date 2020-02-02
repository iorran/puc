package com.puc.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.app.web.rest.TestUtil;

public class DamTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dam.class);
        Dam dam1 = new Dam();
        dam1.setId(1L);
        Dam dam2 = new Dam();
        dam2.setId(dam1.getId());
        assertThat(dam1).isEqualTo(dam2);
        dam2.setId(2L);
        assertThat(dam1).isNotEqualTo(dam2);
        dam1.setId(null);
        assertThat(dam1).isNotEqualTo(dam2);
    }
}
