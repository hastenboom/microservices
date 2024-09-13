package com.hasten.gateway.filter;

import com.hasten.gateway.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Hasten
 */
@Component
@RequiredArgsConstructor
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private final StringRedisTemplate redisTemplate;

    private List<String> excludePaths = List.of("/login");

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final String LOGIN_PREFIX = "login:token:";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. get the request
        ServerHttpRequest request = exchange.getRequest();

        for (var excludePath : excludePaths) {
            if (antPathMatcher.match(excludePath, request.getPath().toString()))
                return chain.filter(exchange);
        }

       /* if (this.isExcludePath(request.getPath().toString())) {
            return chain.filter(exchange);
        }*/
        // 2. intercept it and tryGetting{the UserInfo}

        List<String> authHeaders = request.getHeaders().get("auth");
        if (authHeaders == null || authHeaders.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //TODO: here Intercept here;
            return response.setComplete();
        }

        String token = authHeaders.get(0);
        //user id only
        String userInfo = redisTemplate.opsForValue().get(LOGIN_PREFIX + token);
        if (userInfo == null) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //TODO: here Intercept here;
            return response.setComplete();
        }

        ServerWebExchange newExc = exchange.mutate()
                .request(builder -> builder.header("user-id", userInfo))
                .build();

        return chain.filter(newExc);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    public boolean isExcludePath(String path) {
        for (var exludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(exludePath, path))
                return true;
        }
        return false;
    }
}
