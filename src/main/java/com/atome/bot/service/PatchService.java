package com.atome.bot.service;

import com.atome.bot.dto.request.FeedbackRequest;
import com.atome.bot.dto.response.FeedbackResponse;
import com.atome.bot.entity.AnswerPatch;
import com.atome.bot.entity.FeedbackRecord;
import com.atome.bot.entity.PatchRule;
import com.atome.bot.repository.AnswerPatchRepository;
import com.atome.bot.repository.FeedbackRecordRepository;
import com.atome.bot.repository.PatchRuleRepository;
import com.atome.bot.util.TextMatchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatchService {

    private final FeedbackRecordRepository feedbackRecordRepository;
    private final PatchRuleRepository patchRuleRepository;
    private final AnswerPatchRepository answerPatchRepository;

    public void saveFeedback(String agentId, FeedbackRequest request) {
        FeedbackRecord feedback = FeedbackRecord.builder()
                .id(UUID.randomUUID().toString())
                .agentId(agentId)
                .conversationId(request.getConversationId())
                .question(request.getQuestion())
                .wrongAnswer(request.getWrongAnswer())
                .correctAnswer(request.getCorrectAnswer())
                .issueType(request.getIssueType())
                .createdAt(LocalDateTime.now())
                .build();
        feedbackRecordRepository.save(feedback);

        PatchRule patchRule = PatchRule.builder()
                .id(UUID.randomUUID().toString())
                .agentId(agentId)
                .questionPattern(request.getQuestion())
                .correctAnswer(request.getCorrectAnswer())
                .createdAt(LocalDateTime.now())
                .build();
        patchRuleRepository.save(patchRule);
    }

    public Optional<String> findPatchedAnswer(String agentId, String question) {
        List<PatchRule> rules = patchRuleRepository.findByAgentId(agentId);
        return rules.stream()
                .filter(rule -> TextMatchUtil.looselyMatches(question, rule.getQuestionPattern()))
                .map(PatchRule::getCorrectAnswer)
                .findFirst();
    }

    public void applyPatchFromFeedback(String feedbackId) {
        FeedbackRecord feedback = feedbackRecordRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));

        if (feedback.getCorrectAnswer() == null || feedback.getCorrectAnswer().isBlank()) {
            throw new IllegalArgumentException("Correct answer is required to apply autofix");
        }

        AnswerPatch patch = AnswerPatch.builder()
                .id(UUID.randomUUID().toString())
                .agentId(feedback.getAgentId())
                .sourceFeedbackId(feedback.getId())
                .question(feedback.getQuestion())
                .correctedAnswer(feedback.getCorrectAnswer())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        answerPatchRepository.save(patch);
    }


    public List<FeedbackResponse> getFeedback(String agentId) {
        return feedbackRecordRepository.findByAgentIdOrderByCreatedAtDesc(agentId)
                .stream()
                .map(item -> FeedbackResponse.builder()
                        .id(item.getId())
                        .question(item.getQuestion())
                        .wrongAnswer(item.getWrongAnswer())
                        .correctAnswer(item.getCorrectAnswer())
                        .issueType(item.getIssueType())
                        .createdAt(item.getCreatedAt())
                        .build())
                .toList();
    }
}
