package com.atome.bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patch_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchRule {

    @Id
    private String id;

    private String agentId;

    @Lob
    private String questionPattern;

    @Lob
    private String correctAnswer;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
