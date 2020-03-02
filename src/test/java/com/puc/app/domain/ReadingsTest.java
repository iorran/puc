package com.puc.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.puc.app.web.rest.TestUtil;

public class ReadingsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Readings.class);
        Readings readings1 = new Readings();
        readings1.setId(1L);
        Readings readings2 = new Readings();
        readings2.setId(readings1.getId());
        assertThat(readings1).isEqualTo(readings2);
        readings2.setId(2L);
        assertThat(readings1).isNotEqualTo(readings2);
        readings1.setId(null);
        assertThat(readings1).isNotEqualTo(readings2);
    }
}
