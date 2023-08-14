package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.service.dto.IdentifierDTO;

/**
 * Mapper for the entity {@link Identifier} and its DTO {@link IdentifierDTO}.
 */
@Mapper(componentModel = "jsr330")
public interface IdentifierMapper extends EntityMapper<IdentifierDTO, Identifier> {}
