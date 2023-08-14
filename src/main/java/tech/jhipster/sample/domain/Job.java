package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.jhipster.sample.domain.enumeration.JobType;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 5, max = 25)
    @Column(name = "title", length = 25)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private JobType type;

    @Column(name = "min_salary")
    private Long minSalary;

    @Column(name = "max_salary")
    private Long maxSalary;

    @ManyToMany
    @JoinTable(name = "rel_job__chore", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "chore_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "linkedJobs" }, allowSetters = true)
    private Set<Task> chores = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "jobs", "manager", "sibag", "gobag", "department", "histories" }, allowSetters = true)
    private Employee emp;

    @ManyToMany(mappedBy = "jobs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "jobs", "emps" }, allowSetters = true)
    private Set<JobHistory> histories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Job id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Job title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JobType getType() {
        return this.type;
    }

    public Job type(JobType type) {
        this.setType(type);
        return this;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public Long getMinSalary() {
        return this.minSalary;
    }

    public Job minSalary(Long minSalary) {
        this.setMinSalary(minSalary);
        return this;
    }

    public void setMinSalary(Long minSalary) {
        this.minSalary = minSalary;
    }

    public Long getMaxSalary() {
        return this.maxSalary;
    }

    public Job maxSalary(Long maxSalary) {
        this.setMaxSalary(maxSalary);
        return this;
    }

    public void setMaxSalary(Long maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Set<Task> getChores() {
        return this.chores;
    }

    public void setChores(Set<Task> tasks) {
        this.chores = tasks;
    }

    public Job chores(Set<Task> tasks) {
        this.setChores(tasks);
        return this;
    }

    public Job addChore(Task task) {
        this.chores.add(task);
        task.getLinkedJobs().add(this);
        return this;
    }

    public Job removeChore(Task task) {
        this.chores.remove(task);
        task.getLinkedJobs().remove(this);
        return this;
    }

    public Employee getEmp() {
        return this.emp;
    }

    public void setEmp(Employee employee) {
        this.emp = employee;
    }

    public Job emp(Employee employee) {
        this.setEmp(employee);
        return this;
    }

    public Set<JobHistory> getHistories() {
        return this.histories;
    }

    public void setHistories(Set<JobHistory> jobHistories) {
        if (this.histories != null) {
            this.histories.forEach(i -> i.removeJob(this));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.addJob(this));
        }
        this.histories = jobHistories;
    }

    public Job histories(Set<JobHistory> jobHistories) {
        this.setHistories(jobHistories);
        return this;
    }

    public Job addHistory(JobHistory jobHistory) {
        this.histories.add(jobHistory);
        jobHistory.getJobs().add(this);
        return this;
    }

    public Job removeHistory(JobHistory jobHistory) {
        this.histories.remove(jobHistory);
        jobHistory.getJobs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalary=" + getMaxSalary() +
            "}";
    }
}
