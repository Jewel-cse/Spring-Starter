package dev.start.init.config.security;


import dev.start.init.config.properties.CorsConfigProperties;
import dev.start.init.config.security.jwt.JwtAuthTokenFilter;
import dev.start.init.config.security.jwt.JwtAuthenticationEntryPoint;
import dev.start.init.constants.AdminConstants;
import dev.start.init.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static dev.start.init.constants.apiEndPoint.API_V1.TWO_FACTOR_URL;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * This configuration handles api web requests with stateless session.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ApiWebSecurityConfig {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final AuthenticationManager authenticationManager;
    private final CorsConfigurationSource corsConfigurationSource;


    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource);
    }

    /**
     * Configure the {@link HttpSecurity}. Typically, subclasses should not call super as it may
     * override their configuration.
     *
     * @param http the {@link HttpSecurity} to modify.
     * @throws Exception thrown when error happens during authentication.
     */
    @Bean
    @Order
    public SecurityFilterChain apiFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

        // Match any incoming request targeting the /api/** to use this security filter chain
        http.securityMatcher(SecurityConstants.API_ROOT_URL_MAPPING);

        // Use a custom exception handler when authentication fails
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler))

                // guarantee that the application won't create any session at all by using stateless policy
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(requests -> {
                    //allow websocket end point to testing
                    requests.requestMatchers(new AntPathRequestMatcher("ws://localhost:8080/ws/chat")).permitAll();
                    // Allow public access to Swagger/OpenAPI documentation endpoints
                    requests.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"), new AntPathRequestMatcher("/swagger-ui/**"), new AntPathRequestMatcher("/swagger-ui.html")).permitAll();
                    // Allow public access to POST /api/v1/users: Used to register new users
                    requests.requestMatchers(new AntPathRequestMatcher(AdminConstants.API_V1_USERS_ROOT_URL, HttpMethod.POST.name()), new AntPathRequestMatcher(AdminConstants.API_V1_USERS_ROOT_URL + "/verify", HttpMethod.GET.name())).permitAll();

                    // Allow access for users to authenticate after registration and refresh tokens
                    requests.requestMatchers(new AntPathRequestMatcher(SecurityConstants.API_V1_AUTH_URL_MAPPING)).permitAll();
                    requests.requestMatchers(new AntPathRequestMatcher(TWO_FACTOR_URL + "/verify-otp", HttpMethod.POST.name())).permitAll();

                    requests.requestMatchers(new AntPathRequestMatcher("/api/v1/doc-view/**", HttpMethod.GET.name())).permitAll();

                    requests.anyRequest().authenticated();
                })
                //.cors().configurationSource(corsConfigurationSource)

                // If your application is attaching the credentials via an Authorization header,
                // then the browser can't automatically authenticate the requests,
                // and CSRF isn't possible.
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager).addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

