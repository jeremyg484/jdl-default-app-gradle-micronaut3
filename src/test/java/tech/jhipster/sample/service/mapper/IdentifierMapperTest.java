package tech.jhipster.sample.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdentifierMapperTest {

    private IdentifierMapper identifierMapper;

    @BeforeEach
    public void setUp() {
        identifierMapper = new IdentifierMapperImpl();
    }
}
