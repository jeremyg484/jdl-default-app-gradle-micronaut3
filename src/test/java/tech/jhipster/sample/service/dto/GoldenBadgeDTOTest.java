package tech.jhipster.sample.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class GoldenBadgeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoldenBadgeDTO.class);
        GoldenBadgeDTO goldenBadgeDTO1 = new GoldenBadgeDTO();
        goldenBadgeDTO1.setId(1L);
        GoldenBadgeDTO goldenBadgeDTO2 = new GoldenBadgeDTO();
        assertThat(goldenBadgeDTO1).isNotEqualTo(goldenBadgeDTO2);
        goldenBadgeDTO2.setId(goldenBadgeDTO1.getId());
        assertThat(goldenBadgeDTO1).isEqualTo(goldenBadgeDTO2);
        goldenBadgeDTO2.setId(2L);
        assertThat(goldenBadgeDTO1).isNotEqualTo(goldenBadgeDTO2);
        goldenBadgeDTO1.setId(null);
        assertThat(goldenBadgeDTO1).isNotEqualTo(goldenBadgeDTO2);
    }
}
