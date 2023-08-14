package tech.jhipster.sample.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GoldenBadgeMapperTest {

    private GoldenBadgeMapper goldenBadgeMapper;

    @BeforeEach
    public void setUp() {
        goldenBadgeMapper = new GoldenBadgeMapperImpl();
    }
}
