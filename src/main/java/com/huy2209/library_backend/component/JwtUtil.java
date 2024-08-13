package com.huy2209.library_backend.component;

import com.huy2209.library_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String createToken(User user) throws Exception{
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("role",user.getRole());

        try{
            return Jwts
                        .builder()
                        .setClaims(claims)
                        .setSubject(user.getEmail())
                        .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                        .signWith(getSignKey(), SignatureAlgorithm.HS256)
                        .compact();
        }
        catch (Exception e){
            throw new Exception("cannot create token");
        }
    }

    private Key getSignKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validToken(String token, User user){
        String email = extractEmail(token);
        return (email.equals(user.getEmail()) && !isTokenExpired(token));
    }
}
