package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ChatRoomsApi;
import com.heylocal.traveler.dto.ChatRoomDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ChatRooms")
@RestController
public class ChatRoomController implements ChatRoomsApi {
	/**
	 * @return
	 */
	@Override
	public List<ChatRoomDto.ChatRoomResponse> getChatRooms() {
		return null;
	}

	/**
	 * @param chatroomId
	 * @return
	 */
	@Override
	public List<ChatRoomDto.ChatMessageResponse> getChatMessages(long chatroomId) {
		return null;
	}
}
