package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TheLabel.
 */
@Entity
@Table(name = "the_label")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TheLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "label_name", nullable = false)
    private String labelName;

    @ManyToMany(mappedBy = "theLabels")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "theLabels", "bankAccount" }, allowSetters = true)
    private Set<Operation> operations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TheLabel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public TheLabel labelName(String labelName) {
        this.setLabelName(labelName);
        return this;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Set<Operation> getOperations() {
        return this.operations;
    }

    public void setOperations(Set<Operation> operations) {
        if (this.operations != null) {
            this.operations.forEach(i -> i.removeTheLabel(this));
        }
        if (operations != null) {
            operations.forEach(i -> i.addTheLabel(this));
        }
        this.operations = operations;
    }

    public TheLabel operations(Set<Operation> operations) {
        this.setOperations(operations);
        return this;
    }

    public TheLabel addOperation(Operation operation) {
        this.operations.add(operation);
        operation.getTheLabels().add(this);
        return this;
    }

    public TheLabel removeOperation(Operation operation) {
        this.operations.remove(operation);
        operation.getTheLabels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TheLabel)) {
            return false;
        }
        return id != null && id.equals(((TheLabel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TheLabel{" +
            "id=" + getId() +
            ", labelName='" + getLabelName() + "'" +
            "}";
    }
}
