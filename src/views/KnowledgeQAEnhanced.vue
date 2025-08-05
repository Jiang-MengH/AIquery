<template>
  <div class="knowledge-qa-enhanced">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">问答知识库</h1>
        <p class="page-subtitle">管理和配置智能问答知识</p>
      </div>
      <div class="header-actions">
        <button class="btn-secondary" @click="showStatistics">
          <i class="fas fa-chart-bar mr-2"></i>数据统计
        </button>
        <button class="btn-primary" @click="showAddModal">
          <i class="fas fa-plus mr-2"></i>新增知识
        </button>
      </div>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-overview" v-if="showStats">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-file-alt"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalEntries }}</div>
          <div class="stat-label">总条目数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-eye"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalViews }}</div>
          <div class="stat-label">总查看次数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-chart-line"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.avgAccuracy }}%</div>
          <div class="stat-label">平均准确率</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-star"></i>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.avgRating }}/5</div>
          <div class="stat-label">用户满意度</div>
        </div>
      </div>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input 
            type="text" 
            placeholder="搜索知识条目..." 
            v-model="searchKeyword"
            @input="handleSearch"
          >
        </div>
        <div class="filter-dropdown">
          <select v-model="selectedStatus" @change="handleFilter">
            <option value="">全部状态</option>
            <option value="active">已启用</option>
            <option value="inactive">已禁用</option>
            <option value="draft">草稿</option>
          </select>
        </div>
      </div>
      
      <div class="action-buttons">
        <button class="btn-secondary" @click="showImportModal">
          <i class="fas fa-download mr-2"></i>导入
        </button>
        <button class="btn-secondary" @click="exportData">
          <i class="fas fa-upload mr-2"></i>导出
        </button>
        <button class="btn-secondary" @click="batchOperation" :disabled="!hasSelectedItems">
          <i class="fas fa-tasks mr-2"></i>批量操作
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
    <div class="knowledge-list" v-loading="loading">
      <div class="list-header">
        <div class="select-all">
          <input 
            type="checkbox" 
            :checked="allSelected"
            @change="toggleSelectAll"
          >
          <span>全选</span>
        </div>
        <div class="list-info">
          共 {{ filteredItems.length }} 条记录
        </div>
      </div>

      <div 
        v-for="item in paginatedItems" 
        :key="item.id"
        class="knowledge-item"
        :class="{ selected: selectedItems.includes(item.id) }"
      >
        <div class="item-checkbox">
          <input 
            type="checkbox" 
            :checked="selectedItems.includes(item.id)"
            @change="toggleSelectItem(item.id)"
          >
        </div>
        
        <div class="item-content">
          <div class="item-header">
            <h3 class="item-title">{{ item.title }}</h3>
            <div class="item-badges">
              <span class="badge badge-info">{{ item.category }}</span>
              <span class="badge" :class="getStatusBadgeClass(item.status)">
                {{ getStatusText(item.status) }}
              </span>
            </div>
          </div>
          
          <div class="item-description">
            <p>{{ item.description }}</p>
          </div>
          
          <div class="item-meta">
            <span class="meta-item">
              <i class="fas fa-eye"></i>
              查看: {{ item.viewCount }}
            </span>
            <span class="meta-item">
              <i class="fas fa-chart-line"></i>
              准确率: {{ item.accuracy }}%
            </span>
            <span class="meta-item">
              <i class="fas fa-clock"></i>
              更新: {{ formatDate(item.updateTime) }}
            </span>
          </div>
        </div>
        
        <div class="item-actions">
          <button class="btn-sm btn-secondary" @click="editItem(item)" title="编辑">
            <i class="fas fa-edit"></i>
          </button>
          <button class="btn-sm btn-primary" @click="testItem(item)" title="测试">
            <i class="fas fa-play"></i>
          </button>
          <button class="btn-sm btn-warning" @click="previewItem(item)" title="预览">
            <i class="fas fa-eye"></i>
          </button>
          <button class="btn-sm btn-error" @click="deleteItem(item)" title="删除">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="totalPages > 1">
        <button 
          class="page-btn" 
          :disabled="currentPage === 1"
          @click="changePage(currentPage - 1)"
        >
          <i class="fas fa-chevron-left"></i>
        </button>
        
        <button 
          v-for="page in visiblePages" 
          :key="page"
          class="page-btn"
          :class="{ active: page === currentPage }"
          @click="changePage(page)"
        >
          {{ page }}
        </button>
        
        <button 
          class="page-btn" 
          :disabled="currentPage === totalPages"
          @click="changePage(currentPage + 1)"
        >
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>

    <!-- 新增/编辑模态框 -->
    <div class="modal-overlay" v-if="showModal" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑知识条目' : '新增知识条目' }}</h3>
          <button class="modal-close" @click="closeModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <form @submit.prevent="saveEntry">
            <div class="form-section">
              <h4>基本信息</h4>
              <div class="form-group">
                <label class="form-label">问题标题 *</label>
                <input 
                  type="text" 
                  class="form-input" 
                  v-model="formData.title"
                  placeholder="请输入问题标题"
                  required
                >
              </div>
              
              <div class="form-group">
                <label class="form-label">答案内容 *</label>
                <textarea 
                  class="form-textarea" 
                  v-model="formData.content"
                  placeholder="请输入答案内容"
                  rows="6"
                  required
                ></textarea>
              </div>
              
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">所属分类 *</label>
                  <select class="form-select" v-model="formData.category" required>
                    <option value="">请选择分类</option>
                    <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                      {{ cat.name }}
                    </option>
                  </select>
                </div>
                
                <div class="form-group">
                  <label class="form-label">优先级</label>
                  <div class="priority-stars">
                    <i 
                      v-for="star in 5" 
                      :key="star"
                      class="fas fa-star"
                      :class="{ active: star <= formData.priority }"
                      @click="formData.priority = star"
                    ></i>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="form-section">
              <h4>高级设置</h4>
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">启用状态</label>
                  <div class="toggle-switch">
                    <input 
                      type="checkbox" 
                      id="isActive"
                      v-model="formData.isActive"
                    >
                    <label for="isActive"></label>
                  </div>
                </div>
                
                <div class="form-group">
                  <label class="form-label">自动回复</label>
                  <div class="toggle-switch">
                    <input 
                      type="checkbox" 
                      id="autoReply"
                      v-model="formData.autoReply"
                    >
                    <label for="autoReply"></label>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
        
        <div class="modal-footer">
          <button class="btn-secondary" @click="closeModal">取消</button>
          <button class="btn-secondary" @click="saveDraft">保存草稿</button>
          <button class="btn-primary" @click="saveEntry">发布</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KnowledgeQAEnhanced',
  data() {
    return {
      // 搜索和筛选
      searchKeyword: '',
      selectedStatus: '',
      activeCategory: 'all',
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      
      // 选择状态
      selectedItems: [],
      
      // 模态框状态
      showModal: false,
      isEditing: false,
      showStats: false,
      
      // 表单数据
      formData: {
        title: '',
        content: '',
        category: '',
        priority: 3,
        isActive: true,
        autoReply: false
      },
      
      // 加载状态
      loading: false,
      
      // 统计数据
      statistics: {
        totalEntries: 208,
        totalViews: 15680,
        avgAccuracy: 92.5,
        avgRating: 4.2
      },
      
      // 分类数据
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
      
      // 知识条目数据
      knowledgeItems: [
        {
          id: 1,
          title: '退款流程和规则',
          category: '售后问题',
          description: '包含退款申请、修改退款信息、无法申请退款等相关内容',
          viewCount: 156,
          accuracy: 94,
          updateTime: '2024-01-15 14:30',
          status: 'active',
          priority: 5
        },
        {
          id: 2,
          title: '发票开具和管理',
          category: '售后问题',
          description: '发票金额、发票进度查询、发票信息反馈等相关内容',
          viewCount: 89,
          accuracy: 92,
          updateTime: '2024-01-15 12:15',
          status: 'active',
          priority: 4
        },
        {
          id: 3,
          title: '安装服务说明',
          category: '售前问题',
          description: '安装教程、安装服务、安装网点等相关内容',
          viewCount: 67,
          accuracy: 88,
          updateTime: '2024-01-14 18:45',
          status: 'draft',
          priority: 3
        }
      ]
    }
  },
  computed: {
    filteredItems() {
      let items = this.knowledgeItems
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        items = items.filter(item => item.category === this.activeCategory)
      }
      
      // 按状态筛选
      if (this.selectedStatus) {
        items = items.filter(item => item.status === this.selectedStatus)
      }
      
      // 按关键词搜索
      if (this.searchKeyword) {
        items = items.filter(item => 
          item.title.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchKeyword.toLowerCase())
        )
      }
      
      return items
    },
    
    paginatedItems() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredItems.slice(start, end)
    },
    
    totalPages() {
      return Math.ceil(this.filteredItems.length / this.pageSize)
    },
    
    visiblePages() {
      const pages = []
      const maxVisible = 5
      let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2))
      let end = Math.min(this.totalPages, start + maxVisible - 1)
      
      if (end - start + 1 < maxVisible) {
        start = Math.max(1, end - maxVisible + 1)
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      
      return pages
    },
    
    allSelected() {
      return this.paginatedItems.length > 0 && 
             this.paginatedItems.every(item => this.selectedItems.includes(item.id))
    },
    
    hasSelectedItems() {
      return this.selectedItems.length > 0
    }
  },
  methods: {
    // 搜索和筛选
    handleSearch() {
      this.currentPage = 1
    },
    
    handleFilter() {
      this.currentPage = 1
    },
    
    setActiveCategory(categoryId) {
      this.activeCategory = categoryId
      this.currentPage = 1
    },
    
    // 分页
    changePage(page) {
      this.currentPage = page
    },
    
    // 选择操作
    toggleSelectAll() {
      if (this.allSelected) {
        this.selectedItems = []
      } else {
        this.selectedItems = this.paginatedItems.map(item => item.id)
      }
    },
    
    toggleSelectItem(itemId) {
      const index = this.selectedItems.indexOf(itemId)
      if (index > -1) {
        this.selectedItems.splice(index, 1)
      } else {
        this.selectedItems.push(itemId)
      }
    },
    
    // 模态框操作
    showAddModal() {
      this.isEditing = false
      this.resetForm()
      this.showModal = true
    },
    
    closeModal() {
      this.showModal = false
      this.resetForm()
    },
    
    resetForm() {
      this.formData = {
        title: '',
        content: '',
        category: '',
        priority: 3,
        isActive: true,
        autoReply: false
      }
    },
    
    // 表单操作
    async saveEntry() {
      try {
        this.loading = true
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success(this.isEditing ? '更新成功' : '添加成功')
        this.closeModal()
      } catch (error) {
        this.$message.error('操作失败，请重试')
      } finally {
        this.loading = false
      }
    },
    
    async saveDraft() {
      try {
        this.loading = true
        await new Promise(resolve => setTimeout(resolve, 500))
        this.$message.success('草稿保存成功')
        this.closeModal()
      } catch (error) {
        this.$message.error('保存失败，请重试')
      } finally {
        this.loading = false
      }
    },
    
    // 条目操作
    editItem(item) {
      this.isEditing = true
      this.formData = {
        title: item.title,
        content: item.description,
        category: item.category,
        priority: item.priority,
        isActive: item.status === 'active',
        autoReply: false
      }
      this.showModal = true
    },
    
    async testItem(item) {
      try {
        this.loading = true
        await new Promise(resolve => setTimeout(resolve, 1500))
        this.$message.success(`测试完成：${item.title}`)
      } catch (error) {
        this.$message.error('测试失败，请重试')
      } finally {
        this.loading = false
      }
    },
    
    previewItem(item) {
      this.$message.info(`预览：${item.title}`)
    },
    
    async deleteItem(item) {
      const confirmed = await this.$interaction.confirm({
        title: '确认删除',
        message: `确定要删除知识条目"${item.title}"吗？此操作不可撤销。`,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
      })
      
      if (confirmed) {
        try {
          this.loading = true
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.knowledgeItems = this.knowledgeItems.filter(i => i.id !== item.id)
          this.$message.success('删除成功')
        } catch (error) {
          this.$message.error('删除失败，请重试')
        } finally {
          this.loading = false
        }
      }
    },
    
    // 批量操作
    batchOperation() {
      this.$message.info('批量操作功能开发中...')
    },
    
    // 导入导出
    showImportModal() {
      this.$message.info('导入功能开发中...')
    },
    
    exportData() {
      this.$message.info('导出功能开发中...')
    },
    
    // 统计
    showStatistics() {
      this.showStats = !this.showStats
    },
    
    // 工具方法
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('zh-CN')
    },
    
    getStatusText(status) {
      const statusMap = {
        'active': '已启用',
        'inactive': '已禁用',
        'draft': '草稿'
      }
      return statusMap[status] || status
    },
    
    getStatusBadgeClass(status) {
      const classMap = {
        'active': 'badge-success',
        'inactive': 'badge-warning',
        'draft': 'badge-info'
      }
      return classMap[status] || 'badge-info'
    }
  }
}
</script>

<style scoped>
.knowledge-qa-enhanced {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1.5rem;
  gap: 1rem;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 1.875rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.page-subtitle {
  color: #6b7280;
  font-size: 0.875rem;
}

.header-actions {
  display: flex;
  gap: 0.75rem;
}

/* 统计概览 */
.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  width: 3rem;
  height: 3rem;
  background: #dbeafe;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2563eb;
  font-size: 1.25rem;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1f2937;
  line-height: 1;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  margin-top: 0.25rem;
}

/* 操作栏 */
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  gap: 1rem;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1rem;
}

.search-section {
  display: flex;
  gap: 1rem;
  flex: 1;
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

.filter-dropdown select {
  padding: 0.75rem 1rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  background: white;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
}

/* 按钮样式 */
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

.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 分类标签 */
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

/* 列表头部 */
.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding: 0.75rem 1rem;
  background: #f9fafb;
  border-radius: 0.5rem;
}

.select-all {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #6b7280;
}

.list-info {
  font-size: 0.875rem;
  color: #6b7280;
}

/* 知识条目列表 */
.knowledge-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.knowledge-item {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.25rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.knowledge-item:hover {
  border-color: #2563eb;
  box-shadow: 0 2px 8px rgba(37, 102, 235, 0.1);
}

.knowledge-item.selected {
  border-color: #2563eb;
  background: #f0f9ff;
}

.item-checkbox {
  padding-top: 0.25rem;
}

.item-content {
  flex: 1;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.75rem;
  gap: 1rem;
}

.item-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
  flex: 1;
}

.item-badges {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
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

.badge-success {
  background: #dcfce7;
  color: #166534;
}

.badge-warning {
  background: #fef3c7;
  color: #92400e;
}

.item-description {
  margin-bottom: 1rem;
}

.item-description p {
  color: #6b7280;
  font-size: 0.875rem;
  line-height: 1.5;
  margin: 0;
}

.item-meta {
  display: flex;
  gap: 1.5rem;
  font-size: 0.75rem;
  color: #9ca3af;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.item-actions {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.btn-sm {
  padding: 0.5rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
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

.btn-sm.btn-warning {
  background: #f59e0b;
  color: #fff;
}

.btn-sm.btn-error {
  background: #ef4444;
  color: #fff;
}

.btn-sm:hover {
  transform: translateY(-1px);
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.page-btn {
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db;
  background: white;
  color: #374151;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.page-btn:hover:not(:disabled) {
  background: #f9fafb;
}

.page-btn.active {
  background: #2563eb;
  color: white;
  border-color: #2563eb;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 0.75rem;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  color: #6b7280;
  padding: 0.5rem;
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  padding: 1.5rem;
  border-top: 1px solid #e5e7eb;
}

/* 表单样式 */
.form-section {
  margin-bottom: 2rem;
}

.form-section h4 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
}

.form-input, .form-select, .form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.form-input:focus, .form-select:focus, .form-textarea:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 120px;
}

/* 优先级星星 */
.priority-stars {
  display: flex;
  gap: 0.25rem;
}

.priority-stars i {
  color: #d1d5db;
  cursor: pointer;
  font-size: 1.25rem;
  transition: color 0.2s ease;
}

.priority-stars i.active {
  color: #f59e0b;
}

.priority-stars i:hover {
  color: #f59e0b;
}

/* 开关样式 */
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 3rem;
  height: 1.5rem;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-switch label {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 1.5rem;
}

.toggle-switch label:before {
  position: absolute;
  content: "";
  height: 1.125rem;
  width: 1.125rem;
  left: 0.1875rem;
  bottom: 0.1875rem;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

.toggle-switch input:checked + label {
  background-color: #2563eb;
}

.toggle-switch input:checked + label:before {
  transform: translateX(1.5rem);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .knowledge-qa-enhanced {
    padding: 0.5rem;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .header-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .action-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .search-section {
    flex-direction: column;
    width: 100%;
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
  
  .knowledge-item {
    flex-direction: column;
    gap: 0.75rem;
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
    justify-content: flex-end;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .modal-content {
    width: 95%;
    margin: 1rem;
  }
}
</style> 