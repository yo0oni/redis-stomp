package com.example.chatserver.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {

	@MessageMapping("/{roomId}") // 클라이언트에서 특정 /publish/roodId 형태로 메시지를 발행 시 MessageMapping이 수신
	@SendTo("/topic/{roomId}") // 해당 rootId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
	// DestinationVariable 은 @MessageMapping 어노테이션으로 정의한 Websocket Controller 내에서만 사용
	public String sendMessage(@DestinationVariable Long roomId, String message) {
		System.out.println(message);
		return message;
	}
}