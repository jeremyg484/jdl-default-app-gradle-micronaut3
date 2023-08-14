package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.GoldenBadge;
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.service.dto.GoldenBadgeDTO;
import tech.jhipster.sample.service.dto.IdentifierDTO;

/**
 * Mapper for the entity {@link GoldenBadge} and its DTO {@link GoldenBadgeDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface GoldenBadgeMapper extends EntityMapper<GoldenBadgeDTO, GoldenBadge> {
    @Mapping(target = "iden", source = "iden", qualifiedByName = "identifierName")
    GoldenBadgeDTO toDto(GoldenBadge s);

    @Named("identifierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IdentifierDTO toDtoIdentifierName(Identifier identifier);
}
