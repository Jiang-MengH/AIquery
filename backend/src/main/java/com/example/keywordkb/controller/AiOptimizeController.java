package com.example.keywordkb.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/ai-optimize")
public class AiOptimizeController {
    @PostMapping
    public Map<String, Object> optimize(@RequestBody Map<String, Object> req) {
        String content = (String) req.getOrDefault("content", "");
        // 模拟AI优化，实际可接入AI服务
        String optimized = content.replace("。", "！");
        Map<String, Object> res = new HashMap<>();
        res.put("optimizedContent", "[AI优化] " + optimized);
        return res;
    }
}