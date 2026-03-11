package com.atome.bot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstructionRequest {
    @NotBlank
    private String instruction;
}
