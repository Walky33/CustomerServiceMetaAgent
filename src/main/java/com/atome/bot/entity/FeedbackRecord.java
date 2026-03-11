package com.atome.bot.entity;

import com.atome.bot.enums.IssueType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRecord {

    @Id
    private String id;

    private String agentId;
    private String conversationId;

    @Lob
    private String question;

    @Lob
    private String wrongAnswer;

    @Lob
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
