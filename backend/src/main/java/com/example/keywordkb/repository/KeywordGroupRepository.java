package com.example.keywordkb.repository;

import com.example.keywordkb.entity.KeywordGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordGroupRepository extends JpaRepository<KeywordGroup, Long> {
    boolean existsByName(String name);
}