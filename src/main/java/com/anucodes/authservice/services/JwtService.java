package com.anucodes.authservice.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_kEY = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";


    //Returns the username extracted from the jwt claims
    public String extractUsername(String token){
        return extractclaimInformation(token, Claims::getSubject);
    }

    //Returns the username extracted from the jwt claims
    public Date extractExpiration(String token){
        return extractclaimInformation(token, Claims::getExpiration);
    }

    //Check that the token is expired time.
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Checking the validity of the token.
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //The function is used to extract the information from the claims extracted.
    public <T> T extractclaimInformation(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    //The function will generate the token.
    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }


    //The function will create a jwt token.
    public String createToken(Map<String, Object> claims, String username){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    //The function is used to extract the claims from the String token.
    public Claims extractClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Getting the signed key in the claims
    public Key getSignKey(){
        byte[] keys = Decoders.BASE64.decode(SECRET_kEY);
        return Keys.hmacShaKeyFor(keys);
    }
}
