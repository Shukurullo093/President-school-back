//package com.example.president_school.config;
//
//import com.example.president_school.entity.enums.Role;
//import com.example.president_school.jwt.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableMethodSecurity
//public class SecurityConfiguration1 {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/home", "/assets/**",
//                        "/css/**", "/js/**", "/icons/**", "/images/**", "/vendor/**", "/auth/login").permitAll()
//                .antMatchers("/api/admin/dashboard").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login").permitAll()
//                .and()
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//
//    private AuthenticationSuccessHandler authenticationSuccessHandler(){
//        AuthenticationSuccessHandler handler = new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request,
//                                                HttpServletResponse response,
//                                                Authentication authentication)
//                    throws IOException, ServletException {
//                String urlContext = request.getContextPath();
//                System.out.println(urlContext);
//                for (GrantedAuthority authority : authentication.getAuthorities()){
//                    if ("ROLE_ADMIN".equals(authority.getAuthority())){
//                        response.sendRedirect(urlContext + "/api/admin/dashboard");
//                        break;
//                    } else if ("ROLE_USER".equals(authority.getAuthority())){
//                        response.sendRedirect(urlContext + "/user/test");
//                        break;
//                    }
//                }
//            }
//        };
//        return handler;
//    }
//}
