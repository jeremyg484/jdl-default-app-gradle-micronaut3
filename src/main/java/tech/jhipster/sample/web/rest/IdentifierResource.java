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
import tech.jhipster.sample.service.IdentifierService;
import tech.jhipster.sample.service.dto.IdentifierDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Identifier}.
 */
@Controller("/api")
public class IdentifierResource {

    private final Logger log = LoggerFactory.getLogger(IdentifierResource.class);

    private static final String ENTITY_NAME = "identifier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentifierService identifierService;

    public IdentifierResource(IdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    /**
     * {@code POST  /identifiers} : Create a new identifier.
     *
     * @param identifierDTO the identifierDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new identifierDTO, or with status {@code 400 (Bad Request)} if the identifier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/identifiers")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<IdentifierDTO> createIdentifier(@Body IdentifierDTO identifierDTO) throws URISyntaxException {
        log.debug("REST request to save Identifier : {}", identifierDTO);
        if (identifierDTO.getId() != null) {
            throw new BadRequestAlertException("A new identifier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdentifierDTO result = identifierService.save(identifierDTO);
        URI location = new URI("/api/identifiers/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /identifiers/:id} : Updates an existing identifier.
     *
     * @param identifierDTO the identifierDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated identifierDTO,
     * or with status {@code 400 (Bad Request)} if the identifierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identifierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/identifiers/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<IdentifierDTO> updateIdentifier(@Body IdentifierDTO identifierDTO, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to update Identifier : {}", identifierDTO);
        if (identifierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identifierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        IdentifierDTO result = identifierService.update(identifierDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, identifierDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /identifiers} : get all the identifiers.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of identifiers in body.
     */
    @Get("/identifiers")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<IdentifierDTO> getAllIdentifiers(HttpRequest request) {
        log.debug("REST request to get all Identifiers");
        return identifierService.findAll();
    }

    /**
     * {@code GET  /identifiers/:id} : get the "id" identifier.
     *
     * @param id the id of the identifierDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the identifierDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/identifiers/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<IdentifierDTO> getIdentifier(@PathVariable Long id) {
        log.debug("REST request to get Identifier : {}", id);

        return identifierService.findOne(id);
    }

    /**
     * {@code DELETE  /identifiers/:id} : delete the "id" identifier.
     *
     * @param id the id of the identifierDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/identifiers/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteIdentifier(@PathVariable Long id) {
        log.debug("REST request to delete Identifier : {}", id);
        identifierService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
