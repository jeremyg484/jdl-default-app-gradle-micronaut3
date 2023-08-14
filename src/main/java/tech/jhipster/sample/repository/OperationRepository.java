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
import tech.jhipster.sample.domain.Operation;

/**
 * Micronaut Data  repository for the Operation entity.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query(
        value = "select distinct operation from Operation operation left join fetch operation.theLabels",
        countQuery = "select count(distinct operation) from Operation operation"
    )
    public Page<Operation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct operation from Operation operation left join fetch operation.theLabels")
    public List<Operation> findAllWithEagerRelationships();

    @Query("select operation from Operation operation left join fetch operation.theLabels where operation.id =:id")
    public Optional<Operation> findOneWithEagerRelationships(Long id);
}
