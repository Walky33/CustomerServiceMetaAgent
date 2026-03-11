package com.atome.bot.dto.request;

import com.atome.bot.enums.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {
    @NotBlank
    private String conversationId;
    @NotBlank
    private String question;
    @NotBlank
    private String wrongAnswer;
    @NotBlank
    private String correctAnswer;
    @NotNull
    private IssueType issueType;
}
