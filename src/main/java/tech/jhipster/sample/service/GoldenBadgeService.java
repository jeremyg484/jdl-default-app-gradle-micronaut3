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
import tech.jhipster.sample.domain.GoldenBadge;
import tech.jhipster.sample.repository.GoldenBadgeRepository;
import tech.jhipster.sample.service.dto.GoldenBadgeDTO;
import tech.jhipster.sample.service.mapper.GoldenBadgeMapper;

/**
 * Service Implementation for managing {@link GoldenBadge}.
 */
@Singleton
@Transactional
public class GoldenBadgeService {

    private final Logger log = LoggerFactory.getLogger(GoldenBadgeService.class);

    private final GoldenBadgeRepository goldenBadgeRepository;

    private final GoldenBadgeMapper goldenBadgeMapper;

    public GoldenBadgeService(GoldenBadgeRepository goldenBadgeRepository, GoldenBadgeMapper goldenBadgeMapper) {
        this.goldenBadgeRepository = goldenBadgeRepository;
        this.goldenBadgeMapper = goldenBadgeMapper;
    }

    /**
     * Save a goldenBadge.
     *
     * @param goldenBadgeDTO the entity to save.
     * @return the persisted entity.
     */
    public GoldenBadgeDTO save(GoldenBadgeDTO goldenBadgeDTO) {
        log.debug("Request to save GoldenBadge : {}", goldenBadgeDTO);
        GoldenBadge goldenBadge = goldenBadgeMapper.toEntity(goldenBadgeDTO);
        goldenBadge = goldenBadgeRepository.save(goldenBadge);
        return goldenBadgeMapper.toDto(goldenBadge);
    }

    /**
     * Update a goldenBadge.
     *
     * @param goldenBadgeDTO the entity to update.
     * @return the persisted entity.
     */
    public GoldenBadgeDTO update(GoldenBadgeDTO goldenBadgeDTO) {
        log.debug("Request to update GoldenBadge : {}", goldenBadgeDTO);
        GoldenBadge goldenBadge = goldenBadgeMapper.toEntity(goldenBadgeDTO);
        goldenBadge = goldenBadgeRepository.update(goldenBadge);
        return goldenBadgeMapper.toDto(goldenBadge);
    }

    /**
     * Get all the goldenBadges.
     *
     * @return the list of entities.
     */
    @ReadOnly
    @Transactional
    public List<GoldenBadgeDTO> findAll() {
        log.debug("Request to get all GoldenBadges");
        return goldenBadgeRepository.findAll().stream().map(goldenBadgeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one goldenBadge by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @ReadOnly
    @Transactional
    public Optional<GoldenBadgeDTO> findOne(Long id) {
        log.debug("Request to get GoldenBadge : {}", id);
        return goldenBadgeRepository.findById(id).map(goldenBadgeMapper::toDto);
    }

    /**
     * Delete the goldenBadge by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoldenBadge : {}", id);
        goldenBadgeRepository.deleteById(id);
    }
}
