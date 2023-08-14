package tech.jhipster.sample.service;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.TheLabel;
import tech.jhipster.sample.repository.TheLabelRepository;

/**
 * Service Implementation for managing {@link TheLabel}.
 */
@Singleton
@Transactional
public class TheLabelService {

    private final Logger log = LoggerFactory.getLogger(TheLabelService.class);

    private final TheLabelRepository theLabelRepository;

    public TheLabelService(TheLabelRepository theLabelRepository) {
        this.theLabelRepository = theLabelRepository;
    }

    /**
     * Save a theLabel.
     *
     * @param theLabel the entity to save.
     * @return the persisted entity.
     */
    public TheLabel save(TheLabel theLabel) {
        log.debug("Request to save TheLabel : {}", theLabel);
        return theLabelRepository.save(theLabel);
    }

    /**
     * Update a theLabel.
     *
     * @param theLabel the entity to update.
     * @return the persisted entity.
     */
    public TheLabel update(TheLabel theLabel) {
        log.debug("Request to update TheLabel : {}", theLabel);
        return theLabelRepository.update(theLabel);
    }

    /**
     * Get all the theLabels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @ReadOnly
    @Transactional
    public Page<TheLabel> findAll(Pageable pageable) {
        log.debug("Request to get all TheLabels");
        return theLabelRepository.findAll(pageable);
    }

    /**
     * Get one theLabel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @ReadOnly
    @Transactional
    public Optional<TheLabel> findOne(Long id) {
        log.debug("Request to get TheLabel : {}", id);
        return theLabelRepository.findById(id);
    }

    /**
     * Delete the theLabel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TheLabel : {}", id);
        theLabelRepository.deleteById(id);
    }
}
