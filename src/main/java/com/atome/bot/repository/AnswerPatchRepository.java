package com.atome.bot.repository;

import com.atome.bot.entity.AnswerPatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerPatchRepository extends JpaRepository<AnswerPatch, String> {
    List<AnswerPatch> findByAgentIdAndActiveTrueOrderByCreatedAtDesc(String agentId);
}