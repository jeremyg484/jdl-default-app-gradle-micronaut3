package tech.jhipster.sample.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.jhipster.sample.web.rest.TestUtil;

class BankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = new Bank();
        bank1.setId(1L);
        Bank bank2 = new Bank();
        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);
        bank2.setId(2L);
        assertThat(bank1).isNotEqualTo(bank2);
        bank1.setId(null);
        assertThat(bank1).isNotEqualTo(bank2);
    }
}
