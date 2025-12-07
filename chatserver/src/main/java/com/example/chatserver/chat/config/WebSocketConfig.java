// package com.example.chatserver.chat.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.socket.config.annotation.EnableWebSocket;
// import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
// import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
// import lombok.RequiredArgsConstructor;
//
// @Configuration
// @EnableWebSocket
// @RequiredArgsConstructor
// public class WebSocketConfig implements WebSocketConfigurer {
//
// 	private final SimpleWebSocketHandler simpleWebSocketHandler;
//
// 	@Override
// 	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
// 		// "/connect" url로 websocket 연결 요청이 들어오면 핸들러 클래스가 처리
// 		registry.addHandler(simpleWebSocketHandler, "/connect")
// 			// securityconfig에서의 cors 예외는 http 요청에 대한 예외이므로
// 			// websocket 프로토콜에 대한 예외에 대해서는 별도로 정의해주어야 한다.
// 			.setAllowedOrigins("http://localhost:3000");
// 	}
// }