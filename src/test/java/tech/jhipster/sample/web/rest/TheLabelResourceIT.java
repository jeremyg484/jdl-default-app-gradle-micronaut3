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
import tech.jhipster.sample.domain.TheLabel;
import tech.jhipster.sample.repository.TheLabelRepository;

/**
 * Integration tests for the {@Link TheLabelResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TheLabelResourceIT {

    private static final String DEFAULT_LABEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_NAME = "BBBBBBBBBB";

    @Inject
    private TheLabelRepository theLabelRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private TheLabel theLabel;

    @BeforeEach
    public void initTest() {
        theLabel = createEntity(transactionManager, em);
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
    public static TheLabel createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TheLabel theLabel = new TheLabel().labelName(DEFAULT_LABEL_NAME);
        return theLabel;
    }

    /**
     * Delete all theLabel entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, TheLabel.class);
    }

    @Test
    public void createTheLabel() throws Exception {
        int databaseSizeBeforeCreate = theLabelRepository.findAll().size();

        // Create the TheLabel
        HttpResponse<TheLabel> response = client.exchange(HttpRequest.POST("/api/the-labels", theLabel), TheLabel.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the TheLabel in the database
        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeCreate + 1);
        TheLabel testTheLabel = theLabelList.get(theLabelList.size() - 1);

        assertThat(testTheLabel.getLabelName()).isEqualTo(DEFAULT_LABEL_NAME);
    }

    @Test
    public void createTheLabelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = theLabelRepository.findAll().size();

        // Create the TheLabel with an existing ID
        theLabel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.POST("/api/the-labels", theLabel), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the TheLabel in the database
        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkLabelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = theLabelRepository.findAll().size();
        // set the field null
        theLabel.setLabelName(null);

        // Create the TheLabel, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.POST("/api/the-labels", theLabel), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTheLabels() throws Exception {
        // Initialize the database
        theLabelRepository.saveAndFlush(theLabel);

        // Get the theLabelList w/ all the theLabels
        List<TheLabel> theLabels = client
            .retrieve(HttpRequest.GET("/api/the-labels?eagerload=true"), Argument.listOf(TheLabel.class))
            .blockingFirst();
        TheLabel testTheLabel = theLabels.get(0);

        assertThat(testTheLabel.getLabelName()).isEqualTo(DEFAULT_LABEL_NAME);
    }

    @Test
    public void getTheLabel() throws Exception {
        // Initialize the database
        theLabelRepository.saveAndFlush(theLabel);

        // Get the theLabel
        TheLabel testTheLabel = client.retrieve(HttpRequest.GET("/api/the-labels/" + theLabel.getId()), TheLabel.class).blockingFirst();

        assertThat(testTheLabel.getLabelName()).isEqualTo(DEFAULT_LABEL_NAME);
    }

    @Test
    public void getNonExistingTheLabel() throws Exception {
        // Get the theLabel
        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.GET("/api/the-labels/" + Long.MAX_VALUE), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateTheLabel() throws Exception {
        // Initialize the database
        theLabelRepository.saveAndFlush(theLabel);

        int databaseSizeBeforeUpdate = theLabelRepository.findAll().size();

        // Update the theLabel
        TheLabel updatedTheLabel = theLabelRepository.findById(theLabel.getId()).get();

        updatedTheLabel.labelName(UPDATED_LABEL_NAME);

        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.PUT("/api/the-labels/" + theLabel.getId(), updatedTheLabel), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the TheLabel in the database
        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeUpdate);
        TheLabel testTheLabel = theLabelList.get(theLabelList.size() - 1);

        assertThat(testTheLabel.getLabelName()).isEqualTo(UPDATED_LABEL_NAME);
    }

    @Test
    public void updateNonExistingTheLabel() throws Exception {
        int databaseSizeBeforeUpdate = theLabelRepository.findAll().size();

        // Create the TheLabel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.PUT("/api/the-labels/" + theLabel.getId(), theLabel), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the TheLabel in the database
        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTheLabel() throws Exception {
        // Initialize the database with one entity
        theLabelRepository.saveAndFlush(theLabel);

        int databaseSizeBeforeDelete = theLabelRepository.findAll().size();

        // Delete the theLabel
        @SuppressWarnings("unchecked")
        HttpResponse<TheLabel> response = client
            .exchange(HttpRequest.DELETE("/api/the-labels/" + theLabel.getId()), TheLabel.class)
            .onErrorReturn(t -> (HttpResponse<TheLabel>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<TheLabel> theLabelList = theLabelRepository.findAll();
        assertThat(theLabelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TheLabel.class);
        TheLabel theLabel1 = new TheLabel();
        theLabel1.setId(1L);
        TheLabel theLabel2 = new TheLabel();
        theLabel2.setId(theLabel1.getId());
        assertThat(theLabel1).isEqualTo(theLabel2);
        theLabel2.setId(2L);
        assertThat(theLabel1).isNotEqualTo(theLabel2);
        theLabel1.setId(null);
        assertThat(theLabel1).isNotEqualTo(theLabel2);
    }
}
