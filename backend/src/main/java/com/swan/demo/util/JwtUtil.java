package com.swan.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGH".getBytes());

    public static String createToken(Map<String, Object> claims) {

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
