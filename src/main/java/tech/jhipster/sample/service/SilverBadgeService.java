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
import tech.jhipster.sample.domain.SilverBadge;
import tech.jhipster.sample.repository.SilverBadgeRepository;
import tech.jhipster.sample.service.dto.SilverBadgeDTO;
import tech.jhipster.sample.service.mapper.SilverBadgeMapper;

/**
 * Service Implementation for managing {@link SilverBadge}.
 */
@Singleton
@Transactional
public class SilverBadgeService {

    private final Logger log = LoggerFactory.getLogger(SilverBadgeService.class);

    private final SilverBadgeRepository silverBadgeRepository;

    private final SilverBadgeMapper silverBadgeMapper;

    public SilverBadgeService(SilverBadgeRepository silverBadgeRepository, SilverBadgeMapper silverBadgeMapper) {
        this.silverBadgeRepository = silverBadgeRepository;
        this.silverBadgeMapper = silverBadgeMapper;
    }

    /**
     * Save a silverBadge.
     *
     * @param silverBadgeDTO the entity to save.
     * @return the persisted entity.
     */
    public SilverBadgeDTO save(SilverBadgeDTO silverBadgeDTO) {
        log.debug("Request to save SilverBadge : {}", silverBadgeDTO);
        SilverBadge silverBadge = silverBadgeMapper.toEntity(silverBadgeDTO);
        silverBadge = silverBadgeRepository.save(silverBadge);
        return silverBadgeMapper.toDto(silverBadge);
    }

    /**
     * Update a silverBadge.
     *
     * @param silverBadgeDTO the entity to update.
     * @return the persisted entity.
     */
    public SilverBadgeDTO update(SilverBadgeDTO silverBadgeDTO) {
        log.debug("Request to update SilverBadge : {}", silverBadgeDTO);
        SilverBadge silverBadge = silverBadgeMapper.toEntity(silverBadgeDTO);
        silverBadge = silverBadgeRepository.update(silverBadge);
        return silverBadgeMapper.toDto(silverBadge);
    }

    /**
     * Get all the silverBadges.
     *
     * @return the list of entities.
     */
    @ReadOnly
    @Transactional
    public List<SilverBadgeDTO> findAll() {
        log.debug("Request to get all SilverBadges");
        return silverBadgeRepository.findAll().stream().map(silverBadgeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one silverBadge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @ReadOnly
    @Transactional
    public Optional<SilverBadgeDTO> findOne(Long id) {
        log.debug("Request to get SilverBadge : {}", id);
        return silverBadgeRepository.findById(id).map(silverBadgeMapper::toDto);
    }

    /**
     * Delete the silverBadge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SilverBadge : {}", id);
        silverBadgeRepository.deleteById(id);
    }
}
