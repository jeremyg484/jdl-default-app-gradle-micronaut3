package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class GoldenBadgeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoldenBadge.class);
        GoldenBadge goldenBadge1 = new GoldenBadge();
        goldenBadge1.setId(1L);
        GoldenBadge goldenBadge2 = new GoldenBadge();
        goldenBadge2.setId(goldenBadge1.getId());
        assertThat(goldenBadge1).isEqualTo(goldenBadge2);
        goldenBadge2.setId(2L);
        assertThat(goldenBadge1).isNotEqualTo(goldenBadge2);
        goldenBadge1.setId(null);
        assertThat(goldenBadge1).isNotEqualTo(goldenBadge2);
    }
}
