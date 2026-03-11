package com.atome.bot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private String answer;
    private String source;
    private Double confidence;
    private boolean escalated;
}
