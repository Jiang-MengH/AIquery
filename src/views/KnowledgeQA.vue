<template>
  <div class="knowledge-qa">
    <h1 class="page-title">问答知识库</h1>
    <p class="page-subtitle">管理和配置智能问答知识</p>
    
    <!-- 搜索和操作栏 -->
    <div class="action-bar">
      <div class="search-box">
        <i class="fas fa-search"></i>
        <input type="text" placeholder="搜索知识条目..." v-model="searchKeyword">
      </div>
      <div class="action-buttons">
        <button class="btn-secondary">
          <i class="fas fa-download mr-2"></i>导入知识
        </button>
        <button class="btn-primary">
          <i class="fas fa-plus mr-2"></i>添加知识
        </button>
      </div>
    </div>

    <!-- 分类标签 -->
    <div class="category-tabs">
      <button 
        v-for="category in categories" 
        :key="category.id"
        class="category-tab"
        :class="{ active: activeCategory === category.id }"
        @click="setActiveCategory(category.id)"
      >
        {{ category.name }} ({{ category.count }})
      </button>
    </div>

    <!-- 知识条目列表 -->
    <div class="knowledge-list">
      <div 
        v-for="item in filteredKnowledgeItems" 
        :key="item.id"
        class="knowledge-item"
      >
        <div class="item-header">
          <h3>{{ item.title }}</h3>
          <span class="badge badge-info">{{ item.category }}</span>
        </div>
        <div class="item-content">
          <p>{{ item.description }}</p>
          <div class="item-meta">
            <span>咨询量: {{ item.consultations }}</span>
            <span>准确率: {{ item.accuracy }}%</span>
            <span>更新时间: {{ item.updateTime }}</span>
          </div>
        </div>
        <div class="item-actions">
          <button class="btn-sm btn-secondary">编辑</button>
          <button class="btn-sm btn-primary">测试</button>
          <button class="btn-sm btn-error">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KnowledgeQA',
  data() {
    return {
      searchKeyword: '',
      activeCategory: 'all',
      categories: [
        { id: 'all', name: '全部', count: 208 },
        { id: 'pre-sale', name: '售前问题', count: 3 },
        { id: 'after-sale', name: '售后问题', count: 7 },
        { id: 'product', name: '商品问题', count: 105 },
        { id: 'promotion', name: '活动优惠', count: 11 },
        { id: 'logistics', name: '物流问题', count: 11 },
        { id: 'chat', name: '聊天互动', count: 59 },
        { id: 'purchase', name: '购买操作', count: 12 }
      ],
      knowledgeItems: [
        {
          id: 1,
          title: '退款流程和规则',
          category: '行业通用',
          description: '包含退款申请、修改退款信息、无法申请退款等相关内容',
          consultations: 156,
          accuracy: 94,
          updateTime: '2024-01-15 14:30'
        },
        {
          id: 2,
          title: '发票开具和管理',
          category: '行业通用',
          description: '发票金额、发票进度查询、发票信息反馈等相关内容',
          consultations: 89,
          accuracy: 92,
          updateTime: '2024-01-15 12:15'
        },
        {
          id: 3,
          title: '安装服务说明',
          category: '行业通用',
          description: '安装教程、安装服务、安装网点等相关内容',
          consultations: 67,
          accuracy: 88,
          updateTime: '2024-01-14 18:45'
        }
      ]
    }
  },
  computed: {
    filteredKnowledgeItems() {
      let items = this.knowledgeItems
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        items = items.filter(item => item.category === this.activeCategory)
      }
      
      // 按关键词搜索
      if (this.searchKeyword) {
        items = items.filter(item => 
          item.title.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchKeyword.toLowerCase())
        )
      }
      
      return items
    }
  },
  methods: {
    setActiveCategory(categoryId) {
      this.activeCategory = categoryId
    }
  }
}
</script>

<style scoped>
.knowledge-qa {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem;
}

.page-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.page-subtitle {
  color: #6b7280;
  margin-bottom: 1.5rem;
  font-size: 0.875rem;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  gap: 1rem;
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-box i {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
}

.search-box input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
}

.btn-primary, .btn-secondary {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.btn-primary {
  background: #2563eb;
  color: #fff;
}

.btn-primary:hover {
  background: #1d4ed8;
}

.btn-secondary {
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
}

.btn-secondary:hover {
  background: #e5e7eb;
}

.category-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.category-tab {
  padding: 0.5rem 1rem;
  border: 1px solid #d1d5db;
  background: #fff;
  color: #6b7280;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.category-tab.active {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.category-tab:hover:not(.active) {
  background: #f9fafb;
}

.knowledge-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.knowledge-item {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.25rem;
  transition: all 0.2s ease;
}

.knowledge-item:hover {
  border-color: #2563eb;
  box-shadow: 0 2px 8px rgba(37, 102, 235, 0.1);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.item-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.badge {
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.badge-info {
  background: #dbeafe;
  color: #1e40af;
}

.item-content {
  margin-bottom: 1rem;
}

.item-content p {
  color: #6b7280;
  font-size: 0.875rem;
  line-height: 1.5;
  margin-bottom: 0.75rem;
}

.item-meta {
  display: flex;
  gap: 1.5rem;
  font-size: 0.75rem;
  color: #9ca3af;
}

.item-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-sm {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-sm.btn-secondary {
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
}

.btn-sm.btn-primary {
  background: #2563eb;
  color: #fff;
}

.btn-sm.btn-error {
  background: #ef4444;
  color: #fff;
}

.btn-sm:hover {
  transform: translateY(-1px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .knowledge-qa {
    padding: 0.5rem;
  }
  
  .action-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .search-box {
    max-width: 100%;
  }
  
  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }
  
  .category-tabs {
    gap: 0.25rem;
  }
  
  .category-tab {
    padding: 0.375rem 0.75rem;
    font-size: 0.75rem;
  }
  
  .item-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .item-meta {
    flex-direction: column;
    gap: 0.25rem;
  }
  
  .item-actions {
    flex-wrap: wrap;
  }
}
</style> 