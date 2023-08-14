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
import tech.jhipster.sample.domain.SilverBadge;
import tech.jhipster.sample.repository.SilverBadgeRepository;
import tech.jhipster.sample.service.dto.SilverBadgeDTO;
import tech.jhipster.sample.service.mapper.SilverBadgeMapper;

/**
 * Integration tests for the {@Link SilverBadgeResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SilverBadgeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private SilverBadgeMapper silverBadgeMapper;

    @Inject
    private SilverBadgeRepository silverBadgeRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private SilverBadge silverBadge;

    @BeforeEach
    public void initTest() {
        silverBadge = createEntity(transactionManager, em);
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
    public static SilverBadge createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        SilverBadge silverBadge = new SilverBadge().name(DEFAULT_NAME);
        // Add required entity
        Identifier identifier;
        if (TestUtil.findAll(transactionManager, em, Identifier.class).isEmpty()) {
            identifier = IdentifierResourceIT.createEntity(transactionManager, em);
            transactionManager.executeWrite(status -> {
                em.persist(identifier);
                em.flush();
                return identifier;
            });
        } else {
            identifier = TestUtil.findAll(transactionManager, em, Identifier.class).get(0);
        }
        silverBadge.setIden(identifier);
        return silverBadge;
    }

    /**
     * Delete all silverBadge entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, SilverBadge.class);
        // Delete required entities
        IdentifierResourceIT.deleteAll(transactionManager, em);
    }

    @Test
    public void createSilverBadge() throws Exception {
        int databaseSizeBeforeCreate = silverBadgeRepository.findAll().size();

        SilverBadgeDTO silverBadgeDTO = silverBadgeMapper.toDto(silverBadge);

        // Create the SilverBadge
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.POST("/api/silver-badges", silverBadgeDTO), SilverBadgeDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the SilverBadge in the database
        List<SilverBadge> silverBadgeList = silverBadgeRepository.findAll();
        assertThat(silverBadgeList).hasSize(databaseSizeBeforeCreate + 1);
        SilverBadge testSilverBadge = silverBadgeList.get(silverBadgeList.size() - 1);

        assertThat(testSilverBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createSilverBadgeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = silverBadgeRepository.findAll().size();

        // Create the SilverBadge with an existing ID
        silverBadge.setId(1L);
        SilverBadgeDTO silverBadgeDTO = silverBadgeMapper.toDto(silverBadge);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.POST("/api/silver-badges", silverBadgeDTO), SilverBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<SilverBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the SilverBadge in the database
        List<SilverBadge> silverBadgeList = silverBadgeRepository.findAll();
        assertThat(silverBadgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllSilverBadges() throws Exception {
        // Initialize the database
        silverBadgeRepository.saveAndFlush(silverBadge);

        // Get the silverBadgeList w/ all the silverBadges
        List<SilverBadgeDTO> silverBadges = client
            .retrieve(HttpRequest.GET("/api/silver-badges?eagerload=true"), Argument.listOf(SilverBadgeDTO.class))
            .blockingFirst();
        SilverBadgeDTO testSilverBadge = silverBadges.get(0);

        assertThat(testSilverBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getSilverBadge() throws Exception {
        // Initialize the database
        silverBadgeRepository.saveAndFlush(silverBadge);

        // Get the silverBadge
        SilverBadgeDTO testSilverBadge = client
            .retrieve(HttpRequest.GET("/api/silver-badges/" + silverBadge.getId()), SilverBadgeDTO.class)
            .blockingFirst();

        assertThat(testSilverBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingSilverBadge() throws Exception {
        // Get the silverBadge
        @SuppressWarnings("unchecked")
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.GET("/api/silver-badges/" + Long.MAX_VALUE), SilverBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<SilverBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateSilverBadge() throws Exception {
        // Initialize the database
        silverBadgeRepository.saveAndFlush(silverBadge);

        int databaseSizeBeforeUpdate = silverBadgeRepository.findAll().size();

        // Update the silverBadge
        SilverBadge updatedSilverBadge = silverBadgeRepository.findById(silverBadge.getId()).get();

        updatedSilverBadge.name(UPDATED_NAME);
        SilverBadgeDTO updatedSilverBadgeDTO = silverBadgeMapper.toDto(updatedSilverBadge);

        @SuppressWarnings("unchecked")
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.PUT("/api/silver-badges/" + silverBadge.getId(), updatedSilverBadgeDTO), SilverBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<SilverBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the SilverBadge in the database
        List<SilverBadge> silverBadgeList = silverBadgeRepository.findAll();
        assertThat(silverBadgeList).hasSize(databaseSizeBeforeUpdate);
        SilverBadge testSilverBadge = silverBadgeList.get(silverBadgeList.size() - 1);

        assertThat(testSilverBadge.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingSilverBadge() throws Exception {
        int databaseSizeBeforeUpdate = silverBadgeRepository.findAll().size();

        // Create the SilverBadge
        SilverBadgeDTO silverBadgeDTO = silverBadgeMapper.toDto(silverBadge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.PUT("/api/silver-badges/" + silverBadge.getId(), silverBadgeDTO), SilverBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<SilverBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the SilverBadge in the database
        List<SilverBadge> silverBadgeList = silverBadgeRepository.findAll();
        assertThat(silverBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSilverBadge() throws Exception {
        // Initialize the database with one entity
        silverBadgeRepository.saveAndFlush(silverBadge);

        int databaseSizeBeforeDelete = silverBadgeRepository.findAll().size();

        // Delete the silverBadge
        @SuppressWarnings("unchecked")
        HttpResponse<SilverBadgeDTO> response = client
            .exchange(HttpRequest.DELETE("/api/silver-badges/" + silverBadge.getId()), SilverBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<SilverBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<SilverBadge> silverBadgeList = silverBadgeRepository.findAll();
        assertThat(silverBadgeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SilverBadge.class);
        SilverBadge silverBadge1 = new SilverBadge();
        silverBadge1.setId(1L);
        SilverBadge silverBadge2 = new SilverBadge();
        silverBadge2.setId(silverBadge1.getId());
        assertThat(silverBadge1).isEqualTo(silverBadge2);
        silverBadge2.setId(2L);
        assertThat(silverBadge1).isNotEqualTo(silverBadge2);
        silverBadge1.setId(null);
        assertThat(silverBadge1).isNotEqualTo(silverBadge2);
    }
}
