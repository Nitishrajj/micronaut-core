package io.micronaut.http.server.tck.tests.filter;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.tck.AssertionUtils;
import io.micronaut.http.tck.HttpResponseAssertion;
import io.micronaut.http.tck.TestScenario;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({
        "java:S5960", // We're allowed assertions, as these are used in tests only
        "checkstyle:MissingJavadocType",
        "checkstyle:DesignForExtension"
})
public class RequestFilterPublisherProceedTest {
    public static final String SPEC_NAME = "RequestFilterPublisherProceedTest";

    @Test
    public void requestFilterProceedWithCompletableFuture() throws IOException {
        TestScenario.builder()
                .specName(SPEC_NAME)
                .request(HttpRequest.GET("/foobar").header("X-FOOBAR", "123"))
                .assertion((server, request) -> AssertionUtils.assertDoesNotThrow(server, request, HttpResponseAssertion.builder()
                        .status(HttpStatus.ACCEPTED)
                        .build()))
                .run();

        TestScenario.builder()
                .specName(SPEC_NAME)
                .request(HttpRequest.GET("/foobar"))
                .assertion((server, request) -> AssertionUtils.assertThrows(server, request, HttpResponseAssertion.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .build()))
                .run();
    }

    /*
    //tag::clazz[]
    @ServerFilter(ServerFilter.MATCH_ALL_PATTERN)
    class CsrfFilter {
    //end::clazz[]
    */
    @Requires(property = "spec.name", value = SPEC_NAME)
    @ServerFilter(ServerFilter.MATCH_ALL_PATTERN)
    static class FooBarFilter {
    //tag::methods[]
        @RequestFilter
        public Publisher<Optional<HttpResponse<?>>> filter(@NonNull HttpRequest<?> request) {
            if (request.getHeaders().contains("X-FOOBAR")) {
                // proceed
                return Mono.just(Optional.empty());
            } else {
                return Mono.just(Optional.of(HttpResponse.unauthorized()));
            }
        }
    }
    //end::methods[]

    @Requires(property = "spec.name", value = SPEC_NAME)
    @Controller("/foobar")
    static class FooBarController {
        @Get
        @Status(HttpStatus.ACCEPTED)
        void index() {
            // no-op
        }
    }
}
