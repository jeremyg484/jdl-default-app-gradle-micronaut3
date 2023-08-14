package tech.jhipster.sample.web.rest.errors.handlers;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.UnsatisfiedRouteHandler;
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException;
import jakarta.inject.Singleton;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import tech.jhipster.sample.util.HeaderUtil;
import tech.jhipster.sample.web.rest.errors.BadRequestAlertException;

@Replaces(UnsatisfiedRouteHandler.class)
@Singleton
public class UnsatisfiedRouteExceptionHandler extends ProblemHandler implements ExceptionHandler<UnsatisfiedRouteException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, UnsatisfiedRouteException exception) {
        Problem problem = Problem.builder().withStatus(Status.BAD_REQUEST).build();
        return create(problem, request, exception);
    }
}
