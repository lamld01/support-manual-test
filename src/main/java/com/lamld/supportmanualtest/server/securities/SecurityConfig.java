package com.lamld.supportmanualtest.server.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public AuthedEntryPoint authEntryPoint() {
    return new AuthedEntryPoint();
  }

  @Bean
  public AuthFilter authFilter() {
    return new AuthFilter();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new SecurityService();
  }

  // Cấu hình SecurityFilterChain với CORS và bảo mật JWT
  @Bean
  public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)  // Tắt CSRF cho API REST
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "v1/manual-test/public/**",
                "v1/manual-test/internal/**",
                "v1/manual-test/docs/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Sử dụng trạng thái không lưu trữ (stateless) cho JWT
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(authEntryPoint())
            .accessDeniedHandler(authEntryPoint())
        )
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);  // Thêm AuthFilter trước khi xử lý đăng nhập

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
