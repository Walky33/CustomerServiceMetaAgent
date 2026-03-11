package com.atome.bot.repository;

import com.atome.bot.entity.ConversationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationLogRepository extends JpaRepository<ConversationLog, String> {
}
