package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Country;
import tech.jhipster.sample.domain.Location;
import tech.jhipster.sample.service.dto.CountryDTO;
import tech.jhipster.sample.service.dto.LocationDTO;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    CountryDTO toDto(Country s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);
}
