package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Country;
import tech.jhipster.sample.domain.Region;
import tech.jhipster.sample.service.dto.CountryDTO;
import tech.jhipster.sample.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    RegionDTO toDto(Region s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
