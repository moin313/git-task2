package com.spring.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class Jwtutil {
    @Value("${app.secret}")
    private String secret;

    public boolean validateToken(String token,String username){
        String tokenUserName=getUsername(token);
        return (username.equals(tokenUserName)&&!isTokenExepired(token));
    }

    public  boolean isTokenExepired(String token){
        Date expDate=getExpDate(token);
        return expDate.before(new Date(System.currentTimeMillis()));
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public  Date getExpDate(String token){
        return  getClaims(token).getExpiration();
    }

    public Claims getClaims(String token){
        return  Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
//Generate token
    public String generateToken(String subject ){
        String token =  Jwts.builder().setSubject(subject)
                .setIssuer("Moin")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(15)))
                .signWith(SignatureAlgorithm.HS512,secret.getBytes())
                .compact();
        System.out.println("GENERATED TOKEN iS "+token );
        return token;
    }
}
