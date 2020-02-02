package com.puc.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.app.web.rest.TestUtil;

public class EquipmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = new Equipment();
        equipment1.setId(1L);
        Equipment equipment2 = new Equipment();
        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);
        equipment2.setId(2L);
        assertThat(equipment1).isNotEqualTo(equipment2);
        equipment1.setId(null);
        assertThat(equipment1).isNotEqualTo(equipment2);
    }
}
