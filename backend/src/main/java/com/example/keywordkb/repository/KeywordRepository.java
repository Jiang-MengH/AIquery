package com.example.keywordkb.repository;

import com.example.keywordkb.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByGroupId(Long groupId);
}