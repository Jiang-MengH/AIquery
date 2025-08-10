package com.example.keywordkb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping
    public List<Map<String, Object>> listProducts() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(new HashMap<String, Object>() {{
            put("id", 101);
            put("name", "智能音箱A款");
        }});
        list.add(new HashMap<String, Object>() {{
            put("id", 102);
            put("name", "蓝牙耳机B型");
        }});
        list.add(new HashMap<String, Object>() {{
            put("id", 103);
            put("name", "家用摄像头C");
        }});
        return list;
    }
}