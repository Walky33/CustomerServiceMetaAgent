package com.atome.bot.service;

import com.atome.bot.dto.request.CreateAgentRequest;
import com.atome.bot.dto.request.InstructionRequest;
import com.atome.bot.dto.response.AgentResponse;
import com.atome.bot.entity.Agent;
import com.atome.bot.enums.AgentStatus;
import com.atome.bot.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentResponse createAgent(CreateAgentRequest request) {
        Agent agent = Agent.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .status(AgentStatus.DRAFT)
                .instructions("Answer using uploaded text files only. Escalate if not found.")
                .createdAt(LocalDateTime.now())
                .build();

        agentRepository.save(agent);

        return AgentResponse.builder()
                .id(agent.getId())
                .name(agent.getName())
                .status(agent.getStatus().name())
                .build();
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public void updateInstructions(String agentId, InstructionRequest request) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
        agent.setInstructions(request.getInstruction());
        agentRepository.save(agent);
    }

    public Agent getAgent(String agentId) {
        System.out.println("agentId: " + agentId);
        return agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
    }
}
