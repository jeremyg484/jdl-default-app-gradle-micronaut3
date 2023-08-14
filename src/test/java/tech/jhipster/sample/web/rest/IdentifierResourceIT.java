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
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.repository.IdentifierRepository;
import tech.jhipster.sample.service.dto.IdentifierDTO;
import tech.jhipster.sample.service.mapper.IdentifierMapper;

/**
 * Integration tests for the {@Link IdentifierResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IdentifierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private IdentifierMapper identifierMapper;

    @Inject
    private IdentifierRepository identifierRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Identifier identifier;

    @BeforeEach
    public void initTest() {
        identifier = createEntity(transactionManager, em);
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
    public static Identifier createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Identifier identifier = new Identifier().name(DEFAULT_NAME);
        return identifier;
    }

    /**
     * Delete all identifier entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Identifier.class);
    }

    @Test
    public void createIdentifier() throws Exception {
        int databaseSizeBeforeCreate = identifierRepository.findAll().size();

        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        // Create the Identifier
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.POST("/api/identifiers", identifierDTO), IdentifierDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeCreate + 1);
        Identifier testIdentifier = identifierList.get(identifierList.size() - 1);

        assertThat(testIdentifier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createIdentifierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = identifierRepository.findAll().size();

        // Create the Identifier with an existing ID
        identifier.setId(1L);
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.POST("/api/identifiers", identifierDTO), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = identifierRepository.findAll().size();
        // set the field null
        identifier.setName(null);

        // Create the Identifier, which fails.
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.POST("/api/identifiers", identifierDTO), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllIdentifiers() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);

        // Get the identifierList w/ all the identifiers
        List<IdentifierDTO> identifiers = client
            .retrieve(HttpRequest.GET("/api/identifiers?eagerload=true"), Argument.listOf(IdentifierDTO.class))
            .blockingFirst();
        IdentifierDTO testIdentifier = identifiers.get(0);

        assertThat(testIdentifier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getIdentifier() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);

        // Get the identifier
        IdentifierDTO testIdentifier = client
            .retrieve(HttpRequest.GET("/api/identifiers/" + identifier.getId()), IdentifierDTO.class)
            .blockingFirst();

        assertThat(testIdentifier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingIdentifier() throws Exception {
        // Get the identifier
        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.GET("/api/identifiers/" + Long.MAX_VALUE), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateIdentifier() throws Exception {
        // Initialize the database
        identifierRepository.saveAndFlush(identifier);

        int databaseSizeBeforeUpdate = identifierRepository.findAll().size();

        // Update the identifier
        Identifier updatedIdentifier = identifierRepository.findById(identifier.getId()).get();

        updatedIdentifier.name(UPDATED_NAME);
        IdentifierDTO updatedIdentifierDTO = identifierMapper.toDto(updatedIdentifier);

        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.PUT("/api/identifiers/" + identifier.getId(), updatedIdentifierDTO), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeUpdate);
        Identifier testIdentifier = identifierList.get(identifierList.size() - 1);

        assertThat(testIdentifier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingIdentifier() throws Exception {
        int databaseSizeBeforeUpdate = identifierRepository.findAll().size();

        // Create the Identifier
        IdentifierDTO identifierDTO = identifierMapper.toDto(identifier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.PUT("/api/identifiers/" + identifier.getId(), identifierDTO), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Identifier in the database
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteIdentifier() throws Exception {
        // Initialize the database with one entity
        identifierRepository.saveAndFlush(identifier);

        int databaseSizeBeforeDelete = identifierRepository.findAll().size();

        // Delete the identifier
        @SuppressWarnings("unchecked")
        HttpResponse<IdentifierDTO> response = client
            .exchange(HttpRequest.DELETE("/api/identifiers/" + identifier.getId()), IdentifierDTO.class)
            .onErrorReturn(t -> (HttpResponse<IdentifierDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Identifier> identifierList = identifierRepository.findAll();
        assertThat(identifierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Identifier.class);
        Identifier identifier1 = new Identifier();
        identifier1.setId(1L);
        Identifier identifier2 = new Identifier();
        identifier2.setId(identifier1.getId());
        assertThat(identifier1).isEqualTo(identifier2);
        identifier2.setId(2L);
        assertThat(identifier1).isNotEqualTo(identifier2);
        identifier1.setId(null);
        assertThat(identifier1).isNotEqualTo(identifier2);
    }
}
