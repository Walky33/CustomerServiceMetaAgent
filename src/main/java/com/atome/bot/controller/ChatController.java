package com.atome.bot.controller;

import com.atome.bot.dto.request.ChatRequest;
import com.atome.bot.dto.response.ChatResponse;
import com.atome.bot.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents/{agentId}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@PathVariable String agentId,
                                             @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.chat(agentId, request));
    }
}
