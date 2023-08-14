package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import tech.jhipster.sample.domain.Identifier;

/**
 * Micronaut Data  repository for the Identifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentifierRepository extends JpaRepository<Identifier, Long> {}
