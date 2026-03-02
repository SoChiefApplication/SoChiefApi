package fr.vlegall.sochief.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityApiProperties props;

    public SecurityConfig(SecurityApiProperties props) {
        this.props = props;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**"
                    ).permitAll();
                    auth.requestMatchers("/actuator/**").permitAll();
                    auth.requestMatchers("/api/**").authenticated();
                    auth.anyRequest().permitAll();
                })
                .addFilterBefore(new ApiKeyFilter(props), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    static class ApiKeyFilter extends OncePerRequestFilter {
        private final SecurityApiProperties props;

        ApiKeyFilter(SecurityApiProperties props) {
            this.props = props;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
                throws ServletException, IOException {
            String header = request.getHeader("X-API-KEY");
            final String key = header == null ? "" : header.trim();
            if (!key.isBlank() && props.getKeys().stream().anyMatch(k -> k != null && !k.isBlank() && k.equals(key))) {
                ApiKeyAuthentication auth = new ApiKeyAuthentication(key);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        }
    }

    static class ApiKeyAuthentication extends AbstractAuthenticationToken {
        private final String key;

        ApiKeyAuthentication(String key) {
            super(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
            this.key = key;
            super.setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return key;
        }

        @Override
        public Object getPrincipal() {
            return "api-key";
        }
    }
}
