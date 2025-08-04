package com.example.keywordkb.service.impl;

import com.example.keywordkb.entity.KeywordGroup;
import com.example.keywordkb.repository.KeywordGroupRepository;
import com.example.keywordkb.repository.KeywordRepository;
import com.example.keywordkb.service.KeywordGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KeywordGroupServiceImpl implements KeywordGroupService {
    @Autowired
    private KeywordGroupRepository groupRepo;
    @Autowired
    private KeywordRepository keywordRepo;

    @Override
    @Transactional
    public KeywordGroup createGroup(String name, List<String> keywords) {
        // 具体实现略
        return null;
    }

    @Override
    @Transactional
    public KeywordGroup updateGroup(Long id, String name, List<String> keywords) {
        // 具体实现略
        return null;
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        // 具体实现略
    }

    @Override
    public List<KeywordGroup> listGroups() {
        // 具体实现略
        return null;
    }

    @Override
    public KeywordGroup getGroup(Long id) {
        // 具体实现略
        return null;
    }
}