package com.example.chatserver.chat.config;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class StompHandler implements ChannelInterceptor {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Override
	public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		// 모든 요청에서 검증할 필요 없고 커넥트 요청 시에만 토큰 유효성 검증
		if(StompCommand.CONNECT == accessor.getCommand()) {
			System.out.println("connect 요청 시 토큰 유효성 검증");
			String bearerToken = accessor.getFirstNativeHeader("Authorization");
			String token = bearerToken.substring(7);

			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();

			System.out.println("토큰 검증 완료");
		}

		return message;
	}

}