package tech.jhipster.sample.web.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.service.LocationService;
import tech.jhipster.sample.service.dto.LocationDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Location}.
 */
@Controller("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationService locationService;

    public LocationResource(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param locationDTO the locationDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new locationDTO, or with status {@code 400 (Bad Request)} if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/locations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<LocationDTO> createLocation(@Body LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to save Location : {}", locationDTO);
        if (locationDTO.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationDTO result = locationService.save(locationDTO);
        URI location = new URI("/api/locations/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /locations/:id} : Updates an existing location.
     *
     * @param locationDTO the locationDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated locationDTO,
     * or with status {@code 400 (Bad Request)} if the locationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/locations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<LocationDTO> updateLocation(@Body LocationDTO locationDTO, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Location : {}", locationDTO);
        if (locationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        LocationDTO result = locationService.update(locationDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, locationDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of locations in body.
     */
    @Get("/locations")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<LocationDTO> getAllLocations(HttpRequest request) {
        log.debug("REST request to get all Locations");
        return locationService.findAll();
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the locationDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the locationDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/locations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<LocationDTO> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);

        return locationService.findOne(id);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the locationDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/locations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
