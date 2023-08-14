package tech.jhipster.sample.web.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.ReadOnly;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.TheLabel;
import tech.jhipster.sample.service.TheLabelService;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.util.PaginationUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.TheLabel}.
 */
@Controller("/api")
public class TheLabelResource {

    private final Logger log = LoggerFactory.getLogger(TheLabelResource.class);

    private static final String ENTITY_NAME = "testRootTheLabel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TheLabelService theLabelService;

    public TheLabelResource(TheLabelService theLabelService) {
        this.theLabelService = theLabelService;
    }

    /**
     * {@code POST  /the-labels} : Create a new theLabel.
     *
     * @param theLabel the theLabel to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new theLabel, or with status {@code 400 (Bad Request)} if the theLabel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/the-labels")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<TheLabel> createTheLabel(@Body TheLabel theLabel) throws URISyntaxException {
        log.debug("REST request to save TheLabel : {}", theLabel);
        if (theLabel.getId() != null) {
            throw new BadRequestAlertException("A new theLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TheLabel result = theLabelService.save(theLabel);
        URI location = new URI("/api/the-labels/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /the-labels/:id} : Updates an existing theLabel.
     *
     * @param theLabel the theLabel to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated theLabel,
     * or with status {@code 400 (Bad Request)} if the theLabel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/the-labels/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<TheLabel> updateTheLabel(@Body TheLabel theLabel, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update TheLabel : {}", theLabel);
        if (theLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, theLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        TheLabel result = theLabelService.update(theLabel);
        return HttpResponse
            .ok(result)
            .headers(headers -> HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, theLabel.getId().toString())
            );
    }

    /**
     * {@code GET  /the-labels} : get all the theLabels.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of theLabels in body.
     */
    @Get("/the-labels")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<TheLabel>> getAllTheLabels(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of TheLabels");
        Page<TheLabel> page = theLabelService.findAll(pageable);
        return HttpResponse
            .ok(page.getContent())
            .headers(headers -> PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /the-labels/:id} : get the "id" theLabel.
     *
     * @param id the id of the theLabel to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the theLabel, or with status {@code 404 (Not Found)}.
     */
    @Get("/the-labels/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<TheLabel> getTheLabel(@PathVariable Long id) {
        log.debug("REST request to get TheLabel : {}", id);

        return theLabelService.findOne(id);
    }

    /**
     * {@code DELETE  /the-labels/:id} : delete the "id" theLabel.
     *
     * @param id the id of the theLabel to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/the-labels/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteTheLabel(@PathVariable Long id) {
        log.debug("REST request to delete TheLabel : {}", id);
        theLabelService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
