package com.example.keywordkb.repository;

import com.example.keywordkb.entity.KeywordKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KeywordKnowledgeRepository extends JpaRepository<KeywordKnowledge, Long> {
    List<KeywordKnowledge> findByKeywordId(Long keywordId);
}