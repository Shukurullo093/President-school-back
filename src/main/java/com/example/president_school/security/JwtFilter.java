//package com.example.president_school.security;
//
//import com.example.president_school.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.lang.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    @Autowired
//    JwtProvider jwtProvider;
//    @Autowired
//    AuthService authService;
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
//                                    @NonNull HttpServletResponse httpServletResponse,
//                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String authorization = httpServletRequest.getHeader("Authorization");
//        System.out.println(authorization);
//        if (authorization != null && authorization.startsWith("Bearer")) {
//            authorization = authorization.substring(7);
//            String phone = jwtProvider.getPhoneFromToken(authorization);
//            System.out.println("filter " + phone);
//            if (phone != null) {
//                UserDetails userDetails = authService.loadUserByUsername(phone);
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//}
