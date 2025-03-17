package com.example.Gateway.securityFilter;

import org.springframework.test.util.ReflectionTestUtils;
import com.example.Gateway.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    @Mock
    private RouteValidator routeValidator;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        webClient = Mockito.mock(WebClient.class);
        requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        // Initialize the isSecured predicate in the RouteValidator mock
        Predicate<ServerHttpRequest> isSecuredPredicate = request ->
                RouteValidator.openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
        ReflectionTestUtils.setField(routeValidator, "isSecured", isSecuredPredicate);
    }

    @Test
    void testValidToken() {
        // Arrange
        String validToken = "valid.jwt.token";
        String userDetailsJson = "{\"id\":1,\"username\":\"user\",\"role\":\"ROLE_USER\"}";

        ServerHttpRequest request = MockServerHttpRequest.get("/secured-endpoint")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.builder((MockServerHttpRequest) request).build();

        // Add stubbing for webClientBuilder.build() here
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(userDetailsJson));
        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = authenticationFilter.apply(new AuthenticationFilter.Config()).filter(exchange, filterChain);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        // Verify headers are added
        ServerHttpRequest modifiedRequest = exchange.getRequest();
        assert modifiedRequest.getHeaders().getFirst("X-User-Role").equals("ROLE_USER");
        assert modifiedRequest.getHeaders().getFirst("X-User-Name").equals("user");
        assert modifiedRequest.getHeaders().getFirst("X-User-Id").equals("1");
    }

    @Test
    void testInvalidToken() {
        // Arrange
        String invalidToken = "invalid.jwt.token";

        ServerHttpRequest request = MockServerHttpRequest.get("/secured-endpoint")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.builder((MockServerHttpRequest) request).build();

        // Add stubbing for webClientBuilder.build() here
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("Invalid Token")));

        // Act
        Mono<Void> result = authenticationFilter.apply(new AuthenticationFilter.Config()).filter(exchange, filterChain);

        // Assert
        StepVerifier.create(result)
                .verifyErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert ((RuntimeException) throwable).getMessage().contains("Invalid Token");
                });

        ServerHttpResponse response = exchange.getResponse();
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }
}