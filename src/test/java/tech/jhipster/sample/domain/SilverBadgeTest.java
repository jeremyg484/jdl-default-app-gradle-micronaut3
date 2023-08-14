package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class SilverBadgeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SilverBadge.class);
        SilverBadge silverBadge1 = new SilverBadge();
        silverBadge1.setId(1L);
        SilverBadge silverBadge2 = new SilverBadge();
        silverBadge2.setId(silverBadge1.getId());
        assertThat(silverBadge1).isEqualTo(silverBadge2);
        silverBadge2.setId(2L);
        assertThat(silverBadge1).isNotEqualTo(silverBadge2);
        silverBadge1.setId(null);
        assertThat(silverBadge1).isNotEqualTo(silverBadge2);
    }
}
