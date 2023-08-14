package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bank.
 */
@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_number")
    private Integer bankNumber;

    @ManyToMany
    @JoinTable(
        name = "rel_bank__bank_account",
        joinColumns = @JoinColumn(name = "bank_id"),
        inverseJoinColumns = @JoinColumn(name = "bank_account_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operations", "user", "banks" }, allowSetters = true)
    private Set<BankAccount> bankAccounts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bank id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBankNumber() {
        return this.bankNumber;
    }

    public Bank bankNumber(Integer bankNumber) {
        this.setBankNumber(bankNumber);
        return this;
    }

    public void setBankNumber(Integer bankNumber) {
        this.bankNumber = bankNumber;
    }

    public Set<BankAccount> getBankAccounts() {
        return this.bankAccounts;
    }

    public void setBankAccounts(Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Bank bankAccounts(Set<BankAccount> bankAccounts) {
        this.setBankAccounts(bankAccounts);
        return this;
    }

    public Bank addBankAccount(BankAccount bankAccount) {
        this.bankAccounts.add(bankAccount);
        bankAccount.getBanks().add(this);
        return this;
    }

    public Bank removeBankAccount(BankAccount bankAccount) {
        this.bankAccounts.remove(bankAccount);
        bankAccount.getBanks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        return id != null && id.equals(((Bank) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bank{" +
            "id=" + getId() +
            ", bankNumber=" + getBankNumber() +
            "}";
    }
}
