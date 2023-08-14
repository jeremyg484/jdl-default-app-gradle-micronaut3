package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Department;
import tech.jhipster.sample.domain.Location;
import tech.jhipster.sample.service.dto.DepartmentDTO;
import tech.jhipster.sample.service.dto.LocationDTO;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    DepartmentDTO toDto(Department s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);
}
