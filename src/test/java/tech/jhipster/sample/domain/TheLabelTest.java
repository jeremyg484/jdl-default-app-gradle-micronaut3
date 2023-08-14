package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class TheLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TheLabel.class);
        TheLabel theLabel1 = new TheLabel();
        theLabel1.setId(1L);
        TheLabel theLabel2 = new TheLabel();
        theLabel2.setId(theLabel1.getId());
        assertThat(theLabel1).isEqualTo(theLabel2);
        theLabel2.setId(2L);
        assertThat(theLabel1).isNotEqualTo(theLabel2);
        theLabel1.setId(null);
        assertThat(theLabel1).isNotEqualTo(theLabel2);
    }
}
