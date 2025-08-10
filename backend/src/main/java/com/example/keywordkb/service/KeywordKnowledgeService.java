package com.example.keywordkb.service;

import com.example.keywordkb.entity.KeywordKnowledge;
import java.util.List;

public interface KeywordKnowledgeService {
    List<KeywordKnowledge> findAll();
    KeywordKnowledge save(KeywordKnowledge knowledge);
    void deleteById(Long id);
    KeywordKnowledge findById(Long id);
}