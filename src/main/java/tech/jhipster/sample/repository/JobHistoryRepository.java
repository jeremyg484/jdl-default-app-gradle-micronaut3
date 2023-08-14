package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
// TODO what is MN equivalent?
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import tech.jhipster.sample.domain.JobHistory;

/**
 * Micronaut Data  repository for the JobHistory entity.
 */
@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {
    @Query(
        value = "select distinct jobHistory from JobHistory jobHistory left join fetch jobHistory.departments left join fetch jobHistory.jobs left join fetch jobHistory.emps",
        countQuery = "select count(distinct jobHistory) from JobHistory jobHistory"
    )
    public Page<JobHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct jobHistory from JobHistory jobHistory left join fetch jobHistory.departments left join fetch jobHistory.jobs left join fetch jobHistory.emps"
    )
    public List<JobHistory> findAllWithEagerRelationships();

    @Query(
        "select jobHistory from JobHistory jobHistory left join fetch jobHistory.departments left join fetch jobHistory.jobs left join fetch jobHistory.emps where jobHistory.id =:id"
    )
    public Optional<JobHistory> findOneWithEagerRelationships(Long id);
}
