package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import tech.jhipster.sample.domain.TheLabel;

/**
 * Micronaut Data  repository for the TheLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TheLabelRepository extends JpaRepository<TheLabel, Long> {}
