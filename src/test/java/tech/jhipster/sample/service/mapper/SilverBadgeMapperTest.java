package tech.jhipster.sample.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SilverBadgeMapperTest {

    private SilverBadgeMapper silverBadgeMapper;

    @BeforeEach
    public void setUp() {
        silverBadgeMapper = new SilverBadgeMapperImpl();
    }
}
