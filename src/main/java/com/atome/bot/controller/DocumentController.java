package com.atome.bot.controller;

import com.atome.bot.dto.response.DocumentUploadResponse;
import com.atome.bot.entity.KnowledgeDocument;
import com.atome.bot.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/agents/{agentId}/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentUploadResponse> upload(@PathVariable String agentId,
                                                         @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(documentService.uploadTextFile(agentId, file));
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDocument>> list(@PathVariable String agentId) {
        return ResponseEntity.ok(documentService.findDocuments(agentId));
    }
}
