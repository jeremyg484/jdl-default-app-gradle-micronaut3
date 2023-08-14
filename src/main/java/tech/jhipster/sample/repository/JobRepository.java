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
import tech.jhipster.sample.domain.Job;

/**
 * Micronaut Data  repository for the Job entity.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(value = "select distinct job from Job job left join fetch job.chores", countQuery = "select count(distinct job) from Job job")
    public Page<Job> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct job from Job job left join fetch job.chores")
    public List<Job> findAllWithEagerRelationships();

    @Query("select job from Job job left join fetch job.chores where job.id =:id")
    public Optional<Job> findOneWithEagerRelationships(Long id);
}
