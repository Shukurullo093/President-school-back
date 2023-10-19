//package com.example.president_school.security;
//
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Optional;
//
//public final class SecurityUtils {
//    private SecurityUtils(){}
//
//    public static Optional<String> getCurrentUsername(){
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        return Optional.ofNullable(securityContext.getAuthentication())
//                .map(authentication -> {
//                    if (authentication.getPrincipal() instanceof UserDetails userDetails){
//                        return userDetails.getUsername();
//                    }
//                    else if (authentication.getPrincipal() instanceof String){
//                        return (String) authentication.getPrincipal();
//                    }
//                    return null;
//                });
//    }
//}
