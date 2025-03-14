package com.example.Gateway.securityFilter;

import com.example.Gateway.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    RouteValidator validator;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer")) {
                    authHeader = authHeader.substring(7).trim();
                }

                WebClient webClient = webClientBuilder.build();
                return webClient.get()
                        .uri("http://USER-SERVICE/users/validateToken?token=" + authHeader)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(userDetailsJson -> {
                            Map<String, String> userDetails = parseUserDetails(userDetailsJson);

                            String id = String.valueOf(userDetails.get("id"));
                            String username = userDetails.get("username");
                            String role = userDetails.get("role");

                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Role", role)
                                    .header("X-User-Name", username)
                                    .header("X-User-Id", id)
                                    .build();

                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        })
                        .onErrorResume(e -> onError(exchange, "Invalid Token : " + e.getMessage(), HttpStatus.UNAUTHORIZED));
            }
            return chain.filter(exchange);
        });
    }

    private Map<String, String> parseUserDetails(String userDetailsJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> userDetails = objectMapper.readValue(userDetailsJson, Map.class);
            return userDetails;
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errMsg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errMsg);
        errorResponse.put("status", httpStatus.value());

        byte[] responseBytes;
        try {
            responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            responseBytes = ("{\"error\":\"Internal Server Error\",\"status\":500}").getBytes(StandardCharsets.UTF_8);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = response.bufferFactory().wrap(responseBytes);

        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
    }
}
