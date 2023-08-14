package tech.jhipster.sample.service;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.repository.IdentifierRepository;
import tech.jhipster.sample.service.dto.IdentifierDTO;
import tech.jhipster.sample.service.mapper.IdentifierMapper;

/**
 * Service Implementation for managing {@link Identifier}.
 */
@Singleton
@Transactional
public class IdentifierService {

    private final Logger log = LoggerFactory.getLogger(IdentifierService.class);

    private final IdentifierRepository identifierRepository;

    private final IdentifierMapper identifierMapper;

    public IdentifierService(IdentifierRepository identifierRepository, IdentifierMapper identifierMapper) {
        this.identifierRepository = identifierRepository;
        this.identifierMapper = identifierMapper;
    }

    /**
     * Save a identifier.
     *
     * @param identifierDTO the entity to save.
     * @return the persisted entity.
     */
    public IdentifierDTO save(IdentifierDTO identifierDTO) {
        log.debug("Request to save Identifier : {}", identifierDTO);
        Identifier identifier = identifierMapper.toEntity(identifierDTO);
        identifier = identifierRepository.save(identifier);
        return identifierMapper.toDto(identifier);
    }

    /**
     * Update a identifier.
     *
     * @param identifierDTO the entity to update.
     * @return the persisted entity.
     */
    public IdentifierDTO update(IdentifierDTO identifierDTO) {
        log.debug("Request to update Identifier : {}", identifierDTO);
        Identifier identifier = identifierMapper.toEntity(identifierDTO);
        identifier = identifierRepository.update(identifier);
        return identifierMapper.toDto(identifier);
    }

    /**
     * Get all the identifiers.
     *
     * @return the list of entities.
     */
    @ReadOnly
    @Transactional
    public List<IdentifierDTO> findAll() {
        log.debug("Request to get all Identifiers");
        return identifierRepository.findAll().stream().map(identifierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one identifier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @ReadOnly
    @Transactional
    public Optional<IdentifierDTO> findOne(Long id) {
        log.debug("Request to get Identifier : {}", id);
        return identifierRepository.findById(id).map(identifierMapper::toDto);
    }

    /**
     * Delete the identifier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Identifier : {}", id);
        identifierRepository.deleteById(id);
    }
}
