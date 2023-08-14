package tech.jhipster.sample.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GoldenBadge.
 */
@Entity
@Table(name = "golden_badge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GoldenBadge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    private Identifier iden;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GoldenBadge id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GoldenBadge name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Identifier getIden() {
        return this.iden;
    }

    public void setIden(Identifier identifier) {
        this.iden = identifier;
    }

    public GoldenBadge iden(Identifier identifier) {
        this.setIden(identifier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GoldenBadge)) {
            return false;
        }
        return id != null && id.equals(((GoldenBadge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GoldenBadge{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
