package com.atome.bot.service;

import com.atome.bot.dto.response.DocumentUploadResponse;
import com.atome.bot.entity.KnowledgeDocument;
import com.atome.bot.repository.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final KnowledgeDocumentRepository knowledgeDocumentRepository;

    @Value("${storage.upload-dir}")
    private String uploadDir;

    public DocumentUploadResponse uploadTextFile(String agentId, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".txt")) {
            throw new IllegalArgumentException("Only .txt files are allowed in this simple college project version");
        }

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        String storedName = UUID.randomUUID() + "_" + originalName;
        Path target = dir.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String content = Files.readString(target, StandardCharsets.UTF_8);

        KnowledgeDocument document = KnowledgeDocument.builder()
                .id(UUID.randomUUID().toString())
                .agentId(agentId)
                .fileName(originalName)
                .storagePath(target.toString())
                .content(content)
                .uploadedAt(LocalDateTime.now())
                .build();

        knowledgeDocumentRepository.save(document);

        return DocumentUploadResponse.builder()
                .documentId(document.getId())
                .fileName(document.getFileName())
                .message("Text file uploaded successfully")
                .build();
    }

    public List<KnowledgeDocument> findDocuments(String agentId) {
        return knowledgeDocumentRepository.findByAgentIdOrderByUploadedAtDesc(agentId);
    }
}
