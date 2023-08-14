package tech.jhipster.sample.web.rest;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.sample.service.CountryService;
import tech.jhipster.sample.service.dto.CountryDTO;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tech.jhipster.sample.domain.Country}.
 */
@Controller("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    private static final String ENTITY_NAME = "country";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryService countryService;

    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * {@code POST  /countries} : Create a new country.
     *
     * @param countryDTO the countryDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new countryDTO, or with status {@code 400 (Bad Request)} if the country has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/countries")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CountryDTO> createCountry(@Body CountryDTO countryDTO) throws URISyntaxException {
        log.debug("REST request to save Country : {}", countryDTO);
        if (countryDTO.getId() != null) {
            throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountryDTO result = countryService.save(countryDTO);
        URI location = new URI("/api/countries/" + result.getId());
        return HttpResponse
            .created(result)
            .headers(headers -> {
                headers.location(location);
                HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
            });
    }

    /**
     * {@code PUT  /countries/:id} : Updates an existing country.
     *
     * @param countryDTO the countryDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated countryDTO,
     * or with status {@code 400 (Bad Request)} if the countryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/countries/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<CountryDTO> updateCountry(@Body CountryDTO countryDTO, @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update Country : {}", countryDTO);
        if (countryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        CountryDTO result = countryService.update(countryDTO);
        return HttpResponse
            .ok(result)
            .headers(headers ->
                HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, countryDTO.getId().toString())
            );
    }

    /**
     * {@code GET  /countries} : get all the countries.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of countries in body.
     */
    @Get("/countries")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<CountryDTO> getAllCountries(HttpRequest request) {
        log.debug("REST request to get all Countries");
        return countryService.findAll();
    }

    /**
     * {@code GET  /countries/:id} : get the "id" country.
     *
     * @param id the id of the countryDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the countryDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/countries/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<CountryDTO> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);

        return countryService.findOne(id);
    }

    /**
     * {@code DELETE  /countries/:id} : delete the "id" country.
     *
     * @param id the id of the countryDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/countries/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryService.delete(id);
        return HttpResponse
            .noContent()
            .headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
