package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;
import tech.jhipster.sample.domain.BankAccount;

/**
 * Micronaut Data  repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Query("select bankAccount from BankAccount bankAccount where bankAccount.user.login = :username ")
    public List<BankAccount> findByUserIsCurrentUser(String username);
}
