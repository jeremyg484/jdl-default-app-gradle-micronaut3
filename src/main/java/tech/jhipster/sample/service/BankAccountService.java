package tech.jhipster.sample.service;

import java.util.List;
import java.util.Optional;
import tech.jhipster.sample.service.dto.BankAccountDTO;

/**
 * Service Interface for managing {@link tech.jhipster.sample.domain.BankAccount}.
 */
public interface BankAccountService {
    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save.
     * @return the persisted entity.
     */
    BankAccountDTO save(BankAccountDTO bankAccountDTO);

    /**
     * Update a bankAccount.
     *
     * @param bankAccountDTO the entity to save.
     * @return the persisted entity.
     */
    BankAccountDTO update(BankAccountDTO bankAccountDTO);

    /**
     * Get all the bankAccounts.
     *
     * @return the list of entities.
     */
    List<BankAccountDTO> findAll();

    /**
     * Get the "id" bankAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankAccountDTO> findOne(Long id);

    /**
     * Delete the "id" bankAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
