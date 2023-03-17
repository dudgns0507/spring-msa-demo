package me.cropo.eurekaserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class WebSecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf().ignoringRequestMatchers("/eureka/**").and().authorizeHttpRequests {
            it.anyRequest().authenticated()
        }.httpBasic(withDefaults())
        return http.build()
    }
}