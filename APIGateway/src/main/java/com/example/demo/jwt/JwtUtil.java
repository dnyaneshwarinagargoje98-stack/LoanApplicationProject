package com.example.demo.jwt;
import java.util.Date;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET = "mySecretKeymySecretKeymySecretKeymySecretKey123";
	private static final long EXPIRATION = 60 * 60 * 1000;

	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();
	}

	public Claims validateAndGetClaims(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
}