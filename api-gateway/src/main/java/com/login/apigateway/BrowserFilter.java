package com.login.apigateway;

import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

/**
 * Global filter to block REST clients and only allow browser traffic.
 * DISABLED: allowing all REST clients for API testing
 */
// @Component
public class BrowserFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String userAgent =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("User-Agent");

        if (userAgent == null ||
                !userAgent.contains("Mozilla")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.FORBIDDEN);

            return exchange.getResponse()
                    .setComplete();
        }

        return chain.filter(exchange);
    }
}
