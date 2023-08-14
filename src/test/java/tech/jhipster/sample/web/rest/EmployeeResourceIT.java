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
import tech.jhipster.sample.domain.Employee;
import tech.jhipster.sample.domain.GoldenBadge;
import tech.jhipster.sample.domain.SilverBadge;
import tech.jhipster.sample.repository.EmployeeRepository;
import tech.jhipster.sample.service.dto.EmployeeDTO;
import tech.jhipster.sample.service.mapper.EmployeeMapper;

/**
 * Integration tests for the {@Link EmployeeResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_HIRE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HIRE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_SALARY = 1L;
    private static final Long UPDATED_SALARY = 2L;

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Employee employee;

    @BeforeEach
    public void initTest() {
        employee = createEntity(transactionManager, em);
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
    public static Employee createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Employee employee = new Employee()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .hireDate(DEFAULT_HIRE_DATE)
            .salary(DEFAULT_SALARY)
            .commissionPct(DEFAULT_COMMISSION_PCT);
        // Add required entity
        SilverBadge silverBadge;
        if (TestUtil.findAll(transactionManager, em, SilverBadge.class).isEmpty()) {
            silverBadge = SilverBadgeResourceIT.createEntity(transactionManager, em);
            transactionManager.executeWrite(status -> {
                em.persist(silverBadge);
                em.flush();
                return silverBadge;
            });
        } else {
            silverBadge = TestUtil.findAll(transactionManager, em, SilverBadge.class).get(0);
        }
        employee.setSibag(silverBadge);
        // Add required entity
        GoldenBadge goldenBadge;
        if (TestUtil.findAll(transactionManager, em, GoldenBadge.class).isEmpty()) {
            goldenBadge = GoldenBadgeResourceIT.createEntity(transactionManager, em);
            transactionManager.executeWrite(status -> {
                em.persist(goldenBadge);
                em.flush();
                return goldenBadge;
            });
        } else {
            goldenBadge = TestUtil.findAll(transactionManager, em, GoldenBadge.class).get(0);
        }
        employee.setGobag(goldenBadge);
        return employee;
    }

    /**
     * Delete all employee entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Employee.class);
        // Delete required entities
        SilverBadgeResourceIT.deleteAll(transactionManager, em);
        // Delete required entities
        GoldenBadgeResourceIT.deleteAll(transactionManager, em);
    }

    @Test
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // Create the Employee
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.POST("/api/employees", employeeDTO), EmployeeDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);

        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.POST("/api/employees", employeeDTO), EmployeeDTO.class)
            .onErrorReturn(t -> (HttpResponse<EmployeeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employeeList w/ all the employees
        List<EmployeeDTO> employees = client
            .retrieve(HttpRequest.GET("/api/employees?eagerload=true"), Argument.listOf(EmployeeDTO.class))
            .blockingFirst();
        EmployeeDTO testEmployee = employees.get(0);

        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        EmployeeDTO testEmployee = client
            .retrieve(HttpRequest.GET("/api/employees/" + employee.getId()), EmployeeDTO.class)
            .blockingFirst();

        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        @SuppressWarnings("unchecked")
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.GET("/api/employees/" + Long.MAX_VALUE), EmployeeDTO.class)
            .onErrorReturn(t -> (HttpResponse<EmployeeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();

        updatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT);
        EmployeeDTO updatedEmployeeDTO = employeeMapper.toDto(updatedEmployee);

        @SuppressWarnings("unchecked")
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.PUT("/api/employees/" + employee.getId(), updatedEmployeeDTO), EmployeeDTO.class)
            .onErrorReturn(t -> (HttpResponse<EmployeeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);

        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testEmployee.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
    }

    @Test
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.PUT("/api/employees/" + employee.getId(), employeeDTO), EmployeeDTO.class)
            .onErrorReturn(t -> (HttpResponse<EmployeeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployee() throws Exception {
        // Initialize the database with one entity
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        @SuppressWarnings("unchecked")
        HttpResponse<EmployeeDTO> response = client
            .exchange(HttpRequest.DELETE("/api/employees/" + employee.getId()), EmployeeDTO.class)
            .onErrorReturn(t -> (HttpResponse<EmployeeDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);
        employee2.setId(2L);
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.setId(null);
        assertThat(employee1).isNotEqualTo(employee2);
    }
}
