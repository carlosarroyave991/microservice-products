package com.arka.microservice.productos.infraestructure.driver.rest.config.security;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@EnableReactiveMethodSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveJwtDecoder jwtDecoder,
                                                         ReactiveJwtAuthenticationConverter jwtAuthenticationConverter) {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        // Endpoint de stock permitido sin autenticación (debe ir primero)
                        .pathMatchers(HttpMethod.PUT, "/api/product/{id}/stock").permitAll()
                        // Endpoints GET para productos: accesibles a roles "client" y "admin"
                        .pathMatchers(HttpMethod.GET, "/api/product", "/api/product/{id}","/api/category/all")
                        .hasAnyRole("client", "admin")
                        // Endpoints que requieren el rol admin: POST, PUT y DELETE
                        .pathMatchers(HttpMethod.POST, "/api/product/**","/api/category/**","/api/product-category/**").hasRole("admin")
                        .pathMatchers(HttpMethod.PUT, "/api/product/**","/api/category/**").hasRole("admin")
                        .pathMatchers(HttpMethod.DELETE, "/api/product/**","/api/category/**").hasRole("admin")
                        // Para cualquier otra petición se requiere autenticación
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }
}