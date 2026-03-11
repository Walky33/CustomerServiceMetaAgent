package com.atome.bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer_patches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerPatch {

    @Id
    private String id;

    private String agentId;

    private String sourceFeedbackId;

    private String question;

    @jakarta.persistence.Lob
    private String correctedAnswer;

    private LocalDateTime createdAt;

    private boolean active;
}