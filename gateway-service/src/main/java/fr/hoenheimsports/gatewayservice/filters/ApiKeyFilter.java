package fr.hoenheimsports.gatewayservice.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiKeyFilter implements GlobalFilter, Ordered {

    @Value("${api.key.name}")
    private  String API_KEY;
    @Value("${api.key.value}")
    private  String VALID_API_KEY;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Vérifiez si la requête contient l'en-tête API-Key
        if (!exchange.getRequest().getHeaders().containsKey(API_KEY)) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();  // Bloquer la requête si la clé API n'est pas présente
        }

        // Vérifiez si la clé API est correcte
        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY);
        if (!VALID_API_KEY.equals(apiKey)) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();  // Bloquer la requête si la clé API est incorrecte
        }

        // Si la clé API est correcte, passez la requête au filtre suivant
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;  // Priorité haute pour ce filtre afin qu'il s'exécute en premier
    }
}
