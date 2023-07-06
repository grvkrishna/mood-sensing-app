package com.grv.moodsensingapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

public class TestUtils {


    private static long validityInMilliseconds = 3600000; // 1h


    public static SecretKey secretKey() {
        var secret = Base64.getEncoder().encodeToString("abcdefghijklmnopqrstuvwxyz".getBytes());
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    static Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");

    static Date now = new Date();
    static Date validity = new Date(now.getTime() + validityInMilliseconds);
    public static String getJwt(){
          Claims claims = Jwts.claims().setSubject("testadmin");
          claims.put("roles",authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
