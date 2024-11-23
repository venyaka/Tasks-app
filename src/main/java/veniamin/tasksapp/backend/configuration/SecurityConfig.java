package veniamin.tasksapp.backend.configuration;

import org.springframework.http.HttpMethod;
import veniamin.tasksapp.backend.filter.ExceptionHandlerFilter;
import veniamin.tasksapp.backend.filter.RefreshTokenFilter;
import veniamin.tasksapp.backend.filter.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    private final RefreshTokenFilter refreshTokenFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PATCH", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        configuration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return
                httpSecurity.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                        .addFilterAfter(refreshTokenFilter, JwtTokenFilter.class)
                        .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling(Customizer.withDefaults())
                        .authorizeHttpRequests(c ->
                                c.requestMatchers("/admin/metrics/**").hasAuthority("ADMIN")
                                        .requestMatchers(antMatcher(HttpMethod.GET, "/tasks/**")).authenticated()
                                        .requestMatchers("/tasks/**").hasAuthority("ADMIN"))


                        .cors(Customizer.withDefaults())
                        .csrf(csrf -> csrf.disable())
                        .logout(c -> c.invalidateHttpSession(true)
                                .clearAuthentication(true))
                        .sessionManagement(c -> c.maximumSessions(1))
                        .authorizeHttpRequests((requests) -> requests //*
                        .anyRequest().permitAll()) //*
                        .build();
    }

//    @Bean
//    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
//        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
//    }
}