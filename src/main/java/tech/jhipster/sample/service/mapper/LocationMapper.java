package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Location;
import tech.jhipster.sample.service.dto.LocationDTO;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {}
