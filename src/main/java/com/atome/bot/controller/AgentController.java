package com.atome.bot.controller;

import com.atome.bot.dto.request.CreateAgentRequest;
import com.atome.bot.dto.request.InstructionRequest;
import com.atome.bot.dto.response.AgentResponse;
import com.atome.bot.entity.Agent;
import com.atome.bot.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<AgentResponse> create(@Valid @RequestBody CreateAgentRequest request) {
        return ResponseEntity.ok(agentService.createAgent(request));
    }

    @GetMapping
    public ResponseEntity<List<Agent>> list() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }

    @PostMapping("/{agentId}/instructions")
    public ResponseEntity<Void> updateInstructions(@PathVariable String agentId,
                                                   @Valid @RequestBody InstructionRequest request) {
        agentService.updateInstructions(agentId, request);
        return ResponseEntity.ok().build();
    }
}
