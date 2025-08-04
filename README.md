# AI客服后台管理系统

基于Vue2 + Tailwind CSS + Font Awesome构建的企业级AI客服后台管理系统。

## 技术栈

- **前端框架**: Vue 2.6.14
- **路由管理**: Vue Router 3.5.3
- **状态管理**: Vuex 3.6.2
- **UI框架**: Tailwind CSS
- **图标库**: Font Awesome 6.0.0
- **HTTP客户端**: Axios 0.27.2

## 功能模块

### 主要功能
- **数据统计**: 系统概览、实时数据监控
- **AI智能优化**: 模型训练、性能监控、优化建议

### 知识管理
- **店铺知识库**
  - 问答知识库
  - 关键词知识库
  - 店铺信息
  - 欢迎语
  - 违禁词管理
  - 场景解决方案

### 商品管理
- **商品知识库**
  - 商品管理
  - 商品模板管理

### 系统功能
- **自动回复**: 智能回复配置
- **设置**: 系统配置管理

## 项目结构

```
src/
├── components/          # 公共组件
│   ├── Layout.vue      # 主布局组件
│   └── Message.vue     # 消息提示组件
├── views/              # 页面组件
│   ├── Dashboard.vue   # 数据统计页面
│   ├── AIOptimization.vue # AI智能优化页面
│   └── ...            # 其他页面组件
├── router/             # 路由配置
│   └── index.js
├── store/              # Vuex状态管理
│   └── index.js
├── App.vue             # 根组件
└── main.js             # 入口文件
```

## 安装和运行

### 安装依赖
```bash
npm install
```

### 开发环境运行
```bash
npm run serve
```

### 生产环境构建
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

## 主要特性

### 1. 响应式设计
- 支持桌面端和移动端
- 自适应布局
- 流畅的动画效果

### 2. 组件化架构
- 可复用的UI组件
- 模块化的页面结构
- 清晰的数据流管理

### 3. 交互功能
- Tab切换
- 弹窗提示
- 搜索筛选
- 实时数据更新

### 4. 企业级UI设计
- 参考飞书后台、千牛工作台、京东云客服
- 信息密集、层级分明
- 交互清晰、操作便捷

## 开发规范

### 代码风格
- 使用ESLint进行代码检查
- 遵循Vue官方风格指南
- 组件命名采用PascalCase
- 文件命名采用kebab-case

### 组件开发
- 每个组件都有清晰的props定义
- 使用computed和watch优化性能
- 合理使用Vuex管理状态

### 样式规范
- 使用Tailwind CSS工具类
- 自定义样式采用scoped
- 响应式设计优先

## 部署说明

### 开发环境
项目启动后会自动打开浏览器，访问地址：http://localhost:8080

### 生产环境
构建后的文件位于dist目录，可直接部署到Web服务器。

## 浏览器支持

- Chrome >= 60
- Firefox >= 60
- Safari >= 12
- Edge >= 79

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License 