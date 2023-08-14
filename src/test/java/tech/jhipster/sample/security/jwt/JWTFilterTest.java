package tech.jhipster.sample.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.*;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import tech.jhipster.sample.security.AuthoritiesConstants;
import tech.jhipster.sample.security.DatabaseAuthenticationProvider;

@MicronautTest
@Property(name = "spec.name", value = "JWTFilterTest")
public class JWTFilterTest {

    @Inject
    @Client("/")
    Rx3HttpClient client;

    @Test
    public void testJWTFilter() throws Exception {
        AccessRefreshToken token = client
            .retrieve(
                HttpRequest.POST("/api/authenticate", new UsernamePasswordCredentials("test-user", "test-password")),
                AccessRefreshToken.class
            )
            .blockingFirst();

        String jwt = token.getAccessToken();

        String username = client.retrieve(HttpRequest.GET("/api/test").bearerAuth(jwt)).blockingFirst();

        assertThat(username).isEqualTo("test-user");
    }

    @Test
    public void testJWTFilterInvalidToken() throws Exception {
        String jwt = "wrong_jwt";

        HttpResponse<String> username = client
            .exchange(HttpRequest.GET("/api/test").bearerAuth(jwt), String.class)
            .onErrorReturn(t -> (HttpResponse<String>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(username.code()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    public void testJWTFilterMissingAuthorization() throws Exception {
        HttpResponse<String> username = client
            .exchange(HttpRequest.GET("/api/test"), String.class)
            .onErrorReturn(t -> (HttpResponse<String>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(username.code()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    public void testJWTFilterMissingToken() throws Exception {
        HttpResponse<String> username = client
            .exchange(HttpRequest.GET("/api/test").bearerAuth(""), String.class)
            .onErrorReturn(t -> (HttpResponse<String>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(username.code()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    public void testJWTFilterWrongScheme() throws Exception {
        HttpResponse<String> username = client
            .exchange(HttpRequest.GET("/api/test").basicAuth("test-user", "test-password"), String.class)
            .onErrorReturn(t -> (HttpResponse<String>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(username.code()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Controller("/api")
    static class TestAuthenticationController {

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Get("/test")
        String test(Authentication authentication) {
            return authentication.getName();
        }
    }

    @Replaces(DatabaseAuthenticationProvider.class)
    @Requires(property = "spec.name", value = "JWTFilterTest")
    @Singleton
    static class MockAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Publisher<AuthenticationResponse> authenticate(
            @Nullable HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest
        ) {
            if (
                authenticationRequest.getIdentity().toString().equals("test-user") &&
                authenticationRequest.getSecret().toString().equals("test-password")
            ) {
                return Flowable.just(AuthenticationResponse.success("test-user", Collections.emptyList()));
            }
            return Flowable.empty();
        }
    }
}
