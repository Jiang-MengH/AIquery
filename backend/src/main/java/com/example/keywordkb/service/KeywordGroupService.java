package com.example.keywordkb.service;

import com.example.keywordkb.entity.KeywordGroup;
import java.util.List;

public interface KeywordGroupService {
    KeywordGroup createGroup(String name, List<String> keywords);
    KeywordGroup updateGroup(Long id, String name, List<String> keywords);
    void deleteGroup(Long id);
    List<KeywordGroup> listGroups();
    KeywordGroup getGroup(Long id);
}