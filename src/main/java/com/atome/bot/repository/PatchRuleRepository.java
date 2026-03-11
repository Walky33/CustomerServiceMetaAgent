package com.atome.bot.repository;

import com.atome.bot.entity.PatchRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatchRuleRepository extends JpaRepository<PatchRule, String> {
    List<PatchRule> findByAgentId(String agentId);
}
