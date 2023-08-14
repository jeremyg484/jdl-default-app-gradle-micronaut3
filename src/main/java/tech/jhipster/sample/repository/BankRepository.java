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
import tech.jhipster.sample.domain.Bank;

/**
 * Micronaut Data  repository for the Bank entity.
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    @Query(
        value = "select distinct bank from Bank bank left join fetch bank.bankAccounts",
        countQuery = "select count(distinct bank) from Bank bank"
    )
    public Page<Bank> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct bank from Bank bank left join fetch bank.bankAccounts")
    public List<Bank> findAllWithEagerRelationships();

    @Query("select bank from Bank bank left join fetch bank.bankAccounts where bank.id =:id")
    public Optional<Bank> findOneWithEagerRelationships(Long id);
}
