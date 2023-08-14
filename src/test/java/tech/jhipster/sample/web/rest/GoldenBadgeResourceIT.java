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
import tech.jhipster.sample.domain.GoldenBadge;
import tech.jhipster.sample.domain.Identifier;
import tech.jhipster.sample.repository.GoldenBadgeRepository;
import tech.jhipster.sample.service.dto.GoldenBadgeDTO;
import tech.jhipster.sample.service.mapper.GoldenBadgeMapper;

/**
 * Integration tests for the {@Link GoldenBadgeResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoldenBadgeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private GoldenBadgeMapper goldenBadgeMapper;

    @Inject
    private GoldenBadgeRepository goldenBadgeRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private GoldenBadge goldenBadge;

    @BeforeEach
    public void initTest() {
        goldenBadge = createEntity(transactionManager, em);
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
    public static GoldenBadge createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        GoldenBadge goldenBadge = new GoldenBadge().name(DEFAULT_NAME);
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
        goldenBadge.setIden(identifier);
        return goldenBadge;
    }

    /**
     * Delete all goldenBadge entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, GoldenBadge.class);
        // Delete required entities
        IdentifierResourceIT.deleteAll(transactionManager, em);
    }

    @Test
    public void createGoldenBadge() throws Exception {
        int databaseSizeBeforeCreate = goldenBadgeRepository.findAll().size();

        GoldenBadgeDTO goldenBadgeDTO = goldenBadgeMapper.toDto(goldenBadge);

        // Create the GoldenBadge
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.POST("/api/golden-badges", goldenBadgeDTO), GoldenBadgeDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the GoldenBadge in the database
        List<GoldenBadge> goldenBadgeList = goldenBadgeRepository.findAll();
        assertThat(goldenBadgeList).hasSize(databaseSizeBeforeCreate + 1);
        GoldenBadge testGoldenBadge = goldenBadgeList.get(goldenBadgeList.size() - 1);

        assertThat(testGoldenBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createGoldenBadgeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goldenBadgeRepository.findAll().size();

        // Create the GoldenBadge with an existing ID
        goldenBadge.setId(1L);
        GoldenBadgeDTO goldenBadgeDTO = goldenBadgeMapper.toDto(goldenBadge);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.POST("/api/golden-badges", goldenBadgeDTO), GoldenBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<GoldenBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the GoldenBadge in the database
        List<GoldenBadge> goldenBadgeList = goldenBadgeRepository.findAll();
        assertThat(goldenBadgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllGoldenBadges() throws Exception {
        // Initialize the database
        goldenBadgeRepository.saveAndFlush(goldenBadge);

        // Get the goldenBadgeList w/ all the goldenBadges
        List<GoldenBadgeDTO> goldenBadges = client
            .retrieve(HttpRequest.GET("/api/golden-badges?eagerload=true"), Argument.listOf(GoldenBadgeDTO.class))
            .blockingFirst();
        GoldenBadgeDTO testGoldenBadge = goldenBadges.get(0);

        assertThat(testGoldenBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getGoldenBadge() throws Exception {
        // Initialize the database
        goldenBadgeRepository.saveAndFlush(goldenBadge);

        // Get the goldenBadge
        GoldenBadgeDTO testGoldenBadge = client
            .retrieve(HttpRequest.GET("/api/golden-badges/" + goldenBadge.getId()), GoldenBadgeDTO.class)
            .blockingFirst();

        assertThat(testGoldenBadge.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingGoldenBadge() throws Exception {
        // Get the goldenBadge
        @SuppressWarnings("unchecked")
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.GET("/api/golden-badges/" + Long.MAX_VALUE), GoldenBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<GoldenBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateGoldenBadge() throws Exception {
        // Initialize the database
        goldenBadgeRepository.saveAndFlush(goldenBadge);

        int databaseSizeBeforeUpdate = goldenBadgeRepository.findAll().size();

        // Update the goldenBadge
        GoldenBadge updatedGoldenBadge = goldenBadgeRepository.findById(goldenBadge.getId()).get();

        updatedGoldenBadge.name(UPDATED_NAME);
        GoldenBadgeDTO updatedGoldenBadgeDTO = goldenBadgeMapper.toDto(updatedGoldenBadge);

        @SuppressWarnings("unchecked")
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.PUT("/api/golden-badges/" + goldenBadge.getId(), updatedGoldenBadgeDTO), GoldenBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<GoldenBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the GoldenBadge in the database
        List<GoldenBadge> goldenBadgeList = goldenBadgeRepository.findAll();
        assertThat(goldenBadgeList).hasSize(databaseSizeBeforeUpdate);
        GoldenBadge testGoldenBadge = goldenBadgeList.get(goldenBadgeList.size() - 1);

        assertThat(testGoldenBadge.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingGoldenBadge() throws Exception {
        int databaseSizeBeforeUpdate = goldenBadgeRepository.findAll().size();

        // Create the GoldenBadge
        GoldenBadgeDTO goldenBadgeDTO = goldenBadgeMapper.toDto(goldenBadge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.PUT("/api/golden-badges/" + goldenBadge.getId(), goldenBadgeDTO), GoldenBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<GoldenBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the GoldenBadge in the database
        List<GoldenBadge> goldenBadgeList = goldenBadgeRepository.findAll();
        assertThat(goldenBadgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteGoldenBadge() throws Exception {
        // Initialize the database with one entity
        goldenBadgeRepository.saveAndFlush(goldenBadge);

        int databaseSizeBeforeDelete = goldenBadgeRepository.findAll().size();

        // Delete the goldenBadge
        @SuppressWarnings("unchecked")
        HttpResponse<GoldenBadgeDTO> response = client
            .exchange(HttpRequest.DELETE("/api/golden-badges/" + goldenBadge.getId()), GoldenBadgeDTO.class)
            .onErrorReturn(t -> (HttpResponse<GoldenBadgeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<GoldenBadge> goldenBadgeList = goldenBadgeRepository.findAll();
        assertThat(goldenBadgeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoldenBadge.class);
        GoldenBadge goldenBadge1 = new GoldenBadge();
        goldenBadge1.setId(1L);
        GoldenBadge goldenBadge2 = new GoldenBadge();
        goldenBadge2.setId(goldenBadge1.getId());
        assertThat(goldenBadge1).isEqualTo(goldenBadge2);
        goldenBadge2.setId(2L);
        assertThat(goldenBadge1).isNotEqualTo(goldenBadge2);
        goldenBadge1.setId(null);
        assertThat(goldenBadge1).isNotEqualTo(goldenBadge2);
    }
}
