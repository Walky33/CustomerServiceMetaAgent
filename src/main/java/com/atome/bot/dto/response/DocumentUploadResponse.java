package com.atome.bot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentUploadResponse {
    private String documentId;
    private String fileName;
    private String message;
}
