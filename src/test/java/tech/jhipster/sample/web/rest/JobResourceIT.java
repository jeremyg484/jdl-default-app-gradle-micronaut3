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
import tech.jhipster.sample.domain.Job;
import tech.jhipster.sample.domain.enumeration.JobType;
import tech.jhipster.sample.repository.JobRepository;

/**
 * Integration tests for the {@Link JobResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final JobType DEFAULT_TYPE = JobType.BOSS;
    private static final JobType UPDATED_TYPE = JobType.SLAVE;

    private static final Long DEFAULT_MIN_SALARY = 1L;
    private static final Long UPDATED_MIN_SALARY = 2L;

    private static final Long DEFAULT_MAX_SALARY = 1L;
    private static final Long UPDATED_MAX_SALARY = 2L;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Job job;

    @BeforeEach
    public void initTest() {
        job = createEntity(transactionManager, em);
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
    public static Job createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Job job = new Job().title(DEFAULT_TITLE).type(DEFAULT_TYPE).minSalary(DEFAULT_MIN_SALARY).maxSalary(DEFAULT_MAX_SALARY);
        return job;
    }

    /**
     * Delete all job entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Job.class);
    }

    @Test
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        HttpResponse<Job> response = client.exchange(HttpRequest.POST("/api/jobs", job), Job.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);

        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testJob.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
    }

    @Test
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Job> response = client
            .exchange(HttpRequest.POST("/api/jobs", job), Job.class)
            .onErrorReturn(t -> (HttpResponse<Job>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the jobList w/ all the jobs
        List<Job> jobs = client.retrieve(HttpRequest.GET("/api/jobs?eagerload=true"), Argument.listOf(Job.class)).blockingFirst();
        Job testJob = jobs.get(0);

        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testJob.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
    }

    @Test
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        Job testJob = client.retrieve(HttpRequest.GET("/api/jobs/" + job.getId()), Job.class).blockingFirst();

        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testJob.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
    }

    @Test
    public void getNonExistingJob() throws Exception {
        // Get the job
        @SuppressWarnings("unchecked")
        HttpResponse<Job> response = client
            .exchange(HttpRequest.GET("/api/jobs/" + Long.MAX_VALUE), Job.class)
            .onErrorReturn(t -> (HttpResponse<Job>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();

        updatedJob.title(UPDATED_TITLE).type(UPDATED_TYPE).minSalary(UPDATED_MIN_SALARY).maxSalary(UPDATED_MAX_SALARY);

        @SuppressWarnings("unchecked")
        HttpResponse<Job> response = client
            .exchange(HttpRequest.PUT("/api/jobs/" + job.getId(), updatedJob), Job.class)
            .onErrorReturn(t -> (HttpResponse<Job>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);

        assertThat(testJob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJob.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testJob.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
    }

    @Test
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Job> response = client
            .exchange(HttpRequest.PUT("/api/jobs/" + job.getId(), job), Job.class)
            .onErrorReturn(t -> (HttpResponse<Job>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteJob() throws Exception {
        // Initialize the database with one entity
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Delete the job
        @SuppressWarnings("unchecked")
        HttpResponse<Job> response = client
            .exchange(HttpRequest.DELETE("/api/jobs/" + job.getId()), Job.class)
            .onErrorReturn(t -> (HttpResponse<Job>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
