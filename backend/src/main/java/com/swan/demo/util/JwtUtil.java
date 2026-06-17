package com.swan.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET =
            "abcdefghijklmnopqrstuvwxyz123456";

    private static final SecretKey Key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes()
            );

    public static String createToken(Long userId) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                            + 86400000
                        )
                )
                .signWith(Key)
                .compact();
    }

    public static Long parseToken(String token) {

        Claims claims =
                Jwts.parser()
                        .verifyWith(Key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}
