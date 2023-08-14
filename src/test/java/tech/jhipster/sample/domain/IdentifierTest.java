package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class IdentifierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Identifier.class);
        Identifier identifier1 = new Identifier();
        identifier1.setId(1L);
        Identifier identifier2 = new Identifier();
        identifier2.setId(identifier1.getId());
        assertThat(identifier1).isEqualTo(identifier2);
        identifier2.setId(2L);
        assertThat(identifier1).isNotEqualTo(identifier2);
        identifier1.setId(null);
        assertThat(identifier1).isNotEqualTo(identifier2);
    }
}
