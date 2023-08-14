package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.domain.SilverBadge;
import tech.jhipster.sample.service.dto.IdentifierDTO;
import tech.jhipster.sample.service.dto.SilverBadgeDTO;

/**
 * Mapper for the entity {@link SilverBadge} and its DTO {@link SilverBadgeDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface SilverBadgeMapper extends EntityMapper<SilverBadgeDTO, SilverBadge> {
    @Mapping(target = "iden", source = "iden", qualifiedByName = "identifierName")
    SilverBadgeDTO toDto(SilverBadge s);

    @Named("identifierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IdentifierDTO toDtoIdentifierName(Identifier identifier);
}
