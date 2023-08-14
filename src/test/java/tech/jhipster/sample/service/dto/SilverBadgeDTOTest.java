package tech.jhipster.sample.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class SilverBadgeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SilverBadgeDTO.class);
        SilverBadgeDTO silverBadgeDTO1 = new SilverBadgeDTO();
        silverBadgeDTO1.setId(1L);
        SilverBadgeDTO silverBadgeDTO2 = new SilverBadgeDTO();
        assertThat(silverBadgeDTO1).isNotEqualTo(silverBadgeDTO2);
        silverBadgeDTO2.setId(silverBadgeDTO1.getId());
        assertThat(silverBadgeDTO1).isEqualTo(silverBadgeDTO2);
        silverBadgeDTO2.setId(2L);
        assertThat(silverBadgeDTO1).isNotEqualTo(silverBadgeDTO2);
        silverBadgeDTO1.setId(null);
        assertThat(silverBadgeDTO1).isNotEqualTo(silverBadgeDTO2);
    }
}
