package com.chaw.shopping_note.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll() // Swagger 관련은 모두 허용
                    .anyExchange().authenticated() // 나머지는 인증 필요
            }
            .httpBasic { it.disable() } // Basic Auth 끔
            .formLogin { it.disable() } // Form Login 끔
            .csrf { it.disable() }      // CSRF 끔 (API 서버는 보통 끔)
            .build()
    }
}
