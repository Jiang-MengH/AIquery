<template>
  <div class="knowledge-qa">
    <!-- 顶部信息栏 -->
    <div class="top-info-bar">
      <div class="category-stats">
        <div class="stat-item" 
             v-for="category in categoryStats" 
             :key="category.id"
             :class="{ active: activeCategory === category.id }"
             @click="setActiveCategory(category.id)">
          <span class="stat-label">{{ category.name }}</span>
          <span class="stat-count">({{ category.count }})</span>
        </div>
      </div>
      
      <div class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input 
            type="text" 
            placeholder="搜索关键词或问题..." 
            v-model="searchKeyword"
            @input="handleSearch"
          >
        </div>
        
        <div class="filter-dropdown">
          <select v-model="selectedSimulationType" @change="handleFilter">
            <option value="">模拟问法筛选</option>
            <option value="common">常见问法</option>
            <option value="specific">特定问法</option>
            <option value="variation">变体问法</option>
          </select>
        </div>
        
        <button class="btn-subscribe" @click="subscribeIndustryPackage">
          <i class="fas fa-download mr-2"></i>订阅行业包
        </button>
      </div>
    </div>

    <!-- 问题卡片列表 -->
    <div class="knowledge-cards" v-loading="loading">
      <div class="cards-header">
        <div class="cards-info">
          共 {{ filteredCards.length }} 个关键词
        </div>
        <div class="view-toggle">
          <button class="btn-toggle" :class="{ active: viewMode === 'card' }" @click="setViewMode('card')">
            <i class="fas fa-th-large"></i> 卡片视图
          </button>
          <button class="btn-toggle" :class="{ active: viewMode === 'list' }" @click="setViewMode('list')">
            <i class="fas fa-list"></i> 列表视图
          </button>
        </div>
      </div>

      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" class="cards-grid">
        <div 
          v-for="card in paginatedCards" 
          :key="card.keyword_id"
          class="knowledge-card"
        >
          <div class="card-header">
            <h3 class="keyword-title">{{ card.keyword_name }}</h3>
            <div class="card-badges">
              <span class="badge" :class="card.is_industry_common ? 'badge-industry' : 'badge-custom'">
                {{ card.is_industry_common ? '行业通用' : '自定义' }}
              </span>
            </div>
          </div>

          <div class="card-content">
            <!-- 常见问法 -->
            <div class="common-questions">
              <h4 class="section-title">常见问法</h4>
              <div class="questions-list">
                <div v-for="(question, index) in card.common_questions.slice(0, 3)" :key="index" class="question-item">
                  <i class="fas fa-question-circle"></i>
                  {{ question }}
                </div>
                <div v-if="card.common_questions.length > 3" class="more-questions">
                  <a href="#" @click.prevent="showMoreQuestions(card)">更多问法</a>
                </div>
              </div>
            </div>

            <!-- 触发条件 -->
            <div class="trigger-conditions">
              <h4 class="section-title">触发条件</h4>
              <div class="conditions-content">
                <span v-if="!card.trigger_conditions || Object.keys(card.trigger_conditions).length === 0" class="no-conditions">
                  无装载条件
                </span>
                <div v-else class="conditions-list">
                  <span v-for="(value, key) in card.trigger_conditions" :key="key" class="condition-item">
                    {{ key }}: {{ value }}
                  </span>
                </div>
              </div>
            </div>

            <!-- 知识内容 -->
            <div class="knowledge-content">
              <h4 class="section-title">知识内容</h4>
              <div v-if="card.knowledge_content" class="content-text" v-html="card.knowledge_content"></div>
              <div v-else class="no-content">
                <button class="btn-add-knowledge" @click="addKnowledge(card)">
                  <i class="fas fa-plus"></i> 添加知识
                </button>
              </div>
            </div>
          </div>

          <div class="card-actions">
            <button v-if="card.knowledge_content" class="btn-action btn-configure" @click="configureKnowledge(card)">
              <i class="fas fa-cog"></i> 配置
            </button>
            <button v-if="card.knowledge_content" class="btn-action btn-delete" @click="deleteKnowledge(card)">
              <i class="fas fa-trash"></i> 删除
            </button>
            <button v-if="!card.knowledge_content" class="btn-action btn-add" @click="addKnowledge(card)">
              <i class="fas fa-plus"></i> 添加知识
            </button>
          </div>
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else class="cards-list">
        <div 
          v-for="card in paginatedCards" 
          :key="card.keyword_id"
          class="knowledge-list-item"
        >
          <div class="list-item-header">
            <h3 class="keyword-title">{{ card.keyword_name }}</h3>
            <div class="list-item-badges">
              <span class="badge" :class="card.is_industry_common ? 'badge-industry' : 'badge-custom'">
                {{ card.is_industry_common ? '行业通用' : '自定义' }}
              </span>
            </div>
          </div>
          
          <div class="list-item-content">
            <div class="content-summary">
              <span class="questions-count">{{ card.common_questions.length }} 个问法</span>
              <span class="conditions-summary">
                {{ getConditionsSummary(card.trigger_conditions) }}
              </span>
              <span class="content-status" :class="card.knowledge_content ? 'has-content' : 'no-content'">
                {{ card.knowledge_content ? '已配置' : '未配置' }}
              </span>
            </div>
          </div>

          <div class="list-item-actions">
            <button v-if="card.knowledge_content" class="btn-action btn-configure" @click="configureKnowledge(card)">
              <i class="fas fa-cog"></i> 配置
            </button>
            <button v-if="card.knowledge_content" class="btn-action btn-delete" @click="deleteKnowledge(card)">
              <i class="fas fa-trash"></i> 删除
            </button>
            <button v-if="!card.knowledge_content" class="btn-action btn-add" @click="addKnowledge(card)">
              <i class="fas fa-plus"></i> 添加知识
            </button>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="totalPages > 1">
        <button class="btn-page" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">
          <i class="fas fa-chevron-left"></i>
        </button>
        <button 
          v-for="page in visiblePages" 
          :key="page"
          class="btn-page" 
          :class="{ active: page === currentPage }"
          @click="changePage(page)"
        >
          {{ page }}
        </button>
        <button class="btn-page" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>

    <!-- 添加/编辑知识弹窗 -->
    <div v-if="showKnowledgeModal" class="modal-overlay" @click="closeKnowledgeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ editingKnowledge ? '编辑知识' : '添加知识' }}</h3>
          <button class="btn-close" @click="closeKnowledgeModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <form @submit.prevent="saveKnowledge">
            <div class="form-group">
              <label>关键词名称</label>
              <input type="text" v-model="knowledgeForm.keyword_name" required>
            </div>
            
            <div class="form-group">
              <label>所属分类</label>
              <select v-model="knowledgeForm.keyword_category" required>
                <option value="">请选择分类</option>
                <option v-for="category in categories" :key="category.id" :value="category.id">
                  {{ category.name }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>是否行业通用</label>
              <div class="radio-group">
                <label class="radio-item">
                  <input type="radio" v-model="knowledgeForm.is_industry_common" :value="true">
                  <span>是</span>
                </label>
                <label class="radio-item">
                  <input type="radio" v-model="knowledgeForm.is_industry_common" :value="false">
                  <span>否</span>
                </label>
              </div>
            </div>
            
            <div class="form-group">
              <label>常见问法</label>
              <div class="questions-input">
                <div v-for="(question, index) in knowledgeForm.common_questions" :key="index" class="question-input-row">
                  <input type="text" v-model="knowledgeForm.common_questions[index]" placeholder="输入问法">
                  <button type="button" class="btn-remove" @click="removeQuestion(index)">
                    <i class="fas fa-minus"></i>
                  </button>
                </div>
                <button type="button" class="btn-add-question" @click="addQuestion">
                  <i class="fas fa-plus"></i> 添加问法
                </button>
              </div>
            </div>
            
            <div class="form-group">
              <label>触发条件</label>
              <div class="conditions-input">
                <div class="condition-row">
                  <select v-model="conditionForm.key">
                    <option value="">选择条件类型</option>
                    <option value="product">商品条件</option>
                    <option value="order_status">订单状态</option>
                    <option value="user_type">用户类型</option>
                  </select>
                  <input type="text" v-model="conditionForm.value" placeholder="条件值">
                  <button type="button" class="btn-add-condition" @click="addCondition">
                    <i class="fas fa-plus"></i>
                  </button>
                </div>
                <div class="conditions-list">
                  <div v-for="(value, key) in knowledgeForm.trigger_conditions" :key="key" class="condition-item">
                    <span>{{ key }}: {{ value }}</span>
                    <button type="button" class="btn-remove-condition" @click="removeCondition(key)">
                      <i class="fas fa-times"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="form-group">
              <label>知识内容</label>
              <textarea v-model="knowledgeForm.knowledge_content" rows="6" placeholder="输入知识内容..." required></textarea>
            </div>
            
            <div class="form-group">
              <label>自动回复模式</label>
              <div class="radio-group">
                <label class="radio-item">
                  <input type="radio" v-model="knowledgeForm.auto_reply_enabled" :value="true">
                  <span>开启</span>
                </label>
                <label class="radio-item">
                  <input type="radio" v-model="knowledgeForm.auto_reply_enabled" :value="false">
                  <span>关闭</span>
                </label>
              </div>
            </div>
          </form>
        </div>
        
        <div class="modal-footer">
          <button class="btn-secondary" @click="closeKnowledgeModal">取消</button>
          <button class="btn-primary" @click="saveKnowledge">保存</button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="showDeleteModal" class="modal-overlay" @click="closeDeleteModal">
      <div class="modal-content delete-modal" @click.stop>
        <div class="modal-header">
          <h3>确认删除</h3>
        </div>
        <div class="modal-body">
          <p>确定要删除关键词 "{{ deletingKnowledge?.keyword_name }}" 的知识配置吗？</p>
          <p class="warning-text">删除后无法恢复，请谨慎操作！</p>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="closeDeleteModal">取消</button>
          <button class="btn-error" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>

    <!-- 更多问法弹窗 -->
    <div v-if="showQuestionsModal" class="modal-overlay" @click="closeQuestionsModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ currentCard?.keyword_name }} - 全部问法</h3>
          <button class="btn-close" @click="closeQuestionsModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="questions-list-full">
            <div v-for="(question, index) in currentCard?.common_questions" :key="index" class="question-item-full">
              <span class="question-number">{{ index + 1 }}.</span>
              <span class="question-text">{{ question }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>


export default {
  name: 'KnowledgeQA',
  components: {
  },
  data() {
    return {
      // 搜索和筛选
      searchKeyword: '',
      selectedSimulationType: '',
      activeCategory: 'all',
      
      // 视图模式
      viewMode: 'card',
      
      // 分页
      currentPage: 1,
      pageSize: 12,
      
      // 加载状态
      loading: false,
      
      // 模态框状态
      showKnowledgeModal: false,
      showDeleteModal: false,
      showQuestionsModal: false,
      editingKnowledge: null,
      deletingKnowledge: null,
      currentCard: null,
      
      // 表单数据
      knowledgeForm: {
        keyword_id: null,
        keyword_name: '',
        keyword_category: '',
        is_industry_common: false,
        common_questions: [''],
        trigger_conditions: {},
        knowledge_id: null,
        knowledge_content: '',
        auto_reply_enabled: true
      },
      
      // 条件表单
      conditionForm: {
        key: '',
        value: ''
      },
      
      // 分类统计数据
      categoryStats: [
        { id: 'all', name: '全部', count: 208 },
        { id: 'pre-sale', name: '售前问题', count: 3 },
        { id: 'after-sale', name: '售后问题', count: 7 },
        { id: 'product', name: '商品问题', count: 105 },
        { id: 'promotion', name: '活动优惠', count: 11 },
        { id: 'logistics', name: '物流问题', count: 11 },
        { id: 'chat', name: '聊天互动', count: 59 },
        { id: 'purchase', name: '购买操作', count: 12 }
      ],
      
      // 分类映射
      categories: [
        { id: 'pre-sale', name: '售前问题' },
        { id: 'after-sale', name: '售后问题' },
        { id: 'product', name: '商品问题' },
        { id: 'promotion', name: '活动优惠' },
        { id: 'logistics', name: '物流问题' },
        { id: 'chat', name: '聊天互动' },
        { id: 'purchase', name: '购买操作' }
      ],
      
      // 知识卡片数据
      knowledgeCards: [], // 从后端获取
    }
  },
  created() {
    this.fetchKnowledgeCards()
  },
  computed: {
    filteredCards() {
      let cards = this.knowledgeCards
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        cards = cards.filter(card => card.keyword_category === this.activeCategory)
      }
      
      // 按模拟问法筛选
      if (this.selectedSimulationType) {
        cards = cards.filter(card => {
          if (this.selectedSimulationType === 'common') {
            return card.common_questions.length > 0
          } else if (this.selectedSimulationType === 'specific') {
            return card.common_questions.length === 1
          } else if (this.selectedSimulationType === 'variation') {
            return card.common_questions.length > 1
          }
          return true
        })
      }
      
      // 按关键词搜索
      if (this.searchKeyword) {
        cards = cards.filter(card => 
          card.keyword_name.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          card.common_questions.some(question => 
            question.toLowerCase().includes(this.searchKeyword.toLowerCase())
          ) ||
          (card.knowledge_content && card.knowledge_content.toLowerCase().includes(this.searchKeyword.toLowerCase()))
        )
      }
      
      return cards
    },
    
    paginatedCards() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredCards.slice(start, end)
    },
    
    totalPages() {
      return Math.ceil(this.filteredCards.length / this.pageSize)
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
    
    // 视图模式切换
    setViewMode(mode) {
      this.viewMode = mode
    },
    
    // 分页
    changePage(page) {
      this.currentPage = page
    },
    
    // 获取条件摘要
    getConditionsSummary(conditions) {
      if (!conditions || Object.keys(conditions).length === 0) {
        return '无装载条件'
      }
      const keys = Object.keys(conditions)
      return `${keys[0]}: ${conditions[keys[0]]}${keys.length > 1 ? '...' : ''}`
    },
    
    // 订阅行业包
    subscribeIndustryPackage() {
              console.log('订阅行业包功能开发中...')
    },
    
    // 显示更多问法
    showMoreQuestions(card) {
      this.currentCard = card
      this.showQuestionsModal = true
    },
    
    closeQuestionsModal() {
      this.showQuestionsModal = false
      this.currentCard = null
    },
    
    // 添加知识
    addKnowledge(card) {
      this.editingKnowledge = null
      this.resetKnowledgeForm()
      if (card) {
        this.knowledgeForm.keyword_name = card.keyword_name
        this.knowledgeForm.keyword_category = card.keyword_category
        this.knowledgeForm.is_industry_common = card.is_industry_common
        this.knowledgeForm.common_questions = [...card.common_questions]
        this.knowledgeForm.trigger_conditions = { ...card.trigger_conditions }
      }
      this.showKnowledgeModal = true
    },
    
    // 配置知识
    configureKnowledge(card) {
      this.editingKnowledge = card
      this.knowledgeForm = {
        keyword_id: card.keyword_id,
        keyword_name: card.keyword_name,
        keyword_category: card.keyword_category,
        is_industry_common: card.is_industry_common,
        common_questions: [...card.common_questions],
        trigger_conditions: { ...card.trigger_conditions },
        knowledge_id: card.knowledge_id,
        knowledge_content: card.knowledge_content,
        auto_reply_enabled: card.auto_reply_enabled
      }
      this.showKnowledgeModal = true
    },
    
    // 删除知识
    deleteKnowledge(card) {
      this.deletingKnowledge = card
      this.showDeleteModal = true
    },
    
    // 确认删除
    async confirmDelete() {
      try {
        this.loading = true
        // 调用后端API删除
        await this.deleteKnowledgeAPI(this.deletingKnowledge.knowledge_id)
        console.log('删除成功')
        this.closeDeleteModal()
        this.fetchKnowledgeCards()
      } catch (error) {
        console.log('删除失败，请重试')
      } finally {
        this.loading = false
      }
    },
    
    // 关闭删除弹窗
    closeDeleteModal() {
      this.showDeleteModal = false
      this.deletingKnowledge = null
    },
    
    // 关闭知识弹窗
    closeKnowledgeModal() {
      this.showKnowledgeModal = false
      this.editingKnowledge = null
      this.resetKnowledgeForm()
    },
    
    // 重置知识表单
    resetKnowledgeForm() {
      this.knowledgeForm = {
        keyword_id: null,
        keyword_name: '',
        keyword_category: '',
        is_industry_common: false,
        common_questions: [''],
        trigger_conditions: {},
        knowledge_id: null,
        knowledge_content: '',
        auto_reply_enabled: true
      }
      this.conditionForm = {
        key: '',
        value: ''
      }
    },
    
    // 添加问法
    addQuestion() {
      this.knowledgeForm.common_questions.push('')
    },
    
    // 移除问法
    removeQuestion(index) {
      if (this.knowledgeForm.common_questions.length > 1) {
        this.knowledgeForm.common_questions.splice(index, 1)
      }
    },
    
    // 添加条件
    addCondition() {
      if (this.conditionForm.key && this.conditionForm.value) {
        this.knowledgeForm.trigger_conditions[this.conditionForm.key] = this.conditionForm.value
        this.conditionForm.key = ''
        this.conditionForm.value = ''
      }
    },
    
    // 移除条件
    removeCondition(key) {
      delete this.knowledgeForm.trigger_conditions[key]
    },
    
    // 保存知识
    async saveKnowledge() {
      try {
        this.loading = true
        
        // 过滤空问法
        this.knowledgeForm.common_questions = this.knowledgeForm.common_questions.filter(q => q.trim())
        
        if (this.editingKnowledge) {
          // 更新知识
          await this.updateKnowledgeAPI(this.knowledgeForm)
          console.log('更新成功')
        } else {
          // 添加知识
          await this.addKnowledgeAPI(this.knowledgeForm)
          console.log('添加成功')
        }
        
        this.closeKnowledgeModal()
        this.fetchKnowledgeCards()
      } catch (error) {
        console.log('保存失败，请重试')
      } finally {
        this.loading = false
      }
    },
    
    // 获取知识卡片数据
    async fetchKnowledgeCards() {
      try {
        this.loading = true
        const response = await this.$http.get('/keyword-knowledge')
        this.knowledgeCards = response.data || []
        this.updateCategoryStats()
      } catch (error) {
        console.error('获取知识卡片失败:', error)
        // 使用console.log替代$message，避免组件未定义错误
        console.log('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 更新分类统计
    updateCategoryStats() {
      this.categoryStats.forEach(category => {
        if (category.id === 'all') {
          category.count = this.knowledgeCards.length
        } else {
          category.count = this.knowledgeCards.filter(card => 
            card.keyword_category === category.id
          ).length
        }
      })
    },
    
    // API调用方法
    async addKnowledgeAPI(data) {
      const response = await this.$http.post('/keyword-knowledge', data)
      return response.data
    },
    
    async updateKnowledgeAPI(data) {
      const response = await this.$http.put(`/keyword-knowledge/${data.knowledge_id}`, data)
      return response.data
    },
    
    async deleteKnowledgeAPI(knowledgeId) {
      const response = await this.$http.delete(`/keyword-knowledge/${knowledgeId}`)
      return response.data
    },
    
    // 关键词管理
    addKeyword() {
      if (this.newKeyword.trim()) {
        if (!this.formData.keywords.includes(this.newKeyword.trim())) {
          this.formData.keywords.push(this.newKeyword.trim())
        }
        this.newKeyword = ''
      }
    },
    
    removeKeyword(index) {
      this.formData.keywords.splice(index, 1)
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
    },

    async fetchKnowledgeItems() {
      this.loading = true
      try {
        const res = await this.$http.get('/keyword-knowledge')
        // 将后端数据格式转换为前端需要的格式
        this.knowledgeItems = res.data.map(item => {
          const conditions = item.conditions ? JSON.parse(item.conditions) : {}
          return {
            id: item.id,
            title: this.generateTitleFromContent(item.content),
            content: item.content,
            category: conditions.category || 'other',
            priority: conditions.priority || 3,
            status: item.autoReply ? 'active' : 'inactive',
            isActive: item.autoReply,
            autoReply: item.autoReply,
            keywords: conditions.keywords ? conditions.keywords.split(',') : [],
            conditions: conditions,
            views: Math.floor(Math.random() * 1000) + 100,
            accuracy: (Math.random() * 20 + 80).toFixed(1),
            rating: (Math.random() * 2 + 3).toFixed(1),
            satisfaction: Math.floor(Math.random() * 20 + 80),
            createdAt: item.createdAt,
            updatedAt: item.updatedAt,
            createdBy: item.createdBy
          }
        })
      } catch (e) {
        console.log('知识库数据加载失败')
      } finally {
        this.loading = false
      }
    },

    generateTitleFromContent(content) {
      // 从内容中提取标题，取前30个字符
      const cleanContent = content.replace(/\n/g, ' ').trim()
      return cleanContent.length > 30 ? cleanContent.substring(0, 30) + '...' : cleanContent
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
          await this.$http.delete(`/keyword-knowledge/${item.id}`)
          this.knowledgeItems = this.knowledgeItems.filter(i => i.id !== item.id)
          console.log('删除成功')
        } catch (error) {
          console.log('删除失败，请重试')
        } finally {
          this.loading = false
        }
      }
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

.stat-trend {
  font-size: 0.75rem;
  margin-top: 0.25rem;
  font-weight: 500;
}

.stat-trend.positive {
  color: #059669;
}

.stat-trend.negative {
  color: #dc2626;
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

.search-box {
  position: relative;
  flex: 1;
  max-width: 400px;
  display: flex;
  align-items: center;
}

.search-box input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.search-advanced {
  position: absolute;
  right: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #6b7280;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.25rem;
  transition: all 0.2s ease;
}

.search-advanced:hover {
  background: #f3f4f6;
  color: #374151;
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

.item-title-section {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
}

.item-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
  flex: 1;
}

.item-priority {
  display: flex;
  gap: 0.125rem;
}

.item-priority i {
  color: #d1d5db;
  font-size: 0.875rem;
  transition: color 0.2s ease;
}

.item-priority i.active {
  color: #f59e0b;
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
  margin-bottom: 0.5rem;
}

.item-keywords {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
}

.keyword-tag {
  background: #f3f4f6;
  color: #374151;
  padding: 0.125rem 0.375rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  border: 1px solid #e5e7eb;
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

.btn-sm.btn-info {
  background: #06b6d4;
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

.keyword-input {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.keyword-input .form-input {
  flex: 1;
}

.keyword-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.keyword-tag {
  background: #f3f4f6;
  color: #374151;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  border: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.keyword-tag i {
  cursor: pointer;
  color: #6b7280;
  font-size: 0.625rem;
}

.keyword-tag i:hover {
  color: #ef4444;
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

/* 顶部信息栏样式 */
.top-info-bar {
  background: #fff;
  border-radius: 0.5rem;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.category-stats {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 120px;
}

.stat-item:hover {
  background: #f8fafc;
}

.stat-item.active {
  background: #dbeafe;
  color: #1d4ed8;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.25rem;
}

.stat-count {
  font-size: 1.25rem;
  font-weight: 600;
  color: #111827;
}

.search-section {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 300px;
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
  background: #fff;
}

.btn-subscribe {
  background: #10b981;
  color: #fff;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-subscribe:hover {
  background: #059669;
}

/* 知识卡片样式 */
.knowledge-cards {
  background: #fff;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.cards-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.view-toggle {
  display: flex;
  gap: 0.5rem;
}

.btn-toggle {
  padding: 0.5rem 1rem;
  border: 1px solid #d1d5db;
  background: #fff;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-toggle.active {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.knowledge-card {
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  padding: 1.5rem;
  background: #fff;
  transition: all 0.2s ease;
}

.knowledge-card:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-color: #d1d5db;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.keyword-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
  margin: 0;
}

.card-badges {
  display: flex;
  gap: 0.5rem;
}

.badge {
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.badge-industry {
  background: #dbeafe;
  color: #1d4ed8;
}

.badge-custom {
  background: #fef3c7;
  color: #92400e;
}

.card-content {
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: #374151;
  margin: 0 0 0.5rem 0;
}

.questions-list {
  margin-bottom: 1rem;
}

.question-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0;
  font-size: 0.875rem;
  color: #6b7280;
}

.question-item i {
  color: #9ca3af;
  font-size: 0.75rem;
}

.more-questions a {
  color: #2563eb;
  text-decoration: none;
  font-size: 0.875rem;
}

.conditions-content {
  margin-bottom: 1rem;
}

.no-conditions {
  color: #9ca3af;
  font-style: italic;
}

.conditions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.condition-item {
  background: #f3f4f6;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  color: #374151;
}

.content-text {
  background: #f9fafb;
  padding: 1rem;
  border-radius: 0.375rem;
  border: 1px solid #e5e7eb;
  font-size: 0.875rem;
  line-height: 1.5;
  color: #374151;
}

.no-content {
  text-align: center;
  padding: 2rem;
  color: #9ca3af;
}

.btn-add-knowledge {
  background: #2563eb;
  color: #fff;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-add-knowledge:hover {
  background: #1d4ed8;
}

.card-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.btn-action {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-configure {
  background: #f59e0b;
  color: #fff;
}

.btn-configure:hover {
  background: #d97706;
}

.btn-delete {
  background: #ef4444;
  color: #fff;
}

.btn-delete:hover {
  background: #dc2626;
}

.btn-add {
  background: #10b981;
  color: #fff;
}

.btn-add:hover {
  background: #059669;
}

/* 列表视图样式 */
.cards-list {
  margin-bottom: 2rem;
}

.knowledge-list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.375rem;
  margin-bottom: 0.5rem;
  background: #fff;
}

.list-item-header {
  flex: 1;
}

.list-item-content {
  flex: 2;
}

.content-summary {
  display: flex;
  gap: 2rem;
  color: #6b7280;
  font-size: 0.875rem;
}

.content-status.has-content {
  color: #10b981;
}

.content-status.no-content {
  color: #ef4444;
}

.list-item-actions {
  display: flex;
  gap: 0.5rem;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 2rem;
}

.btn-page {
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db;
  background: #fff;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-page:hover:not(:disabled) {
  background: #f9fafb;
}

.btn-page.active {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.btn-page:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 0.5rem;
  max-width: 600px;
  width: 90%;
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
  color: #111827;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  color: #6b7280;
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid #e5e7eb;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.questions-input {
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  padding: 1rem;
}

.question-input-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  align-items: center;
}

.question-input-row input {
  flex: 1;
  margin: 0;
}

.btn-remove {
  background: #ef4444;
  color: #fff;
  border: none;
  border-radius: 0.25rem;
  padding: 0.5rem;
  cursor: pointer;
}

.btn-add-question {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.375rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
}

.conditions-input {
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  padding: 1rem;
}

.condition-row {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  align-items: center;
}

.condition-row select,
.condition-row input {
  flex: 1;
  margin: 0;
}

.btn-add-condition {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.25rem;
  padding: 0.5rem;
  cursor: pointer;
}

.conditions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.condition-item {
  background: #f3f4f6;
  padding: 0.5rem;
  border-radius: 0.375rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-remove-condition {
  background: #ef4444;
  color: #fff;
  border: none;
  border-radius: 0.25rem;
  padding: 0.25rem;
  cursor: pointer;
  font-size: 0.75rem;
}

.delete-modal {
  max-width: 400px;
}

.warning-text {
  color: #ef4444;
  font-weight: 500;
}

.questions-list-full {
  max-height: 400px;
  overflow-y: auto;
}

.question-item-full {
  display: flex;
  gap: 0.5rem;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f3f4f6;
}

.question-number {
  color: #6b7280;
  font-weight: 500;
  min-width: 2rem;
}

.question-text {
  color: #374151;
}

/* 基础按钮样式 */
.btn-primary {
  background: #2563eb;
  color: #fff;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-primary:hover {
  background: #1d4ed8;
}

.btn-secondary {
  background: #6b7280;
  color: #fff;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background: #4b5563;
}

.btn-error {
  background: #ef4444;
  color: #fff;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-error:hover {
  background: #dc2626;
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
  .knowledge-qa {
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
  
  .item-title-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .item-priority {
    align-self: flex-start;
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