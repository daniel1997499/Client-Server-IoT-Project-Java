package com.example.demo.Security;

import com.example.demo.Model.Device;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60 * 1000; //1hour

//    @Value("${jwt.secret}")
    private String secret = "312re0dscd40=3@!#!#RASERT$T4twfdw3jrw385@";

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(Map<String, Object> claims, Device device) {
        return doGenerateToken(claims, device.getName());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setIssuer(subject)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, Device device) {
        final Long deviceId = Long.valueOf(getAllClaimsFromToken(token).get("deviceId").toString());
        final String name = getAllClaimsFromToken(token).getSubject();
        final String address = getAllClaimsFromToken(token).get("address").toString();
        return (deviceId.equals(device.getID()) && name.equals(device.getName()) && address.equals(device.getAddress()) && !isTokenExpired(token));
    }
}
