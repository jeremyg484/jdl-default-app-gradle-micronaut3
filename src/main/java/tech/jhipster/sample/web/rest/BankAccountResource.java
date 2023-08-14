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
import tech.jhipster.sample.service.BankAccountService;
import tech.jhipster.sample.service.dto.BankAccountDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.BankAccount}.
 */
@Controller("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "testRootBankAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankAccountService bankAccountService;

    public BankAccountResource(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * {@code POST  /bank-accounts} : Create a new bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new bankAccountDTO, or with status {@code 400 (Bad Request)} if the bankAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/bank-accounts")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<BankAccountDTO> createBankAccount(@Body BankAccountDTO bankAccountDTO) throws URISyntaxException {
        log.debug("REST request to save BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankAccountDTO result = bankAccountService.save(bankAccountDTO);
        URI location = new URI("/api/bank-accounts/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /bank-accounts/:id} : Updates an existing bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated bankAccountDTO,
     * or with status {@code 400 (Bad Request)} if the bankAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/bank-accounts/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<BankAccountDTO> updateBankAccount(@Body BankAccountDTO bankAccountDTO, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to update BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        BankAccountDTO result = bankAccountService.update(bankAccountDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, bankAccountDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /bank-accounts} : get all the bankAccounts.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of bankAccounts in body.
     */
    @Get("/bank-accounts")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<BankAccountDTO> getAllBankAccounts(HttpRequest request) {
        log.debug("REST request to get all BankAccounts");
        return bankAccountService.findAll();
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the bankAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/bank-accounts/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<BankAccountDTO> getBankAccount(@PathVariable Long id) {
        log.debug("REST request to get BankAccount : {}", id);

        return bankAccountService.findOne(id);
    }

    /**
     * {@code DELETE  /bank-accounts/:id} : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/bank-accounts/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        bankAccountService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
