package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.\nSecond line in javadoc.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private ZonedDateTime hireDate;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "commission_pct")
    private Long commissionPct;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "emp")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chores", "emp", "histories" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "jobs", "manager", "sibag", "gobag", "department", "histories" }, allowSetters = true)
    private Employee manager;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "iden" }, allowSetters = true)
    private SilverBadge sibag;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "iden" }, allowSetters = true)
    private GoldenBadge gobag;

    /**
     * Another side of the same relationship,
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "employees", "histories" }, allowSetters = true)
    private Department department;

    @ManyToMany(mappedBy = "emps")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "jobs", "emps" }, allowSetters = true)
    private Set<JobHistory> histories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getHireDate() {
        return this.hireDate;
    }

    public Employee hireDate(ZonedDateTime hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(ZonedDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public Long getSalary() {
        return this.salary;
    }

    public Employee salary(Long salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return this.commissionPct;
    }

    public Employee commissionPct(Long commissionPct) {
        this.setCommissionPct(commissionPct);
        return this;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.setEmp(null));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.setEmp(this));
        }
        this.jobs = jobs;
    }

    public Employee jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Employee addJob(Job job) {
        this.jobs.add(job);
        job.setEmp(this);
        return this;
    }

    public Employee removeJob(Job job) {
        this.jobs.remove(job);
        job.setEmp(null);
        return this;
    }

    public Employee getManager() {
        return this.manager;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public Employee manager(Employee employee) {
        this.setManager(employee);
        return this;
    }

    public SilverBadge getSibag() {
        return this.sibag;
    }

    public void setSibag(SilverBadge silverBadge) {
        this.sibag = silverBadge;
    }

    public Employee sibag(SilverBadge silverBadge) {
        this.setSibag(silverBadge);
        return this;
    }

    public GoldenBadge getGobag() {
        return this.gobag;
    }

    public void setGobag(GoldenBadge goldenBadge) {
        this.gobag = goldenBadge;
    }

    public Employee gobag(GoldenBadge goldenBadge) {
        this.setGobag(goldenBadge);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Set<JobHistory> getHistories() {
        return this.histories;
    }

    public void setHistories(Set<JobHistory> jobHistories) {
        if (this.histories != null) {
            this.histories.forEach(i -> i.removeEmp(this));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.addEmp(this));
        }
        this.histories = jobHistories;
    }

    public Employee histories(Set<JobHistory> jobHistories) {
        this.setHistories(jobHistories);
        return this;
    }

    public Employee addHistory(JobHistory jobHistory) {
        this.histories.add(jobHistory);
        jobHistory.getEmps().add(this);
        return this;
    }

    public Employee removeHistory(JobHistory jobHistory) {
        this.histories.remove(jobHistory);
        jobHistory.getEmps().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", commissionPct=" + getCommissionPct() +
            "}";
    }
}
