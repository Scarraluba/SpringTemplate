package concrete.goonies.bulkbusketbackend.configs;

import concrete.goonies.bulkbusketbackend.Filters.CustomAuthFilter;
import concrete.goonies.bulkbusketbackend.configs.handlers.CustomAccessHandler;
import concrete.goonies.bulkbusketbackend.configs.handlers.CustomAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecureConfig {

    private final CustomAccessHandler customAccessHandler;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CustomAuthFilter customAuthFilter;

    private static final String[] PUBLIC_URLS = {"/user/login**", "/user/register/**", "/user/verify/code/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // User permissions
                        .requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER")

                        // Customer permissions
                        .requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")

                        // Everything else requires authentication
                        .anyRequest().authenticated())

                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessHandler).authenticationEntryPoint(customAuthEntryPoint))

                // JWT filter runs before Spring's username/password filter
                .addFilterBefore(customAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);

        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080", "https://securecapita.org"));

        corsConfiguration.setAllowedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization", "X-Requested-With"));

        corsConfiguration.setExposedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Content-Type", "Accept", "Jwt-Token", "Authorization", "File-Name"));

        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
