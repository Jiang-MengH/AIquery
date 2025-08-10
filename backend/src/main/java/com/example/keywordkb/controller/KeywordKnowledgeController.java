package com.example.keywordkb.controller;

import com.example.keywordkb.entity.KeywordKnowledge;
import com.example.keywordkb.service.KeywordKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keyword-knowledge")
public class KeywordKnowledgeController {
    @Autowired
    private KeywordKnowledgeService service;

    @GetMapping
    public List<KeywordKnowledge> listAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PostMapping
    public KeywordKnowledge add(@RequestBody KeywordKnowledge knowledge) {
        return service.save(knowledge);
    }

    @PutMapping("/{id}")
    public KeywordKnowledge update(@PathVariable Long id, @RequestBody KeywordKnowledge knowledge) {
        knowledge.setId(id);
        return service.save(knowledge);
    }
}