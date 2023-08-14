package tech.jhipster.sample.config;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.Properties;
import javax.cache.CacheManager;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import tech.jhipster.sample.util.JHipsterProperties;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new EhcacheCachingProvider()
            .getCacheManager(null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, tech.jhipster.sample.repository.UserRepository.USERS_CACHE);
        createCache(cm, tech.jhipster.sample.domain.User.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Authority.class.getName());
        createCache(cm, tech.jhipster.sample.domain.User.class.getName() + ".authorities");
        createCache(cm, tech.jhipster.sample.domain.Bank.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Bank.class.getName() + ".bankAccounts");
        createCache(cm, tech.jhipster.sample.domain.BankAccount.class.getName());
        createCache(cm, tech.jhipster.sample.domain.BankAccount.class.getName() + ".operations");
        createCache(cm, tech.jhipster.sample.domain.BankAccount.class.getName() + ".banks");
        createCache(cm, tech.jhipster.sample.domain.TheLabel.class.getName());
        createCache(cm, tech.jhipster.sample.domain.TheLabel.class.getName() + ".operations");
        createCache(cm, tech.jhipster.sample.domain.Operation.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Operation.class.getName() + ".theLabels");
        createCache(cm, tech.jhipster.sample.domain.Department.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Department.class.getName() + ".employees");
        createCache(cm, tech.jhipster.sample.domain.Department.class.getName() + ".histories");
        createCache(cm, tech.jhipster.sample.domain.JobHistory.class.getName());
        createCache(cm, tech.jhipster.sample.domain.JobHistory.class.getName() + ".departments");
        createCache(cm, tech.jhipster.sample.domain.JobHistory.class.getName() + ".jobs");
        createCache(cm, tech.jhipster.sample.domain.JobHistory.class.getName() + ".emps");
        createCache(cm, tech.jhipster.sample.domain.Job.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Job.class.getName() + ".chores");
        createCache(cm, tech.jhipster.sample.domain.Job.class.getName() + ".histories");
        createCache(cm, tech.jhipster.sample.domain.Employee.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Employee.class.getName() + ".jobs");
        createCache(cm, tech.jhipster.sample.domain.Employee.class.getName() + ".histories");
        createCache(cm, tech.jhipster.sample.domain.Location.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Location.class.getName() + ".countries");
        createCache(cm, tech.jhipster.sample.domain.Task.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Task.class.getName() + ".linkedJobs");
        createCache(cm, tech.jhipster.sample.domain.GoldenBadge.class.getName());
        createCache(cm, tech.jhipster.sample.domain.SilverBadge.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Identifier.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Country.class.getName());
        createCache(cm, tech.jhipster.sample.domain.Country.class.getName() + ".areas");
        createCache(cm, tech.jhipster.sample.domain.Region.class.getName());
        // jhipster-needle-ehcache-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
