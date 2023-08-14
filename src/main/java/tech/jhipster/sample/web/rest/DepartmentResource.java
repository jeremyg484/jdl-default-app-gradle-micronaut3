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
import tech.jhipster.sample.service.DepartmentService;
import tech.jhipster.sample.service.dto.DepartmentDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Department}.
 */
@Controller("/api")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "department";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * {@code POST  /departments} : Create a new department.
     *
     * @param departmentDTO the departmentDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new departmentDTO, or with status {@code 400 (Bad Request)} if the department has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/departments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<DepartmentDTO> createDepartment(@Body DepartmentDTO departmentDTO) throws URISyntaxException {
        log.debug("REST request to save Department : {}", departmentDTO);
        if (departmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new department cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentDTO result = departmentService.save(departmentDTO);
        URI location = new URI("/api/departments/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /departments/:id} : Updates an existing department.
     *
     * @param departmentDTO the departmentDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/departments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<DepartmentDTO> updateDepartment(@Body DepartmentDTO departmentDTO, @PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to update Department : {}", departmentDTO);
        if (departmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        DepartmentDTO result = departmentService.update(departmentDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, departmentDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of departments in body.
     */
    @Get("/departments")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<DepartmentDTO> getAllDepartments(HttpRequest request) {
        log.debug("REST request to get all Departments");
        return departmentService.findAll();
    }

    /**
     * {@code GET  /departments/:id} : get the "id" department.
     *
     * @param id the id of the departmentDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the departmentDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/departments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<DepartmentDTO> getDepartment(@PathVariable Long id) {
        log.debug("REST request to get Department : {}", id);

        return departmentService.findOne(id);
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" department.
     *
     * @param id the id of the departmentDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/departments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Department : {}", id);
        departmentService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
