package com.puc.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.app.web.rest.TestUtil;

public class EquipmentMaintenanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentMaintenance.class);
        EquipmentMaintenance equipmentMaintenance1 = new EquipmentMaintenance();
        equipmentMaintenance1.setId(1L);
        EquipmentMaintenance equipmentMaintenance2 = new EquipmentMaintenance();
        equipmentMaintenance2.setId(equipmentMaintenance1.getId());
        assertThat(equipmentMaintenance1).isEqualTo(equipmentMaintenance2);
        equipmentMaintenance2.setId(2L);
        assertThat(equipmentMaintenance1).isNotEqualTo(equipmentMaintenance2);
        equipmentMaintenance1.setId(null);
        assertThat(equipmentMaintenance1).isNotEqualTo(equipmentMaintenance2);
    }
}
