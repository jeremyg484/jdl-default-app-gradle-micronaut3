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
import tech.jhipster.sample.domain.Bank;
import tech.jhipster.sample.repository.BankRepository;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Bank}.
 */
@Controller("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankRepository bankRepository;

    public BankResource(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
     * {@code POST  /banks} : Create a new bank.
     *
     * @param bank the bank to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new bank, or with status {@code 400 (Bad Request)} if the bank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/banks")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Bank> createBank(@Body Bank bank) throws URISyntaxException {
        log.debug("REST request to save Bank : {}", bank);
        if (bank.getId() != null) {
            throw new BadRequestAlertException("A new bank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bank result = bankRepository.save(bank);
        URI location = new URI("/api/banks/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /banks/:id} : Updates an existing bank.
     *
     * @param bank the bank to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated bank,
     * or with status {@code 400 (Bad Request)} if the bank is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/banks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Bank> updateBank(@Body Bank bank, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Bank : {}", bank);
        if (bank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bank.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Bank result = bankRepository.update(bank);
        return HttpResponse
            .ok(result)
            .headers(headers -> HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, bank.getId().toString()));
    }

    /**
     * {@code GET  /banks} : get all the banks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of banks in body.
     */
    @Get("/banks{?eagerload}")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Bank> getAllBanks(HttpRequest request, @Nullable Boolean eagerload) {
        log.debug("REST request to get all Banks");
        return bankRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /banks/:id} : get the "id" bank.
     *
     * @param id the id of the bank to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the bank, or with status {@code 404 (Not Found)}.
     */
    @Get("/banks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Bank> getBank(@PathVariable Long id) {
        log.debug("REST request to get Bank : {}", id);

        return bankRepository.findOneWithEagerRelationships(id);
    }

    /**
     * {@code DELETE  /banks/:id} : delete the "id" bank.
     *
     * @param id the id of the bank to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/banks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteBank(@PathVariable Long id) {
        log.debug("REST request to delete Bank : {}", id);
        bankRepository.deleteById(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
