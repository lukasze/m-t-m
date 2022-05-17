package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lab.class);
        Lab lab1 = new Lab();
        lab1.setId(1L);
        Lab lab2 = new Lab();
        lab2.setId(lab1.getId());
        assertThat(lab1).isEqualTo(lab2);
        lab2.setId(2L);
        assertThat(lab1).isNotEqualTo(lab2);
        lab1.setId(null);
        assertThat(lab1).isNotEqualTo(lab2);
    }
}
