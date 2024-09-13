package com.hasten.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;

/**
 *
 * @author Hasten
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public KeyPair keyPair(JwtProperties properties) {
        properties.getAlias();

        return null;
    }
}
