package tech.jhipster.sample.repository;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import tech.jhipster.sample.domain.GoldenBadge;

/**
 * Micronaut Data  repository for the GoldenBadge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoldenBadgeRepository extends JpaRepository<GoldenBadge, Long> {}
