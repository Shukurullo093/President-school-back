//package com.example.president_school.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtProvider {
//    private static final long expireTime = 1000 * 60 * 60 * 2;
//    private static final String secretKey = "==========securesecure====================securesecure====================securesecure==========";
//
//    public String generateToken(String phone) {
//        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
//        String token = Jwts
//                .builder()
//                .setSubject(phone)
//                .setIssuedAt(new Date())
//                .setExpiration(expireDate)
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//        return token;
//    }
//
//    public String getPhoneFromToken(String token) {
//        try {
//            String phone = Jwts
//                    .parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//            return phone;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public boolean tokenCheck(String token){
//        Jwts
//                .parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//        return true;
//    }
//}