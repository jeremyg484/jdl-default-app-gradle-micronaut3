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
import tech.jhipster.sample.service.SilverBadgeService;
import tech.jhipster.sample.service.dto.SilverBadgeDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.SilverBadge}.
 */
@Controller("/api")
public class SilverBadgeResource {

    private final Logger log = LoggerFactory.getLogger(SilverBadgeResource.class);

    private static final String ENTITY_NAME = "silverBadge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SilverBadgeService silverBadgeService;

    public SilverBadgeResource(SilverBadgeService silverBadgeService) {
        this.silverBadgeService = silverBadgeService;
    }

    /**
     * {@code POST  /silver-badges} : Create a new silverBadge.
     *
     * @param silverBadgeDTO the silverBadgeDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new silverBadgeDTO, or with status {@code 400 (Bad Request)} if the silverBadge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/silver-badges")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<SilverBadgeDTO> createSilverBadge(@Body SilverBadgeDTO silverBadgeDTO) throws URISyntaxException {
        log.debug("REST request to save SilverBadge : {}", silverBadgeDTO);
        if (silverBadgeDTO.getId() != null) {
            throw new BadRequestAlertException("A new silverBadge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SilverBadgeDTO result = silverBadgeService.save(silverBadgeDTO);
        URI location = new URI("/api/silver-badges/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /silver-badges/:id} : Updates an existing silverBadge.
     *
     * @param silverBadgeDTO the silverBadgeDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated silverBadgeDTO,
     * or with status {@code 400 (Bad Request)} if the silverBadgeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the silverBadgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/silver-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<SilverBadgeDTO> updateSilverBadge(@Body SilverBadgeDTO silverBadgeDTO, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to update SilverBadge : {}", silverBadgeDTO);
        if (silverBadgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, silverBadgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        SilverBadgeDTO result = silverBadgeService.update(silverBadgeDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, silverBadgeDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /silver-badges} : get all the silverBadges.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of silverBadges in body.
     */
    @Get("/silver-badges")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<SilverBadgeDTO> getAllSilverBadges(HttpRequest request) {
        log.debug("REST request to get all SilverBadges");
        return silverBadgeService.findAll();
    }

    /**
     * {@code GET  /silver-badges/:id} : get the "id" silverBadge.
     *
     * @param id the id of the silverBadgeDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the silverBadgeDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/silver-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<SilverBadgeDTO> getSilverBadge(@PathVariable Long id) {
        log.debug("REST request to get SilverBadge : {}", id);

        return silverBadgeService.findOne(id);
    }

    /**
     * {@code DELETE  /silver-badges/:id} : delete the "id" silverBadge.
     *
     * @param id the id of the silverBadgeDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/silver-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteSilverBadge(@PathVariable Long id) {
        log.debug("REST request to delete SilverBadge : {}", id);
        silverBadgeService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
