package com.joo.real_world.security.config

import com.joo.real_world.common.config.ApiController
import com.joo.real_world.security.filter.ExceptionHandlerFilter
import com.joo.real_world.security.filter.JwtFilter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebMvc
class WebMvcSecurityConfig(
    private val jwtFilter: JwtFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix("/api") { it.isAnnotationPresent(ApiController::class.java) }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(*AUTH_EXCLUDE_PATH.toTypedArray()).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    companion object {
        private val AUTH_EXCLUDE_PATH = listOf(
            "api/users",
            "api/users/login"
        )
    }
}