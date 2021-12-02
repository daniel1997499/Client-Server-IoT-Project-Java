package com.example.demo.Security;

import com.example.demo.Model.Device;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60 * 1000; //1hour

//    @Value("${jwt.secret}")
    private String secret = "sgfd23#@%%!f3rgt43afahrthjsdajfgj0-43sdggh4%@^$%#!@#%#^U*&&(%($@$##%&#%^U#%^rfhjkyurw2$#%^e^@$#wfg%^&ffg$&#!bf.?+)_%$@znxvcbr34";
    Base64UrlCodec codec = new Base64UrlCodec();

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(codec.encode(secret)).parseClaimsJws(token).getBody();
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
                .signWith(SignatureAlgorithm.HS256, codec.encode(secret))
                .compact();
    }

    public Boolean validateToken(String token, Device device) {
        final Long deviceId = Long.valueOf(getAllClaimsFromToken(token).get("deviceId").toString());
        final String name = getAllClaimsFromToken(token).getSubject();
        final String address = getAllClaimsFromToken(token).get("address").toString();
        return (deviceId.equals(device.getID()) && name.equals(device.getName()) && address.equals(device.getAddress()) && !isTokenExpired(token));
    }
}
