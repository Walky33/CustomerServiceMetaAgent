package com.atome.bot.repository;

import com.atome.bot.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, String> {
    List<KnowledgeDocument> findByAgentIdOrderByUploadedAtDesc(String agentId);
}
