package com.ar.autocomplete.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.autocomplete.service.TrieService;

@RestController
@RequestMapping("/")
public class AutoCompleteController {

    private final TrieService trieService;

    @Autowired
    public AutoCompleteController(TrieService trieService) {
        this.trieService = trieService;
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam(required = true) String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prefix must not be empty.");
        }

        List<String> suggestions = trieService.searchByPrefix(prefix);
        return ResponseEntity.ok(suggestions);
    }
}
