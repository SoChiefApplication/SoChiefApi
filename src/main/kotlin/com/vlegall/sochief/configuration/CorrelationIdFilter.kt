package com.vlegall.sochief.configuration

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorrelationIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cid = request.getHeader("X-Correlation-ID")?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        MDC.put("cid", cid)
        response.setHeader("X-Correlation-ID", cid)
        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove("cid")
        }
    }
}
