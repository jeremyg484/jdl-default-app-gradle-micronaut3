package tech.jhipster.sample.service.impl;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.Location;
import tech.jhipster.sample.repository.LocationRepository;
import tech.jhipster.sample.service.LocationService;
import tech.jhipster.sample.service.dto.LocationDTO;
import tech.jhipster.sample.service.mapper.LocationMapper;

/**
 * Service Implementation for managing {@link Location}.
 */
@Singleton
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    /**
     * Update a location.
     *
     * @param locationDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        log.debug("Request to update Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.update(location);
        return locationMapper.toDto(location);
    }

    /**
     * Get all the locations.
     *
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public List<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findAll().stream().map(locationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
