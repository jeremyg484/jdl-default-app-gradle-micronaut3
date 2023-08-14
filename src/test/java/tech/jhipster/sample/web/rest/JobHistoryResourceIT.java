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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.jhipster.sample.domain.JobHistory;
import tech.jhipster.sample.domain.enumeration.Language;
import tech.jhipster.sample.repository.JobHistoryRepository;

/**
 * Integration tests for the {@Link JobHistoryResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobHistoryResourceIT {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    @Inject
    private JobHistoryRepository jobHistoryRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private JobHistory jobHistory;

    @BeforeEach
    public void initTest() {
        jobHistory = createEntity(transactionManager, em);
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
    public static JobHistory createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        JobHistory jobHistory = new JobHistory().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).language(DEFAULT_LANGUAGE);
        return jobHistory;
    }

    /**
     * Delete all jobHistory entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, JobHistory.class);
    }

    @Test
    public void createJobHistory() throws Exception {
        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();

        // Create the JobHistory
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.POST("/api/job-histories", jobHistory), JobHistory.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);

        assertThat(testJobHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJobHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJobHistory.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    public void createJobHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();

        // Create the JobHistory with an existing ID
        jobHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.POST("/api/job-histories", jobHistory), JobHistory.class)
            .onErrorReturn(t -> (HttpResponse<JobHistory>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllJobHistories() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get the jobHistoryList w/ all the jobHistories
        List<JobHistory> jobHistories = client
            .retrieve(HttpRequest.GET("/api/job-histories?eagerload=true"), Argument.listOf(JobHistory.class))
            .blockingFirst();
        JobHistory testJobHistory = jobHistories.get(0);

        assertThat(testJobHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJobHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJobHistory.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    public void getJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get the jobHistory
        JobHistory testJobHistory = client
            .retrieve(HttpRequest.GET("/api/job-histories/" + jobHistory.getId()), JobHistory.class)
            .blockingFirst();

        assertThat(testJobHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJobHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJobHistory.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    public void getNonExistingJobHistory() throws Exception {
        // Get the jobHistory
        @SuppressWarnings("unchecked")
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.GET("/api/job-histories/" + Long.MAX_VALUE), JobHistory.class)
            .onErrorReturn(t -> (HttpResponse<JobHistory>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Update the jobHistory
        JobHistory updatedJobHistory = jobHistoryRepository.findById(jobHistory.getId()).get();

        updatedJobHistory.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).language(UPDATED_LANGUAGE);

        @SuppressWarnings("unchecked")
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.PUT("/api/job-histories/" + jobHistory.getId(), updatedJobHistory), JobHistory.class)
            .onErrorReturn(t -> (HttpResponse<JobHistory>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);

        assertThat(testJobHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJobHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testJobHistory.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    public void updateNonExistingJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Create the JobHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.PUT("/api/job-histories/" + jobHistory.getId(), jobHistory), JobHistory.class)
            .onErrorReturn(t -> (HttpResponse<JobHistory>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteJobHistory() throws Exception {
        // Initialize the database with one entity
        jobHistoryRepository.saveAndFlush(jobHistory);

        int databaseSizeBeforeDelete = jobHistoryRepository.findAll().size();

        // Delete the jobHistory
        @SuppressWarnings("unchecked")
        HttpResponse<JobHistory> response = client
            .exchange(HttpRequest.DELETE("/api/job-histories/" + jobHistory.getId()), JobHistory.class)
            .onErrorReturn(t -> (HttpResponse<JobHistory>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobHistory.class);
        JobHistory jobHistory1 = new JobHistory();
        jobHistory1.setId(1L);
        JobHistory jobHistory2 = new JobHistory();
        jobHistory2.setId(jobHistory1.getId());
        assertThat(jobHistory1).isEqualTo(jobHistory2);
        jobHistory2.setId(2L);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
        jobHistory1.setId(null);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
    }
}
