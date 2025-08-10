package com.example.keywordkb.service.impl;

import com.example.keywordkb.entity.KeywordKnowledge;
import com.example.keywordkb.repository.KeywordKnowledgeRepository;
import com.example.keywordkb.service.KeywordKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordKnowledgeServiceImpl implements KeywordKnowledgeService {
    @Autowired
    private KeywordKnowledgeRepository repository;

    @Override
    public List<KeywordKnowledge> findAll() {
        return repository.findAll();
    }

    @Override
    public KeywordKnowledge save(KeywordKnowledge knowledge) {
        return repository.save(knowledge);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public KeywordKnowledge findById(Long id) {
        Optional<KeywordKnowledge> opt = repository.findById(id);
        return opt.orElse(null);
    }
}