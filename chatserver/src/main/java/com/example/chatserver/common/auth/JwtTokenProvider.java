package com.example.chatserver.common.auth;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private static final long MILLISECONDS_PER_MINUTE = 60_000L;

	private final int accessTokenExpirationMinutes;
	private final Key signingKey;

	public JwtTokenProvider(
		@Value("${jwt.secretKey}") String secretKey,
		@Value("${jwt.expiration}") int accessTokenExpirationMinutes
	) {
		this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
		this.signingKey = new SecretKeySpec(
			Base64.getDecoder().decode(secretKey),
			SignatureAlgorithm.HS512.getJcaName()
		);
	}

	public String createToken(String email, String role) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);

		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + accessTokenExpirationMinutes * MILLISECONDS_PER_MINUTE); // 만료 시간 계산

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expirationDate)
			.signWith(signingKey)
			.compact();
	}
}