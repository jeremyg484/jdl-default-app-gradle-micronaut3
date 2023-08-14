package tech.jhipster.sample.service.impl;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.repository.BankAccountRepository;
import tech.jhipster.sample.service.BankAccountService;
import tech.jhipster.sample.service.dto.BankAccountDTO;
import tech.jhipster.sample.service.mapper.BankAccountMapper;

/**
 * Service Implementation for managing {@link BankAccount}.
 */
@Singleton
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BankAccountDTO save(BankAccountDTO bankAccountDTO) {
        log.debug("Request to save BankAccount : {}", bankAccountDTO);
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDto(bankAccount);
    }

    /**
     * Update a bankAccount.
     *
     * @param bankAccountDTO the entity to update.
     * @return the persisted entity.
     */
    @Override
    public BankAccountDTO update(BankAccountDTO bankAccountDTO) {
        log.debug("Request to update BankAccount : {}", bankAccountDTO);
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount = bankAccountRepository.update(bankAccount);
        return bankAccountMapper.toDto(bankAccount);
    }

    /**
     * Get all the bankAccounts.
     *
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public List<BankAccountDTO> findAll() {
        log.debug("Request to get all BankAccounts");
        return bankAccountRepository.findAll().stream().map(bankAccountMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one bankAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<BankAccountDTO> findOne(Long id) {
        log.debug("Request to get BankAccount : {}", id);
        return bankAccountRepository.findById(id).map(bankAccountMapper::toDto);
    }

    /**
     * Delete the bankAccount by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankAccount : {}", id);
        bankAccountRepository.deleteById(id);
    }
}
