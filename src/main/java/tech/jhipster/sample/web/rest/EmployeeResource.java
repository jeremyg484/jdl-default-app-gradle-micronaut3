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
import tech.jhipster.sample.service.EmployeeService;
import tech.jhipster.sample.service.dto.EmployeeDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.util.PaginationUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Employee}.
 */
@Controller("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeService employeeService;

    public EmployeeResource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new employeeDTO, or with status {@code 400 (Bad Request)} if the employee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/employees")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EmployeeDTO> createEmployee(@Body EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        URI location = new URI("/api/employees/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /employees/:id} : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated employeeDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/employees/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EmployeeDTO> updateEmployee(@Body EmployeeDTO employeeDTO, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        EmployeeDTO result = employeeService.update(employeeDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, employeeDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of employees in body.
     */
    @Get("/employees")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<EmployeeDTO>> getAllEmployees(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of Employees");
        Page<EmployeeDTO> page = employeeService.findAll(pageable);
        return HttpResponse
            .ok(page.getContent())
            .headers(headers -> PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the employeeDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/employees/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<EmployeeDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);

        return employeeService.findOne(id);
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/employees/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
