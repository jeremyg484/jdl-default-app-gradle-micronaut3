package tech.jhipster.sample.domain.enumeration;

/**
 * The BankAccountType enumeration.
 */
public enum BankAccountType {
    CHECKING("checking_account"),
    SAVINGS("savings_account"),
    LOAN("loan_account");

    private final String value;

    BankAccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
