package tech.jhipster.sample.repository;

import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import tech.jhipster.sample.domain.User;

/**
 * Micronaut Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public static String USERS_CACHE = "usersByLogin";

    public Optional<User> findOneByActivationKey(String activationKey);

    public List<User> findAllByActivatedFalseAndCreatedDateBefore(Instant dateTime);

    public Optional<User> findOneByResetKey(String resetKey);

    public Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_CACHE)
    public Optional<User> findOneByLoginIgnoreCaseOrEmail(String login, String email);

    public Page<User> findByLoginNotEqual(String login, Pageable pageable);

    public void update(@Id Long id, Instant createdDate);
}
