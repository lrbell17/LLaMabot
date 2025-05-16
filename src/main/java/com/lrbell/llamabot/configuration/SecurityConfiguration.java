package com.lrbell.llamabot.configuration;

import com.lrbell.llamabot.service.security.oauth.CustomOidcUserService;
import com.lrbell.llamabot.service.security.CustomUserDetailsService;
import com.lrbell.llamabot.service.security.jwt.JwtTokenFilter;
import com.lrbell.llamabot.service.security.oauth.OAuthLoginSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
     * Custom OAuth user service.
     */
    private final CustomOidcUserService oidcUserService;

    /**
     * Constructor.
     *
     * @param jwtFilter
     * @param userDetailsService
     * @param oidcUserService
     */
    public SecurityConfiguration(final JwtTokenFilter jwtFilter, final CustomUserDetailsService userDetailsService,
                                 final CustomOidcUserService oidcUserService) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.oidcUserService = oidcUserService;
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
     * @param config
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
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final OAuthLoginSuccessHandler successHandler) throws Exception {
        http
                .csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allow static & root resources
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/index.html").permitAll()
                        // allow REST auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // require auth for other endpoints
                        .requestMatchers("/graphql").authenticated()
                        .anyRequest().denyAll()
                )
                .authenticationProvider(authenticationProvider())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.oidcUserService(oidcUserService))
                        .successHandler(successHandler)
                )
                .sessionManagement(s -> { s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
