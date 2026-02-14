package fr.vlegall.sochief.configuration

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val props: SecurityApiProperties
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                it.requestMatchers("/actuator/**").permitAll()
                it.requestMatchers("/api/**").authenticated()
                it.anyRequest().permitAll()
            }
            .addFilterBefore(ApiKeyFilter(props), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}

private class ApiKeyFilter(
    private val props: SecurityApiProperties
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val key = request.getHeader("X-API-KEY")?.trim().orEmpty()
        if (key.isNotBlank() && props.keys.any { it.isNotBlank() && it == key }) {
            val auth = ApiKeyAuthentication(key)
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }
}

private class ApiKeyAuthentication(
    private val key: String
) : AbstractAuthenticationToken(listOf(SimpleGrantedAuthority("ROLE_ADMIN"))) {
    init {
        super.setAuthenticated(true)
    }

    override fun getCredentials(): Any = key
    override fun getPrincipal(): Any = "api-key"
}
