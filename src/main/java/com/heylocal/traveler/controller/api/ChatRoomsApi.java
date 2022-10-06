package com.heylocal.traveler.controller.api;

import com.heylocal.traveler.dto.ChatRoomDto.ChatMessageResponse;
import com.heylocal.traveler.dto.ChatRoomDto.ChatRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * packageName    : com.heylocal.traveler.controller.api
 * fileName       : ChatRoomsApi
 * author         : 우태균
 * date           : 2022/08/12
 * description    : 채팅 API 인터페이스
 */

@RequestMapping("/chatrooms")
public interface ChatRoomsApi {
    @Operation(summary = "채팅방 리스트 조회", description = "사용자가 참여한 채팅방 목록을 조회합니다.", tags = {"ChatRooms"})
    @GetMapping()
    List<ChatRoomResponse> getChatRooms();

    @Operation(summary = "채팅방 메시지 조회", description = "채팅방의 메시지 목록을 조회합니다.", tags = {"ChatRooms"})
    @GetMapping("/{chatroomId}/messages")
    List<ChatMessageResponse> getChatMessages(
        @Parameter(in = ParameterIn.PATH, description = "조회할 채팅방 id", required = true) @PathVariable long chatroomId
    );
}

