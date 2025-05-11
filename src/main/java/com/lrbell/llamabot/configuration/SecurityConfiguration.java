package com.lrbell.llamabot.configuration;

import com.lrbell.llamabot.service.security.CustomUserDetailsService;
import com.lrbell.llamabot.service.security.JwtTokenFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    /**
     * Custom user details service.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * JWT token filer
     */
    private final JwtTokenFilter jwtFilter;

    /**
     * Constructor.
     *
     * @param jwtFilter
     * @param uds
     */
    public SecurityConfiguration(final JwtTokenFilter jwtFilter, final CustomUserDetailsService uds) {
        this.userDetailsService = uds;
        this.jwtFilter = jwtFilter;
    }


    /**
     * Password encoder.
     *
     * @return The password encoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * User authentication provider.
     *
     * @return The auth provider bean.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider =  new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * The authentication manager.
     *
     * @param http
     * @return The authentication manager bean.
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Security filter to allow requests to /api/auth but secure other endpoints.
     *
     * @param http
     * @return The security filter bean.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/index.html").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/graphql").authenticated()
                        .anyRequest().denyAll()
                )
                .sessionManagement(s -> { s.sessionCreationPolicy(SessionCreationPolicy.STATELESS); })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
