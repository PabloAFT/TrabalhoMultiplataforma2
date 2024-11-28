package com.trabalho.multi.trabalhomulti;

import java.util.Date;
import java.util.UUID;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Charsets;
import com.google.auth.oauth2.JwtClaims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.impl.DefaultClaims;

@Service
public class JwtService {

    public boolean validateToken(String token, UserDetails userDetails) {

        try {

            JwtParser parser = getParser();
            Claims data = parser.parseSignedClaims(token).getPayload();
            Date expiration = data.getExpiration();

            if (expiration.after(new Date())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String extractUsername(String token) {
        JwtParser parser = getParser();
        // parser.parseUnsecuredContent(token).accept(null);
        return parser.parseSignedClaims(token).getPayload().getSubject();

    }

    public UUID extractProviderId(String token) {
        JwtParser parser = getParser();
        return UUID.fromString((String) parser.parseSignedClaims(token).getPayload().get("provider"));
    }

    private JwtParser getParser() {
        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor("RastroMasterKey053y19472613981e21e124123rdfg3145".getBytes(Charsets.UTF_8)))
                .build();
    }

    public String generateJwtToken(String username, UUID providerId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 86400000); // Token expires in 1 day

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .claim("provider", providerId)
                .header().add("typ", "JWT").add("alg", "HS384").and()
                .signWith(
                        Keys.hmacShaKeyFor("RastroMasterKey053y19472613981e21e124123rdfg3145".getBytes(Charsets.UTF_8)))
                .compact();
    }

}
