package com.atome.bot.controller;

import com.atome.bot.service.PatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AutoFixController {

    private final PatchService patchService;

    @PostMapping("/{id}/autofix")
    public ResponseEntity<Void> applyAutoFix(@PathVariable String id) {
        patchService.applyPatchFromFeedback(id);
        return ResponseEntity.ok().build();
    }
}