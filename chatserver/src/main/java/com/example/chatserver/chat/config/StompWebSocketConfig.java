package com.example.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // STOMP를 의미
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/connect")
			.setAllowedOrigins("http://localhost:3000")
			// ws://가 아닌 http:// 앤드포인트를 사용할 수 있게 해주는 sockJS 라이브러리를 사용
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// "/publish/{roomId}" 형태로 메시지를 발행해야 함을 설정
		// "/publish"로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체인 @MessageMapping 메소드로 라우팅
		registry.setApplicationDestinationPrefixes("/publish");
		// "/topic/{roomId}" 형태로 메시지를 수신해야 함을 설정
		registry.enableSimpleBroker("/topic");
	}

	// 웹소켓 요청 (connect, subscribe, disconnect) 등의 요청시에는 http header 등 http 메시지를 넣어줄 수 있고,
	// 이를 interceptor 를 통해 가로채 토큰 등을 검증할 수 있다.
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}