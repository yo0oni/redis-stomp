package com.example.chatserver.chat.service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.example.chatserver.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubSubService implements MessageListener {

	private static final String STOMP_TOPIC_PREFIX = "/topic/";

	@Qualifier("chatPubSub")
	private final StringRedisTemplate stringRedisTemplate;
	private final SimpMessageSendingOperations messageTemplate;

	public void publish(String channel, String message) {
		stringRedisTemplate.convertAndSend(channel, message);
	}

	@Override
	// pattern에는 topic의 이름의 패턴이 담겨있고, 이 패턴을 기반으로 다이나믹한 코딩
	public void onMessage(Message message, byte @Nullable [] pattern) {
		try {
			// 여기서 message를 받아 stomp topic으로 보내주어야 함
			String payload = new String(message.getBody());
			ObjectMapper objectMapper = new ObjectMapper();
			ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
			String destination = STOMP_TOPIC_PREFIX + chatMessageDto.getRoomId();
			messageTemplate.convertAndSend(destination, chatMessageDto.getMessage());
		} catch (Exception e) {
			log.error("Redis 메시지 처리 중 오류 발생", e);
		}
	}
}