package com.cemetery.web.config;

import com.cemetery.web.filter.JwtAuthenticationFilter;
import com.cemetery.web.handler.JwtAccessDeniedHandler;
import com.cemetery.web.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF
            .csrf().disable()
            // 禁用Session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 配置异常处理
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
            // 配置请求权限
            .authorizeRequests()
            // 允许OPTIONS请求
            .antMatchers("OPTIONS", "/**").permitAll()
            // 允许访问登录接口（放在最前面）
            .antMatchers("/api/auth/**").permitAll()
            // 允许访问接口文档
            .antMatchers(
                "/doc.html",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/favicon.ico"
            ).permitAll()
            // 临时放行服务商管理API用于测试
            .antMatchers("/api/service-provider/**").permitAll()
            .antMatchers("/api/provider-service/**").permitAll()
            .antMatchers("/api/service-feedback/**").permitAll()
            // 管理员接口权限
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            // 家属接口权限
            .antMatchers("/api/family/**").hasAnyRole("ADMIN", "FAMILY")
            // 服务商接口权限
            .antMatchers("/api/provider/**").hasAnyRole("ADMIN", "PROVIDER")
            // 其他请求需要认证
            .anyRequest().authenticated()
            .and()
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 禁用默认登录页
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }
}
