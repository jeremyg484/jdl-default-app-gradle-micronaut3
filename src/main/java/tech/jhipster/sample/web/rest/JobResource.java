package tech.jhipster.sample.web.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.ReadOnly;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.Job;
import tech.jhipster.sample.repository.JobRepository;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.util.PaginationUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Job}.
 */
@Controller("/api")
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRepository jobRepository;

    public JobResource(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * {@code POST  /jobs} : Create a new job.
     *
     * @param job the job to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new job, or with status {@code 400 (Bad Request)} if the job has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/jobs")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Job> createJob(@Body Job job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (job.getId() != null) {
            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Job result = jobRepository.save(job);
        URI location = new URI("/api/jobs/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /jobs/:id} : Updates an existing job.
     *
     * @param job the job to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated job,
     * or with status {@code 400 (Bad Request)} if the job is not valid,
     * or with status {@code 500 (Internal Server Error)} if the job couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/jobs/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Job> updateJob(@Body Job job, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Job : {}", job);
        if (job.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, job.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Job result = jobRepository.update(job);
        return HttpResponse
            .ok(result)
            .headers(headers -> HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, job.getId().toString()));
    }

    /**
     * {@code GET  /jobs} : get all the jobs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @Get("/jobs{?eagerload}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<Job>> getAllJobs(HttpRequest request, Pageable pageable, @Nullable Boolean eagerload) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page;
        if (eagerload != null && eagerload) {
            page = jobRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = jobRepository.findAll(pageable);
        }
        return HttpResponse
            .ok(page.getContent())
            .headers(headers -> PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /jobs/:id} : get the "id" job.
     *
     * @param id the id of the job to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the job, or with status {@code 404 (Not Found)}.
     */
    @Get("/jobs/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Job> getJob(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);

        return jobRepository.findOneWithEagerRelationships(id);
    }

    /**
     * {@code DELETE  /jobs/:id} : delete the "id" job.
     *
     * @param id the id of the job to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/jobs/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteJob(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobRepository.deleteById(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
