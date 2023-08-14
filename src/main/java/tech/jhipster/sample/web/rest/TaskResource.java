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
import tech.jhipster.sample.domain.Task;
import tech.jhipster.sample.repository.TaskRepository;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Task}.
 */
@Controller("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskRepository taskRepository;

    public TaskResource(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param task the task to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new task, or with status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/tasks")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Task> createTask(@Body Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getId() != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Task result = taskRepository.save(task);
        URI location = new URI("/api/tasks/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing task.
     *
     * @param task the task to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/tasks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Task> updateTask(@Body Task task, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, task.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Task result = taskRepository.update(task);
        return HttpResponse
            .ok(result)
            .headers(headers -> HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, task.getId().toString()));
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @Get("/tasks")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Task> getAllTasks(HttpRequest request) {
        log.debug("REST request to get all Tasks");
        return taskRepository.findAll();
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the task to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the task, or with status {@code 404 (Not Found)}.
     */
    @Get("/tasks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Task> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);

        return taskRepository.findById(id);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the task to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/tasks/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskRepository.deleteById(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
