package fr.hoenheimsports.gatewayservice.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApiKeyFilterTest {

    @Mock
    private GatewayFilterChain filterChain;

    private ApiKeyFilter filter;
    private MockServerWebExchange exchange;

    @BeforeEach
    void setUp() {
        filter = new ApiKeyFilter();
        // Configure les valeurs des clés API pour les tests
        ReflectionTestUtils.setField(filter, "API_KEY", "API-Key");
        ReflectionTestUtils.setField(filter, "VALID_API_KEY", "valid-api-key");
    }

    @Test
    void testFilterWhenApiKeyHeaderIsMissing() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        exchange = MockServerWebExchange.from(request);

        Mono<Void> result = filter.filter(exchange, filterChain);

        assertThat(result).isNotNull();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(filterChain, never()).filter(exchange);
    }

    @Test
    void testFilterWhenApiKeyHeaderIsPresentButInvalid() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header("API-Key", "invalid-api-key")
                .build();
        exchange = MockServerWebExchange.from(request);

        Mono<Void> result = filter.filter(exchange, filterChain);

        assertThat(result).isNotNull();
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(filterChain, never()).filter(exchange);
    }

    @Test
    void testFilterWhenApiKeyHeaderIsValid() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header("API-Key", "valid-api-key")
                .build();
        exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, filterChain);

        assertThat(result).isNotNull();
        verify(filterChain).filter(exchange);
    }
}
