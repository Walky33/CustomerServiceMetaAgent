package com.atome.bot.dto.response;

import com.atome.bot.enums.IssueType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private String id;

    private String question;

    private String wrongAnswer;

    private String correctAnswer;

    private IssueType issueType;

    private LocalDateTime createdAt;
}