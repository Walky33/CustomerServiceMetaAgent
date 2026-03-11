package com.atome.bot.service;

import com.atome.bot.dto.request.ChatRequest;
import com.atome.bot.dto.response.ChatResponse;
import com.atome.bot.entity.Agent;
import com.atome.bot.entity.ConversationLog;
import com.atome.bot.entity.KnowledgeDocument;
import com.atome.bot.repository.ConversationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final AgentService agentService;
    private final DocumentService documentService;
    private final PatchService patchService;
    private final ConversationLogRepository conversationLogRepository;

    @Value("${agent.confidence-threshold}")
    private double confidenceThreshold;

    public ChatResponse chat(String agentId, ChatRequest request) {
        Agent agent = agentService.getAgent(agentId);

        Optional<String> patchedAnswer = patchService.findPatchedAnswer(agentId, request.getQuestion());
        if (patchedAnswer.isPresent()) {
            return saveAndReturn(agentId, request, patchedAnswer.get(), 0.95, false, "patched-answer");
        }

        List<KnowledgeDocument> documents = documentService.findDocuments(agentId);
        String userQuestion = request.getQuestion();

        for (KnowledgeDocument document : documents) {
            String faqAnswer = findAnswerFromFaq(document.getContent(), userQuestion);
            if (faqAnswer != null) {
                return saveAndReturn(agentId, request, faqAnswer, 0.90, false, document.getFileName());
            }
        }

        for (KnowledgeDocument document : documents) {
            String fallbackLineAnswer = findBestMatchingLine(document.getContent(), userQuestion, document.getFileName());
            if (fallbackLineAnswer != null) {
                return saveAndReturn(agentId, request, fallbackLineAnswer, 0.70, false, document.getFileName());
            }
        }

        String fallback = "I could not find a supported answer in the uploaded text files. Please escalate to a human agent.";
        return saveAndReturn(agentId, request, fallback, 0.20, true, "No matching source");
    }

    private String findAnswerFromFaq(String content, String userQuestion) {
        String[] lines = content.split("\\R");

        for (int i = 0; i < lines.length; i++) {
            String current = lines[i].trim();

            if (!current.startsWith("Q:")) {
                continue;
            }

            String storedQuestion = current.substring(2).trim();

            if (isQuestionMatch(userQuestion, storedQuestion)) {
                for (int j = i + 1; j < lines.length; j++) {
                    String next = lines[j].trim();

                    if (next.isEmpty()) {
                        continue;
                    }

                    if (next.startsWith("A:")) {
                        return next.substring(2).trim();
                    }

                    if (next.startsWith("Q:")) {
                        break;
                    }
                }
            }
        }

        return null;
    }

    private String findBestMatchingLine(String content, String userQuestion, String fileName) {
        String[] lines = content.split("\\R");
        String lowerQuestion = normalize(userQuestion);

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && matchesQuestion(lowerQuestion, normalize(trimmed))) {
                return "Based on uploaded file '" + fileName + "': " + trimmed;
            }
        }

        return null;
    }

    private boolean isQuestionMatch(String userQuestion, String storedQuestion) {
        String q1 = normalize(userQuestion);
        String q2 = normalize(storedQuestion);

        if (q1.equals(q2)) {
            return true;
        }

        if (q1.contains(q2) || q2.contains(q1)) {
            return true;
        }

        return matchesQuestion(q1, q2);
    }

    private String normalize(String text) {
        return text == null ? "" : text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean matchesQuestion(String question, String line) {
        String[] words = question.split("\\s+");
        int count = 0;

        for (String word : words) {
            if (word.length() > 3 && line.contains(word)) {
                count++;
            }
        }

        return count >= 2;
    }

    private ChatResponse saveAndReturn(String agentId,
                                       ChatRequest request,
                                       String answer,
                                       double confidence,
                                       boolean escalated,
                                       String source) {

        ConversationLog log = ConversationLog.builder()
                .id(UUID.randomUUID().toString())
                .agentId(agentId)
                .conversationId(request.getConversationId())
                .userQuestion(request.getQuestion())
                .modelAnswer(answer)
                .confidence(confidence)
                .escalated(escalated || confidence < confidenceThreshold)
                .createdAt(LocalDateTime.now())
                .build();

        conversationLogRepository.save(log);

        return ChatResponse.builder()
                .answer(answer)
                .source(source)
                .confidence(confidence)
                .escalated(escalated || confidence < confidenceThreshold)
                .build();
    }
}