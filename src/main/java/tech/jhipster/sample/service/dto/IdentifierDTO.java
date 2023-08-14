package tech.jhipster.sample.service.dto;

import io.micronaut.core.annotation.Introspected;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tech.jhipster.sample.domain.Identifier} entity.
 */
@Introspected
public class IdentifierDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentifierDTO)) {
            return false;
        }

        IdentifierDTO identifierDTO = (IdentifierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, identifierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentifierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
