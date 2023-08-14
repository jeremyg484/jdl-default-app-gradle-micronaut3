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
import tech.jhipster.sample.domain.JobHistory;
import tech.jhipster.sample.repository.JobHistoryRepository;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.util.PaginationUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.JobHistory}.
 */
@Controller("/api")
public class JobHistoryResource {

    private final Logger log = LoggerFactory.getLogger(JobHistoryResource.class);

    private static final String ENTITY_NAME = "jobHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobHistoryRepository jobHistoryRepository;

    public JobHistoryResource(JobHistoryRepository jobHistoryRepository) {
        this.jobHistoryRepository = jobHistoryRepository;
    }

    /**
     * {@code POST  /job-histories} : Create a new jobHistory.
     *
     * @param jobHistory the jobHistory to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new jobHistory, or with status {@code 400 (Bad Request)} if the jobHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/job-histories")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<JobHistory> createJobHistory(@Body JobHistory jobHistory) throws URISyntaxException {
        log.debug("REST request to save JobHistory : {}", jobHistory);
        if (jobHistory.getId() != null) {
            throw new BadRequestAlertException("A new jobHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobHistory result = jobHistoryRepository.save(jobHistory);
        URI location = new URI("/api/job-histories/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /job-histories/:id} : Updates an existing jobHistory.
     *
     * @param jobHistory the jobHistory to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated jobHistory,
     * or with status {@code 400 (Bad Request)} if the jobHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/job-histories/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<JobHistory> updateJobHistory(@Body JobHistory jobHistory, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update JobHistory : {}", jobHistory);
        if (jobHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        JobHistory result = jobHistoryRepository.update(jobHistory);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, jobHistory.getId().toString())
            );
    }

    /**
     * {@code GET  /job-histories} : get all the jobHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of jobHistories in body.
     */
    @Get("/job-histories{?eagerload}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<JobHistory>> getAllJobHistories(HttpRequest request, Pageable pageable, @Nullable Boolean eagerload) {
        log.debug("REST request to get a page of JobHistories");
        Page<JobHistory> page;
        if (eagerload != null && eagerload) {
            page = jobHistoryRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = jobHistoryRepository.findAll(pageable);
        }
        return HttpResponse
            .ok(page.getContent())
            .headers(headers -> PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /job-histories/:id} : get the "id" jobHistory.
     *
     * @param id the id of the jobHistory to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the jobHistory, or with status {@code 404 (Not Found)}.
     */
    @Get("/job-histories/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<JobHistory> getJobHistory(@PathVariable Long id) {
        log.debug("REST request to get JobHistory : {}", id);

        return jobHistoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * {@code DELETE  /job-histories/:id} : delete the "id" jobHistory.
     *
     * @param id the id of the jobHistory to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/job-histories/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteJobHistory(@PathVariable Long id) {
        log.debug("REST request to delete JobHistory : {}", id);
        jobHistoryRepository.deleteById(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
