package tech.jhipster.sample.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class IdentifierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentifierDTO.class);
        IdentifierDTO identifierDTO1 = new IdentifierDTO();
        identifierDTO1.setId(1L);
        IdentifierDTO identifierDTO2 = new IdentifierDTO();
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
        identifierDTO2.setId(identifierDTO1.getId());
        assertThat(identifierDTO1).isEqualTo(identifierDTO2);
        identifierDTO2.setId(2L);
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
        identifierDTO1.setId(null);
        assertThat(identifierDTO1).isNotEqualTo(identifierDTO2);
    }
}
