package com.atome.bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationLog {

    @Id
    private String id;

    private String agentId;
    private String conversationId;

    @Lob
    private String userQuestion;

    @Lob
    private String modelAnswer;

    private Double confidence;
    private boolean escalated;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
