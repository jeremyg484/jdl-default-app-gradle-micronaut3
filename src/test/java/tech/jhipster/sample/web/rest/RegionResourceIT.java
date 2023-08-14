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
import tech.jhipster.sample.domain.Region;
import tech.jhipster.sample.repository.RegionRepository;
import tech.jhipster.sample.service.dto.RegionDTO;
import tech.jhipster.sample.service.mapper.RegionMapper;

/**
 * Integration tests for the {@Link RegionResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private RegionMapper regionMapper;

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private Region region;

    @BeforeEach
    public void initTest() {
        region = createEntity(transactionManager, em);
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
    public static Region createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Region region = new Region().name(DEFAULT_NAME);
        return region;
    }

    /**
     * Delete all region entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Region.class);
    }

    @Test
    public void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        RegionDTO regionDTO = regionMapper.toDto(region);

        // Create the Region
        HttpResponse<RegionDTO> response = client.exchange(HttpRequest.POST("/api/regions", regionDTO), RegionDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);

        assertThat(testRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region with an existing ID
        region.setId(1L);
        RegionDTO regionDTO = regionMapper.toDto(region);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<RegionDTO> response = client
            .exchange(HttpRequest.POST("/api/regions", regionDTO), RegionDTO.class)
            .onErrorReturn(t -> (HttpResponse<RegionDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the regionList w/ all the regions
        List<RegionDTO> regions = client
            .retrieve(HttpRequest.GET("/api/regions?eagerload=true"), Argument.listOf(RegionDTO.class))
            .blockingFirst();
        RegionDTO testRegion = regions.get(0);

        assertThat(testRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        RegionDTO testRegion = client.retrieve(HttpRequest.GET("/api/regions/" + region.getId()), RegionDTO.class).blockingFirst();

        assertThat(testRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getNonExistingRegion() throws Exception {
        // Get the region
        @SuppressWarnings("unchecked")
        HttpResponse<RegionDTO> response = client
            .exchange(HttpRequest.GET("/api/regions/" + Long.MAX_VALUE), RegionDTO.class)
            .onErrorReturn(t -> (HttpResponse<RegionDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).get();

        updatedRegion.name(UPDATED_NAME);
        RegionDTO updatedRegionDTO = regionMapper.toDto(updatedRegion);

        @SuppressWarnings("unchecked")
        HttpResponse<RegionDTO> response = client
            .exchange(HttpRequest.PUT("/api/regions/" + region.getId(), updatedRegionDTO), RegionDTO.class)
            .onErrorReturn(t -> (HttpResponse<RegionDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);

        assertThat(testRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Create the Region
        RegionDTO regionDTO = regionMapper.toDto(region);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<RegionDTO> response = client
            .exchange(HttpRequest.PUT("/api/regions/" + region.getId(), regionDTO), RegionDTO.class)
            .onErrorReturn(t -> (HttpResponse<RegionDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteRegion() throws Exception {
        // Initialize the database with one entity
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Delete the region
        @SuppressWarnings("unchecked")
        HttpResponse<RegionDTO> response = client
            .exchange(HttpRequest.DELETE("/api/regions/" + region.getId()), RegionDTO.class)
            .onErrorReturn(t -> (HttpResponse<RegionDTO>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = new Region();
        region1.setId(1L);
        Region region2 = new Region();
        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);
        region2.setId(2L);
        assertThat(region1).isNotEqualTo(region2);
        region1.setId(null);
        assertThat(region1).isNotEqualTo(region2);
    }
}
