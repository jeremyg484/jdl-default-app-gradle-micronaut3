package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.jhipster.sample.domain.Bank;
import tech.jhipster.sample.repository.BankRepository;

/**
 * Integration tests for the {@Link BankResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankResourceIT {

    private static final Integer DEFAULT_BANK_NUMBER = 1;
    private static final Integer UPDATED_BANK_NUMBER = 2;

    @Inject
    private BankRepository bankRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Bank bank;

    @BeforeEach
    public void initTest() {
        bank = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Bank bank = new Bank().bankNumber(DEFAULT_BANK_NUMBER);
        return bank;
    }

    /**
     * Delete all bank entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Bank.class);
    }

    @Test
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        HttpResponse<Bank> response = client.exchange(HttpRequest.POST("/api/banks", bank), Bank.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = bankList.get(bankList.size() - 1);

        assertThat(testBank.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
    }

    @Test
    public void createBankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank with an existing ID
        bank.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Bank> response = client
            .exchange(HttpRequest.POST("/api/banks", bank), Bank.class)
            .onErrorReturn(t -> (HttpResponse<Bank>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bankList w/ all the banks
        List<Bank> banks = client.retrieve(HttpRequest.GET("/api/banks?eagerload=true"), Argument.listOf(Bank.class)).blockingFirst();
        Bank testBank = banks.get(0);

        assertThat(testBank.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
    }

    @Test
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        Bank testBank = client.retrieve(HttpRequest.GET("/api/banks/" + bank.getId()), Bank.class).blockingFirst();

        assertThat(testBank.getBankNumber()).isEqualTo(DEFAULT_BANK_NUMBER);
    }

    @Test
    public void getNonExistingBank() throws Exception {
        // Get the bank
        @SuppressWarnings("unchecked")
        HttpResponse<Bank> response = client
            .exchange(HttpRequest.GET("/api/banks/" + Long.MAX_VALUE), Bank.class)
            .onErrorReturn(t -> (HttpResponse<Bank>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        Bank updatedBank = bankRepository.findById(bank.getId()).get();

        updatedBank.bankNumber(UPDATED_BANK_NUMBER);

        @SuppressWarnings("unchecked")
        HttpResponse<Bank> response = client
            .exchange(HttpRequest.PUT("/api/banks/" + bank.getId(), updatedBank), Bank.class)
            .onErrorReturn(t -> (HttpResponse<Bank>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = bankList.get(bankList.size() - 1);

        assertThat(testBank.getBankNumber()).isEqualTo(UPDATED_BANK_NUMBER);
    }

    @Test
    public void updateNonExistingBank() throws Exception {
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Create the Bank

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Bank> response = client
            .exchange(HttpRequest.PUT("/api/banks/" + bank.getId(), bank), Bank.class)
            .onErrorReturn(t -> (HttpResponse<Bank>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBank() throws Exception {
        // Initialize the database with one entity
        bankRepository.saveAndFlush(bank);

        int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Delete the bank
        @SuppressWarnings("unchecked")
        HttpResponse<Bank> response = client
            .exchange(HttpRequest.DELETE("/api/banks/" + bank.getId()), Bank.class)
            .onErrorReturn(t -> (HttpResponse<Bank>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = new Bank();
        bank1.setId(1L);
        Bank bank2 = new Bank();
        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);
        bank2.setId(2L);
        assertThat(bank1).isNotEqualTo(bank2);
        bank1.setId(null);
        assertThat(bank1).isNotEqualTo(bank2);
    }
}
