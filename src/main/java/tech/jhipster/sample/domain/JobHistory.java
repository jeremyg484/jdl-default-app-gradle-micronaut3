package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.jhipster.sample.domain.enumeration.Language;

/**
 * JobHistory comment.
 */
@Schema(description = "JobHistory comment.")
@Entity
@Table(name = "job_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JobHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @ManyToMany
    @JoinTable(
        name = "rel_job_history__department",
        joinColumns = @JoinColumn(name = "job_history_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "employees", "histories" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_job_history__job",
        joinColumns = @JoinColumn(name = "job_history_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chores", "emp", "histories" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_job_history__emp",
        joinColumns = @JoinColumn(name = "job_history_id"),
        inverseJoinColumns = @JoinColumn(name = "emp_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "jobs", "manager", "sibag", "gobag", "department", "histories" }, allowSetters = true)
    private Set<Employee> emps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JobHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public JobHistory startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public JobHistory endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Language getLanguage() {
        return this.language;
    }

    public JobHistory language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public JobHistory departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public JobHistory addDepartment(Department department) {
        this.departments.add(department);
        department.getHistories().add(this);
        return this;
    }

    public JobHistory removeDepartment(Department department) {
        this.departments.remove(department);
        department.getHistories().remove(this);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public JobHistory jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public JobHistory addJob(Job job) {
        this.jobs.add(job);
        job.getHistories().add(this);
        return this;
    }

    public JobHistory removeJob(Job job) {
        this.jobs.remove(job);
        job.getHistories().remove(this);
        return this;
    }

    public Set<Employee> getEmps() {
        return this.emps;
    }

    public void setEmps(Set<Employee> employees) {
        this.emps = employees;
    }

    public JobHistory emps(Set<Employee> employees) {
        this.setEmps(employees);
        return this;
    }

    public JobHistory addEmp(Employee employee) {
        this.emps.add(employee);
        employee.getHistories().add(this);
        return this;
    }

    public JobHistory removeEmp(Employee employee) {
        this.emps.remove(employee);
        employee.getHistories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobHistory)) {
            return false;
        }
        return id != null && id.equals(((JobHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobHistory{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
