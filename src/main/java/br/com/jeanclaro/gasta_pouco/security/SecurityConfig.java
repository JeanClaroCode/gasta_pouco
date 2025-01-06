package br.com.jeanclaro.gasta_pouco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityUserFilter securityUserFilter;

    @Autowired
    private SecurityTransactionFilter securityTransactionFilter;

    private static final String[] PERMIT_ALL_LIST = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/actuator/**"
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
            auth
            .requestMatchers("/user/**").permitAll()
            .requestMatchers("/user/register").permitAll()
            //.requestMatchers("/user/login").permitAll()
            .requestMatchers("/transaction/**").permitAll()
            .requestMatchers("/transaction/expense").permitAll()
            .requestMatchers("/transaction/income").permitAll()
            .requestMatchers(PERMIT_ALL_LIST).permitAll();

            auth.anyRequest().authenticated();
        })
        .addFilterAfter(securityUserFilter, BasicAuthenticationFilter.class)
        .addFilterAfter(securityTransactionFilter,BasicAuthenticationFilter.class );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
