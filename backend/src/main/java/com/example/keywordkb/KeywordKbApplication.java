package com.example.keywordkb;

import com.example.keywordkb.entity.KeywordKnowledge;
import com.example.keywordkb.repository.KeywordKnowledgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KeywordKbApplication {

    @Autowired
    private KeywordKnowledgeRepository keywordKnowledgeRepository;

    public static void main(String[] args) {
        SpringApplication.run(KeywordKbApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 检查是否已有数据，如果没有则初始化
            if (keywordKnowledgeRepository.count() == 0) {
                initializeSampleData();
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    private void initializeSampleData() {
        List<KeywordKnowledge> sampleData = Arrays.asList(
            // 售前问题
            createKnowledgeItem(1L, "产品功能咨询", "我们的产品具有以下核心功能：\n1. 智能客服自动回复\n2. 知识库管理\n3. 数据分析统计\n4. 多渠道集成\n\n如需了解更多详情，请联系我们的销售团队。", "pre-sale", 5, true, "产品功能,功能介绍,产品特性"),
            
            createKnowledgeItem(2L, "价格咨询", "我们的产品采用灵活的定价模式：\n- 基础版：¥999/月\n- 专业版：¥1999/月\n- 企业版：¥3999/月\n\n支持按年付费享受优惠，具体价格请联系销售。", "pre-sale", 4, true, "价格,费用,收费,定价"),
            
            createKnowledgeItem(3L, "试用申请", "我们提供30天免费试用，包含所有功能。\n\n申请步骤：\n1. 填写试用申请表\n2. 等待审核通过\n3. 开通试用账号\n4. 开始体验产品\n\n试用期间有专业客服提供支持。", "pre-sale", 4, true, "试用,免费试用,体验"),
            
            // 售后问题
            createKnowledgeItem(4L, "账号登录问题", "如果遇到登录问题，请按以下步骤排查：\n\n1. 确认用户名和密码正确\n2. 检查网络连接是否正常\n3. 清除浏览器缓存\n4. 尝试使用无痕模式\n\n如问题仍存在，请联系技术支持。", "after-sale", 5, true, "登录,账号,密码,无法登录"),
            
            createKnowledgeItem(5L, "数据备份恢复", "数据备份和恢复服务：\n\n- 自动备份：每日凌晨2点\n- 手动备份：随时可操作\n- 恢复时间：通常1-2小时\n- 备份保留：30天\n\n如需紧急恢复，请联系客服。", "after-sale", 4, true, "备份,恢复,数据丢失"),
            
            createKnowledgeItem(6L, "系统升级", "系统升级安排：\n\n- 升级时间：每月第一个周日凌晨2-4点\n- 升级通知：提前3天发送邮件\n- 升级时长：通常1-2小时\n- 升级内容：功能优化和bug修复\n\n升级期间系统将暂停服务。", "after-sale", 3, true, "升级,系统更新,维护"),
            
            createKnowledgeItem(7L, "发票申请", "发票申请流程：\n\n1. 登录系统进入发票管理\n2. 填写发票信息\n3. 上传相关证明材料\n4. 提交申请等待审核\n5. 审核通过后开具发票\n\n发票将在7个工作日内寄出。", "after-sale", 3, true, "发票,开票,报销"),
            
            // 商品问题
            createKnowledgeItem(8L, "商品缺货", "关于商品缺货的处理：\n\n- 系统会自动显示库存状态\n- 缺货商品可加入收藏夹\n- 到货后会短信通知\n- 支持预约购买\n\n具体到货时间请关注商品页面更新。", "product", 4, true, "缺货,无库存,到货通知"),
            
            createKnowledgeItem(9L, "商品质量问题", "商品质量保证：\n\n- 7天无理由退换货\n- 15天质量问题换货\n- 1年质保服务\n- 提供检测报告\n\n如遇质量问题，请保留商品和包装，联系客服处理。", "product", 5, true, "质量问题,退换货,质保"),
            
            createKnowledgeItem(10L, "商品规格说明", "商品规格说明：\n\n- 尺寸：详细尺寸信息\n- 材质：环保安全材质\n- 重量：精确重量数据\n- 包装：标准包装规格\n\n如有特殊规格需求，请联系客服咨询。", "product", 3, true, "规格,尺寸,材质,重量"),
            
            // 活动优惠
            createKnowledgeItem(11L, "优惠券使用", "优惠券使用规则：\n\n- 每张优惠券仅限使用一次\n- 不可与其他优惠叠加使用\n- 使用前请仔细阅读使用条件\n- 过期优惠券自动失效\n\n优惠券可在订单结算时选择使用。", "promotion", 4, true, "优惠券,折扣,优惠"),
            
            createKnowledgeItem(12L, "限时活动", "限时活动详情：\n\n- 活动时间：具体开始和结束时间\n- 活动范围：参与商品列表\n- 优惠力度：具体折扣信息\n- 参与条件：购买门槛要求\n\n活动期间商品价格可能会有波动。", "promotion", 4, true, "限时,活动,特价"),
            
            // 物流问题
            createKnowledgeItem(13L, "配送时间", "配送时间说明：\n\n- 同城配送：当日下单，次日送达\n- 省内配送：1-2个工作日\n- 全国配送：3-5个工作日\n- 偏远地区：5-7个工作日\n\n配送时间从订单确认后开始计算。", "logistics", 4, true, "配送,快递,送达时间"),
            
            createKnowledgeItem(14L, "运费说明", "运费标准：\n\n- 满99元包邮\n- 不满99元收取10元运费\n- 偏远地区额外收取5元\n- 特殊商品运费另计\n\n运费在订单结算时自动计算。", "logistics", 3, true, "运费,包邮,快递费"),
            
            // 聊天互动
            createKnowledgeItem(15L, "问候语", "您好！欢迎来到我们的客服系统。\n\n我是智能客服助手，很高兴为您服务。\n\n请问有什么可以帮助您的吗？\n\n我可以帮您：\n- 查询商品信息\n- 解答使用问题\n- 处理订单问题\n- 提供售后服务", "chat", 3, true, "你好,您好,在吗,在线"),
            
            createKnowledgeItem(16L, "感谢语", "感谢您的咨询！\n\n如果还有其他问题，随时可以联系我。\n\n祝您生活愉快！\n\n再见！", "chat", 2, true, "谢谢,感谢,再见,拜拜"),
            
            // 购买操作
            createKnowledgeItem(17L, "下单流程", "下单流程说明：\n\n1. 选择商品加入购物车\n2. 确认商品信息和数量\n3. 填写收货地址\n4. 选择支付方式\n5. 确认订单信息\n6. 完成支付\n\n下单后请及时关注订单状态。", "purchase", 4, true, "下单,购买,怎么买"),
            
            createKnowledgeItem(18L, "支付方式", "支持的支付方式：\n\n- 支付宝\n- 微信支付\n- 银行卡支付\n- 货到付款\n\n支付时请确保网络环境安全，避免信息泄露。", "purchase", 3, true, "支付,付款,支付宝,微信")
        );

        keywordKnowledgeRepository.saveAll(sampleData);
        System.out.println("已初始化 " + sampleData.size() + " 条问答知识库数据");
    }

    private KeywordKnowledge createKnowledgeItem(Long keywordId, String title, String content, String category, int priority, boolean autoReply, String keywords) {
        KeywordKnowledge item = new KeywordKnowledge();
        item.setKeywordId(keywordId);
        item.setContent(content);
        item.setConditions("{\"category\":\"" + category + "\",\"priority\":" + priority + ",\"keywords\":\"" + keywords + "\"}");
        item.setAutoReply(autoReply);
        item.setCreatedBy(1L);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return item;
    }
}