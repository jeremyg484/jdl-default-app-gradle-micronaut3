package tech.jhipster.sample.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "advertisement")
    private byte[] advertisement;

    @Column(name = "advertisement_content_type")
    private String advertisementContentType;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @JsonIgnoreProperties(value = { "countries" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "jobs", "manager", "sibag", "gobag", "department", "histories" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(mappedBy = "departments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "departments", "jobs", "emps" }, allowSetters = true)
    private Set<JobHistory> histories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Department id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Department name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Department description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getAdvertisement() {
        return this.advertisement;
    }

    public Department advertisement(byte[] advertisement) {
        this.setAdvertisement(advertisement);
        return this;
    }

    public void setAdvertisement(byte[] advertisement) {
        this.advertisement = advertisement;
    }

    public String getAdvertisementContentType() {
        return this.advertisementContentType;
    }

    public Department advertisementContentType(String advertisementContentType) {
        this.advertisementContentType = advertisementContentType;
        return this;
    }

    public void setAdvertisementContentType(String advertisementContentType) {
        this.advertisementContentType = advertisementContentType;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Department logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Department logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Department location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setDepartment(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setDepartment(this));
        }
        this.employees = employees;
    }

    public Department employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Department addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this);
        return this;
    }

    public Department removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
        return this;
    }

    public Set<JobHistory> getHistories() {
        return this.histories;
    }

    public void setHistories(Set<JobHistory> jobHistories) {
        if (this.histories != null) {
            this.histories.forEach(i -> i.removeDepartment(this));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.addDepartment(this));
        }
        this.histories = jobHistories;
    }

    public Department histories(Set<JobHistory> jobHistories) {
        this.setHistories(jobHistories);
        return this;
    }

    public Department addHistory(JobHistory jobHistory) {
        this.histories.add(jobHistory);
        jobHistory.getDepartments().add(this);
        return this;
    }

    public Department removeHistory(JobHistory jobHistory) {
        this.histories.remove(jobHistory);
        jobHistory.getDepartments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", advertisement='" + getAdvertisement() + "'" +
            ", advertisementContentType='" + getAdvertisementContentType() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
