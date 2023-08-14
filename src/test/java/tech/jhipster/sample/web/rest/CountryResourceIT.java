package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tech.jhipster.sample.domain.Country;
import tech.jhipster.sample.repository.CountryRepository;
import tech.jhipster.sample.service.dto.CountryDTO;
import tech.jhipster.sample.service.mapper.CountryMapper;

/**
 * Integration tests for the {@Link CountryResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CountryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private CountryMapper countryMapper;

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Country country;

    @BeforeEach
    public void initTest() {
        country = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Country country = new Country().name(DEFAULT_NAME);
        return country;
    }

    /**
     * Delete all country entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Country.class);
    }

    @Test
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        CountryDTO countryDTO = countryMapper.toDto(country);

        // Create the Country
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.POST("/api/countries", countryDTO), CountryDTO.class)
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);

        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createCountryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.POST("/api/countries", countryDTO), CountryDTO.class)
            .onErrorReturn(t -> (HttpResponse<CountryDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the countryList w/ all the countries
        List<CountryDTO> countries = client
            .retrieve(HttpRequest.GET("/api/countries?eagerload=true"), Argument.listOf(CountryDTO.class))
            .blockingFirst();
        CountryDTO testCountry = countries.get(0);

        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        CountryDTO testCountry = client.retrieve(HttpRequest.GET("/api/countries/" + country.getId()), CountryDTO.class).blockingFirst();

        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingCountry() throws Exception {
        // Get the country
        @SuppressWarnings("unchecked")
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.GET("/api/countries/" + Long.MAX_VALUE), CountryDTO.class)
            .onErrorReturn(t -> (HttpResponse<CountryDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();

        updatedCountry.name(UPDATED_NAME);
        CountryDTO updatedCountryDTO = countryMapper.toDto(updatedCountry);

        @SuppressWarnings("unchecked")
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.PUT("/api/countries/" + country.getId(), updatedCountryDTO), CountryDTO.class)
            .onErrorReturn(t -> (HttpResponse<CountryDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);

        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.PUT("/api/countries/" + country.getId(), countryDTO), CountryDTO.class)
            .onErrorReturn(t -> (HttpResponse<CountryDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCountry() throws Exception {
        // Initialize the database with one entity
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        @SuppressWarnings("unchecked")
        HttpResponse<CountryDTO> response = client
            .exchange(HttpRequest.DELETE("/api/countries/" + country.getId()), CountryDTO.class)
            .onErrorReturn(t -> (HttpResponse<CountryDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = new Country();
        country1.setId(1L);
        Country country2 = new Country();
        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);
        country2.setId(2L);
        assertThat(country1).isNotEqualTo(country2);
        country1.setId(null);
        assertThat(country1).isNotEqualTo(country2);
    }
}
