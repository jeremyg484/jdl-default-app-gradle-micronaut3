package tech.jhipster.sample.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Hidden;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Hidden
@Controller("/v3/api-docs")
public class SwaggerResource {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Get
    public String getJsonSwagger() throws IOException {
        URL resourceAsStream = this.getClass().getResource("/META-INF/swagger/openapi.yml");
        Object obj = yamlMapper.readValue(resourceAsStream, Object.class);
        return jsonMapper.writeValueAsString(obj);
    }
}
