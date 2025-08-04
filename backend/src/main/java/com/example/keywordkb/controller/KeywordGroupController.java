package com.example.keywordkb.controller;

import com.example.keywordkb.entity.KeywordGroup;
import com.example.keywordkb.service.KeywordGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/keyword-groups")
public class KeywordGroupController {
    @Autowired
    private KeywordGroupService groupService;

    @PostMapping
    public KeywordGroup createGroup(@RequestBody KeywordGroup group) {
        // 具体实现略
        return null;
    }

    @PutMapping("/{id}")
    public KeywordGroup updateGroup(@PathVariable Long id, @RequestBody KeywordGroup group) {
        // 具体实现略
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        // 具体实现略
    }

    @GetMapping
    public List<KeywordGroup> listGroups() {
        // 具体实现略
        return null;
    }

    @GetMapping("/{id}")
    public KeywordGroup getGroup(@PathVariable Long id) {
        // 具体实现略
        return null;
    }
}