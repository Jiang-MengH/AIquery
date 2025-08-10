# API接口文档

## 接口规范

### 基础配置
- **前端端口**: 8081
- **后端端口**: 8080
- **API前缀**: `/api`
- **代理配置**: 前端 `/api/*` → 后端 `http://localhost:8080/api/*`

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 接口列表

### 1. 关键词知识库管理

#### 1.1 获取知识列表
- **接口**: `GET /api/keyword-knowledge`
- **描述**: 获取所有知识条目
- **响应**: `List<KeywordKnowledge>`

#### 1.2 添加知识
- **接口**: `POST /api/keyword-knowledge`
- **描述**: 添加新的知识条目
- **请求体**: `KeywordKnowledge`
- **响应**: `KeywordKnowledge`

#### 1.3 更新知识
- **接口**: `PUT /api/keyword-knowledge/{id}`
- **描述**: 更新指定ID的知识条目
- **请求体**: `KeywordKnowledge`
- **响应**: `KeywordKnowledge`

#### 1.4 删除知识
- **接口**: `DELETE /api/keyword-knowledge/{id}`
- **描述**: 删除指定ID的知识条目
- **响应**: `void`

### 2. AI智能优化

#### 2.1 AI优化
- **接口**: `POST /api/ai-optimize`
- **描述**: 对内容进行AI优化
- **请求体**: `{"content": "待优化内容"}`
- **响应**: `{"optimizedContent": "优化后内容"}`

### 3. 商品管理

#### 3.1 获取商品列表
- **接口**: `GET /api/products`
- **描述**: 获取所有商品
- **响应**: `List<Product>`

## 前端调用规范

### 1. 使用统一的HTTP客户端
```javascript
// 使用 this.$http 而不是直接使用 axios
const response = await this.$http.get('/keyword-knowledge')
```

### 2. API路径规范
```javascript
// 正确：使用相对路径，baseURL已配置为 /api
this.$http.get('/keyword-knowledge')
this.$http.post('/keyword-knowledge', data)
this.$http.put('/keyword-knowledge/123', data)
this.$http.delete('/keyword-knowledge/123')

// 错误：不要使用完整URL
this.$http.get('http://localhost:8080/api/keyword-knowledge')
```

### 3. 错误处理
```javascript
try {
  const response = await this.$http.get('/keyword-knowledge')
  // 处理成功响应
} catch (error) {
  console.error('API调用失败:', error)
  this.$message.error('操作失败，请重试')
}
```

## 数据模型

### KeywordKnowledge
```json
{
  "id": 1,
  "keyword_name": "关键词名称",
  "keyword_category": "pre-sale",
  "is_industry_common": true,
  "common_questions": ["问题1", "问题2"],
  "trigger_conditions": {},
  "knowledge_content": "知识内容",
  "auto_reply_enabled": true,
  "create_time": "2024-01-15T10:30:00",
  "update_time": "2024-01-15T10:30:00"
}
```

## 注意事项

1. **端口统一**: 前端8081，后端8080
2. **代理配置**: 所有API请求通过代理转发
3. **CORS支持**: 后端已配置跨域支持
4. **错误处理**: 统一使用try-catch处理API错误
5. **响应格式**: 保持数据结构一致性
