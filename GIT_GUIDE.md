# Git 版本控制指南

## 项目概述
这是一个Vue.js客服系统项目，包含前端Vue应用和后端Spring Boot服务。

## Git 仓库设置

### 初始化完成
✅ Git仓库已初始化
✅ .gitignore文件已创建
✅ 初始提交已完成

## 常用Git命令

### 查看状态
```bash
git status
```

### 查看提交历史
```bash
git log --oneline
```

### 添加文件到暂存区
```bash
git add .                    # 添加所有文件
git add src/views/          # 添加特定目录
git add package.json        # 添加特定文件
```

### 提交更改
```bash
git commit -m "描述你的更改"
```

### 查看分支
```bash
git branch
```

### 创建新分支
```bash
git checkout -b feature/新功能名称
```

### 切换分支
```bash
git checkout 分支名称
```

## 推荐的提交信息格式

### 功能开发
- `feat: 添加用户登录功能`
- `feat: 实现知识库管理模块`

### 修复问题
- `fix: 修复登录页面样式问题`
- `fix: 解决API接口返回错误`

### 文档更新
- `docs: 更新README文档`
- `docs: 添加API接口说明`

### 代码重构
- `refactor: 重构用户管理组件`
- `refactor: 优化数据库查询性能`

## 分支管理策略

### 主分支
- `master`: 生产环境代码
- `develop`: 开发环境代码

### 功能分支
- `feature/功能名称`: 新功能开发
- `bugfix/问题描述`: 问题修复
- `hotfix/紧急修复`: 紧急修复

## 工作流程

1. **开始新功能开发**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/新功能名称
   ```

2. **开发完成后**
   ```bash
   git add .
   git commit -m "feat: 完成新功能开发"
   git push origin feature/新功能名称
   ```

3. **合并到开发分支**
   ```bash
   git checkout develop
   git merge feature/新功能名称
   git push origin develop
   ```

## 注意事项

- 提交前请确保代码能正常运行
- 提交信息要清晰描述更改内容
- 定期从远程仓库拉取最新代码
- 重要更改前请创建备份分支

## 项目文件结构

```
cursor-ai/
├── src/                    # Vue前端源码
│   ├── views/             # 页面组件
│   ├── components/        # 通用组件
│   ├── router/           # 路由配置
│   └── store/            # 状态管理
├── backend/               # Spring Boot后端
│   ├── src/main/java/    # Java源码
│   └── src/main/resources/ # 配置文件
├── public/               # 静态资源
├── package.json          # 前端依赖配置
└── README.md            # 项目说明
```

## 下一步操作

1. 配置远程仓库（如GitHub、GitLab）
2. 设置开发团队协作规范
3. 配置CI/CD流程
4. 建立代码审查流程 