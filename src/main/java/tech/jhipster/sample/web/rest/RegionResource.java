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
import tech.jhipster.sample.service.RegionService;
import tech.jhipster.sample.service.dto.RegionDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Region}.
 */
@Controller("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    private static final String ENTITY_NAME = "region";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegionService regionService;

    public RegionResource(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * {@code POST  /regions} : Create a new region.
     *
     * @param regionDTO the regionDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new regionDTO, or with status {@code 400 (Bad Request)} if the region has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/regions")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<RegionDTO> createRegion(@Body RegionDTO regionDTO) throws URISyntaxException {
        log.debug("REST request to save Region : {}", regionDTO);
        if (regionDTO.getId() != null) {
            throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegionDTO result = regionService.save(regionDTO);
        URI location = new URI("/api/regions/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /regions/:id} : Updates an existing region.
     *
     * @param regionDTO the regionDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated regionDTO,
     * or with status {@code 400 (Bad Request)} if the regionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/regions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<RegionDTO> updateRegion(@Body RegionDTO regionDTO, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Region : {}", regionDTO);
        if (regionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        RegionDTO result = regionService.update(regionDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, regionDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /regions} : get all the regions.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of regions in body.
     */
    @Get("/regions")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<RegionDTO> getAllRegions(HttpRequest request) {
        log.debug("REST request to get all Regions");
        return regionService.findAll();
    }

    /**
     * {@code GET  /regions/:id} : get the "id" region.
     *
     * @param id the id of the regionDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the regionDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/regions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<RegionDTO> getRegion(@PathVariable Long id) {
        log.debug("REST request to get Region : {}", id);

        return regionService.findOne(id);
    }

    /**
     * {@code DELETE  /regions/:id} : delete the "id" region.
     *
     * @param id the id of the regionDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/regions/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteRegion(@PathVariable Long id) {
        log.debug("REST request to delete Region : {}", id);
        regionService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
