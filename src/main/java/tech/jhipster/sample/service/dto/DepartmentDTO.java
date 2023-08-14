package tech.jhipster.sample.service.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tech.jhipster.sample.domain.Department} entity.
 */
@Introspected
public class DepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @Lob
    private byte[] advertisement;

    private String advertisementContentType;

    @Lob
    private byte[] logo;

    private String logoContentType;
    private LocationDTO location;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(byte[] advertisement) {
        this.advertisement = advertisement;
    }

    public String getAdvertisementContentType() {
        return advertisementContentType;
    }

    public void setAdvertisementContentType(String advertisementContentType) {
        this.advertisementContentType = advertisementContentType;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        DepartmentDTO departmentDTO = (DepartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", advertisement='" + getAdvertisement() + "'" +
            ", logo='" + getLogo() + "'" +
            ", location=" + getLocation() +
            "}";
    }
}
