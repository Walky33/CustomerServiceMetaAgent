package com.atome.bot.controller;

import com.atome.bot.dto.request.FeedbackRequest;
import com.atome.bot.dto.response.FeedbackResponse;
import com.atome.bot.service.PatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents/{agentId}/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final PatchService patchService;

    @PostMapping
    public ResponseEntity<Void> saveFeedback(@PathVariable String agentId,
                                             @Valid @RequestBody FeedbackRequest request) {
        patchService.saveFeedback(agentId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getFeedback(@PathVariable String agentId) {
        return ResponseEntity.ok(patchService.getFeedback(agentId));
    }
}
