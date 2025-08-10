package com.example.keywordkb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/knowledge-templates")
public class KnowledgeTemplateController {
    @GetMapping
    public List<Map<String, Object>> listTemplates() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(new HashMap<String, Object>() {{
            put("id", 1);
            put("name", "标准模板");
            put("type", "standard");
            put("content", "亲，您的订单【订单号】已进入【订单状态】，如有疑问请联系【客服电话】。");
        }});
        list.add(new HashMap<String, Object>() {{
            put("id", 2);
            put("name", "常用使用模板");
            put("type", "common");
            put("content", "您好，关于【商品名称】的售后问题，您可参考【售后政策】。");
        }});
        return list;
    }
}