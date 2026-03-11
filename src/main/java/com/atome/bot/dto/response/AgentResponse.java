package com.atome.bot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentResponse {
    private String id;
    private String name;
    private String status;
}
