package com.atome.bot.repository;

import com.atome.bot.entity.FeedbackRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRecordRepository extends JpaRepository<FeedbackRecord, String> {
    List<FeedbackRecord> findByAgentIdOrderByCreatedAtDesc(String agentId);
}
