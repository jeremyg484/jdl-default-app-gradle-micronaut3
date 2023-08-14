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
import tech.jhipster.sample.domain.Department;
import tech.jhipster.sample.repository.DepartmentRepository;
import tech.jhipster.sample.service.dto.DepartmentDTO;
import tech.jhipster.sample.service.mapper.DepartmentMapper;

/**
 * Integration tests for the {@Link DepartmentResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepartmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ADVERTISEMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ADVERTISEMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ADVERTISEMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ADVERTISEMENT_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Inject
    private DepartmentMapper departmentMapper;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Department department;

    @BeforeEach
    public void initTest() {
        department = createEntity(transactionManager, em);
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
    public static Department createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Department department = new Department()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .advertisement(DEFAULT_ADVERTISEMENT)
            .advertisementContentType(DEFAULT_ADVERTISEMENT_CONTENT_TYPE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return department;
    }

    /**
     * Delete all department entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Department.class);
    }

    @Test
    public void createDepartment() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // Create the Department
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.POST("/api/departments", departmentDTO), DepartmentDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate + 1);
        Department testDepartment = departmentList.get(departmentList.size() - 1);

        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartment.getAdvertisement()).isEqualTo(DEFAULT_ADVERTISEMENT);
        assertThat(testDepartment.getAdvertisementContentType()).isEqualTo(DEFAULT_ADVERTISEMENT_CONTENT_TYPE);
        assertThat(testDepartment.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartment.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    public void createDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.POST("/api/departments", departmentDTO), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRepository.findAll().size();
        // set the field null
        department.setName(null);

        // Create the Department, which fails.
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.POST("/api/departments", departmentDTO), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllDepartments() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get the departmentList w/ all the departments
        List<DepartmentDTO> departments = client
            .retrieve(HttpRequest.GET("/api/departments?eagerload=true"), Argument.listOf(DepartmentDTO.class))
            .blockingFirst();
        DepartmentDTO testDepartment = departments.get(0);

        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartment.getAdvertisement()).isEqualTo(DEFAULT_ADVERTISEMENT);
        assertThat(testDepartment.getAdvertisementContentType()).isEqualTo(DEFAULT_ADVERTISEMENT_CONTENT_TYPE);
        assertThat(testDepartment.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartment.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    public void getDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get the department
        DepartmentDTO testDepartment = client
            .retrieve(HttpRequest.GET("/api/departments/" + department.getId()), DepartmentDTO.class)
            .blockingFirst();

        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartment.getAdvertisement()).isEqualTo(DEFAULT_ADVERTISEMENT);
        assertThat(testDepartment.getAdvertisementContentType()).isEqualTo(DEFAULT_ADVERTISEMENT_CONTENT_TYPE);
        assertThat(testDepartment.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartment.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    public void getNonExistingDepartment() throws Exception {
        // Get the department
        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.GET("/api/departments/" + Long.MAX_VALUE), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).get();

        updatedDepartment
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .advertisement(UPDATED_ADVERTISEMENT)
            .advertisementContentType(UPDATED_ADVERTISEMENT_CONTENT_TYPE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        DepartmentDTO updatedDepartmentDTO = departmentMapper.toDto(updatedDepartment);

        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.PUT("/api/departments/" + department.getId(), updatedDepartmentDTO), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);

        assertThat(testDepartment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartment.getAdvertisement()).isEqualTo(UPDATED_ADVERTISEMENT);
        assertThat(testDepartment.getAdvertisementContentType()).isEqualTo(UPDATED_ADVERTISEMENT_CONTENT_TYPE);
        assertThat(testDepartment.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDepartment.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    public void updateNonExistingDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.PUT("/api/departments/" + department.getId(), departmentDTO), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDepartment() throws Exception {
        // Initialize the database with one entity
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeDelete = departmentRepository.findAll().size();

        // Delete the department
        @SuppressWarnings("unchecked")
        HttpResponse<DepartmentDTO> response = client
            .exchange(HttpRequest.DELETE("/api/departments/" + department.getId()), DepartmentDTO.class)
            .onErrorReturn(t -> (HttpResponse<DepartmentDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = new Department();
        department1.setId(1L);
        Department department2 = new Department();
        department2.setId(department1.getId());
        assertThat(department1).isEqualTo(department2);
        department2.setId(2L);
        assertThat(department1).isNotEqualTo(department2);
        department1.setId(null);
        assertThat(department1).isNotEqualTo(department2);
    }
}
