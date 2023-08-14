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
import tech.jhipster.sample.service.GoldenBadgeService;
import tech.jhipster.sample.service.dto.GoldenBadgeDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.GoldenBadge}.
 */
@Controller("/api")
public class GoldenBadgeResource {

    private final Logger log = LoggerFactory.getLogger(GoldenBadgeResource.class);

    private static final String ENTITY_NAME = "goldenBadge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoldenBadgeService goldenBadgeService;

    public GoldenBadgeResource(GoldenBadgeService goldenBadgeService) {
        this.goldenBadgeService = goldenBadgeService;
    }

    /**
     * {@code POST  /golden-badges} : Create a new goldenBadge.
     *
     * @param goldenBadgeDTO the goldenBadgeDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new goldenBadgeDTO, or with status {@code 400 (Bad Request)} if the goldenBadge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/golden-badges")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<GoldenBadgeDTO> createGoldenBadge(@Body GoldenBadgeDTO goldenBadgeDTO) throws URISyntaxException {
        log.debug("REST request to save GoldenBadge : {}", goldenBadgeDTO);
        if (goldenBadgeDTO.getId() != null) {
            throw new BadRequestAlertException("A new goldenBadge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GoldenBadgeDTO result = goldenBadgeService.save(goldenBadgeDTO);
        URI location = new URI("/api/golden-badges/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /golden-badges/:id} : Updates an existing goldenBadge.
     *
     * @param goldenBadgeDTO the goldenBadgeDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated goldenBadgeDTO,
     * or with status {@code 400 (Bad Request)} if the goldenBadgeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goldenBadgeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/golden-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<GoldenBadgeDTO> updateGoldenBadge(@Body GoldenBadgeDTO goldenBadgeDTO, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to update GoldenBadge : {}", goldenBadgeDTO);
        if (goldenBadgeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, goldenBadgeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        GoldenBadgeDTO result = goldenBadgeService.update(goldenBadgeDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, goldenBadgeDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /golden-badges} : get all the goldenBadges.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of goldenBadges in body.
     */
    @Get("/golden-badges")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<GoldenBadgeDTO> getAllGoldenBadges(HttpRequest request) {
        log.debug("REST request to get all GoldenBadges");
        return goldenBadgeService.findAll();
    }

    /**
     * {@code GET  /golden-badges/:id} : get the "id" goldenBadge.
     *
     * @param id the id of the goldenBadgeDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the goldenBadgeDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/golden-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<GoldenBadgeDTO> getGoldenBadge(@PathVariable Long id) {
        log.debug("REST request to get GoldenBadge : {}", id);

        return goldenBadgeService.findOne(id);
    }

    /**
     * {@code DELETE  /golden-badges/:id} : delete the "id" goldenBadge.
     *
     * @param id the id of the goldenBadgeDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/golden-badges/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteGoldenBadge(@PathVariable Long id) {
        log.debug("REST request to delete GoldenBadge : {}", id);
        goldenBadgeService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
