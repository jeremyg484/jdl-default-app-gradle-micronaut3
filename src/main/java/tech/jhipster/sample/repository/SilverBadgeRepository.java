package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import tech.jhipster.sample.domain.SilverBadge;

/**
 * Micronaut Data  repository for the SilverBadge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SilverBadgeRepository extends JpaRepository<SilverBadge, Long> {}
