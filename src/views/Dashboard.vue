<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">欢迎回来，{{ user.name }}！</h1>
        <p class="welcome-subtitle">今日有 {{ statistics.todaySessions }} 个会话需要关注，AI 已自动处理 89% 的咨询</p>
      </div>
      <div class="welcome-actions">
        <button class="btn-primary" @click="handleQuickAction">
          <i class="fas fa-plus mr-2"></i>快速处理
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div 
        v-for="(stat, index) in statsCards" 
        :key="index"
        class="stat-card"
        :class="stat.bgClass"
        @click="handleStatClick(stat)"
      >
        <div class="stat-content">
          <div class="stat-info">
            <p class="stat-label">{{ stat.label }}</p>
            <p class="stat-value">{{ stat.value }}</p>
            <p class="stat-trend" :class="stat.trendClass">
              {{ stat.trend }}
            </p>
          </div>
          <div class="stat-icon">
            <i :class="stat.icon"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 平台接入概况 -->
    <div class="platforms-section">
      <div class="section-header">
        <h2 class="section-title">多平台接入概况</h2>
        <button class="btn-secondary" @click="refreshPlatforms">
          <i class="fas fa-sync mr-2"></i>刷新状态
        </button>
      </div>
      
      <div class="platforms-grid">
        <div 
          v-for="platform in platforms" 
          :key="platform.name"
          class="platform-card"
          :class="{ 'offline': platform.status === 'offline' }"
        >
          <div class="platform-info">
            <div class="platform-icon">
              <i :class="[platform.icon, platform.color]"></i>
            </div>
            <div class="platform-details">
              <h3 class="platform-name">{{ platform.name }}</h3>
              <p class="platform-shops">{{ platform.shops }}个店铺已接入</p>
            </div>
          </div>
          <div class="platform-status">
            <span class="status-badge" :class="platform.status">
              {{ platform.status === 'online' ? '在线' : '离线' }}
            </span>
            <p class="platform-sessions">{{ platform.sessions }} 会话</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 热点问题趋势 -->
    <div class="trends-section">
      <div class="section-header">
        <h2 class="section-title">热点问题趋势</h2>
        <div class="trend-filters">
          <button 
            v-for="filter in trendFilters"
            :key="filter.value"
            class="filter-btn"
            :class="{ 'active': activeTrendFilter === filter.value }"
            @click="setTrendFilter(filter.value)"
          >
            {{ filter.label }}
          </button>
        </div>
      </div>
      
      <div class="trends-list">
        <div 
          v-for="trend in filteredTrends" 
          :key="trend.name"
          class="trend-item"
        >
          <div class="trend-info">
            <span class="trend-name">{{ trend.name }}</span>
            <div class="trend-progress">
              <div class="progress-bar">
                <div 
                  class="progress-fill"
                  :class="trend.colorClass"
                  :style="{ width: trend.percentage + '%' }"
                ></div>
              </div>
              <span class="trend-percentage">{{ trend.percentage }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 未处理问题提醒 -->
    <div class="pending-issues">
      <div class="section-header">
        <h2 class="section-title">未处理问题提醒</h2>
        <button class="btn-primary" @click="handleAllIssues">
          处理全部
        </button>
      </div>
      
      <div class="issues-table">
        <table class="table">
          <thead>
            <tr>
              <th>平台</th>
              <th>用户</th>
              <th>问题类型</th>
              <th>等待时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="issue in pendingIssues" 
              :key="issue.id"
              class="issue-row"
            >
              <td>
                <div class="platform-cell">
                  <i :class="[issue.platformIcon, issue.platformColor]"></i>
                  <span>{{ issue.platform }}</span>
                </div>
              </td>
              <td>{{ issue.user }}</td>
              <td>{{ issue.type }}</td>
              <td class="wait-time" :class="issue.waitTimeClass">
                {{ issue.waitTime }}
              </td>
              <td>
                <span class="status-badge" :class="issue.statusClass">
                  {{ issue.status }}
                </span>
              </td>
              <td>
                <button class="btn-sm btn-primary" @click="handleIssue(issue)">
                  处理
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions">
      <div class="action-card" @click="showModal('addKnowledge')">
        <i class="fas fa-plus text-blue-600"></i>
        <span>添加知识</span>
      </div>
      <div class="action-card" @click="showModal('syncData')">
        <i class="fas fa-sync text-green-600"></i>
        <span>同步数据</span>
      </div>
      <div class="action-card" @click="showModal('settings')">
        <i class="fas fa-cog text-purple-600"></i>
        <span>系统设置</span>
      </div>
    </div>

    <!-- 模态框 -->
    <div v-if="showModalFlag" class="modal-overlay" @click="hideModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ modalTitle }}</h3>
          <button class="modal-close" @click="hideModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <p>{{ modalContent }}</p>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="hideModal">取消</button>
          <button class="btn-primary" @click="confirmModal">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      // 统计卡片数据
      statsCards: [
        {
          label: '今日会话数',
          value: 1234,
          trend: '↑ 12% 较昨日',
          trendClass: 'text-green-600',
          icon: 'fas fa-comments text-4xl opacity-80',
          bgClass: 'bg-gradient-to-r from-pink-400 to-red-400',
          action: 'viewSessions'
        },
        {
          label: '平均响应时间',
          value: '2.3s',
          trend: '↓ 0.5s 较昨日',
          trendClass: 'text-green-600',
          icon: 'fas fa-clock text-4xl opacity-80',
          bgClass: 'bg-gradient-to-r from-blue-400 to-cyan-400',
          action: 'viewResponseTime'
        },
        {
          label: '问题解决率',
          value: '89%',
          trend: '↑ 3% 较昨日',
          trendClass: 'text-green-600',
          icon: 'fas fa-check-circle text-4xl opacity-80',
          bgClass: 'bg-gradient-to-r from-green-400 to-teal-400',
          action: 'viewSolveRate'
        },
        {
          label: '待处理工单',
          value: 23,
          trend: '↓ 5 较昨日',
          trendClass: 'text-green-600',
          icon: 'fas fa-ticket-alt text-4xl opacity-80',
          bgClass: 'bg-gradient-to-r from-yellow-400 to-orange-400',
          action: 'viewTickets'
        }
      ],
      
      // 趋势筛选
      activeTrendFilter: 'all',
      trendFilters: [
        { label: '全部', value: 'all' },
        { label: '今日', value: 'today' },
        { label: '本周', value: 'week' },
        { label: '本月', value: 'month' }
      ],
      
      // 热点趋势数据
      trends: [
        { name: '退款流程咨询', percentage: 85, colorClass: 'bg-blue-600' },
        { name: '商品质量问题', percentage: 72, colorClass: 'bg-green-600' },
        { name: '物流配送查询', percentage: 68, colorClass: 'bg-yellow-600' },
        { name: '发票开具申请', percentage: 55, colorClass: 'bg-purple-600' }
      ],
      
      // 未处理问题
      pendingIssues: [
        {
          id: 1,
          platform: '淘宝店铺A',
          platformIcon: 'fab fa-alipay',
          platformColor: 'text-blue-600',
          user: '用户***123',
          type: '退款申请',
          waitTime: '15分钟',
          waitTimeClass: 'text-red-600',
          status: '紧急',
          statusClass: 'bg-red-100 text-red-800'
        },
        {
          id: 2,
          platform: '拼多多店铺B',
          platformIcon: 'fas fa-shopping-cart',
          platformColor: 'text-red-600',
          user: '用户***456',
          type: '商品质量问题',
          waitTime: '8分钟',
          waitTimeClass: 'text-orange-600',
          status: '一般',
          statusClass: 'bg-orange-100 text-orange-800'
        }
      ],
      
      // 模态框
      showModalFlag: false,
      modalType: '',
      modalTitle: '',
      modalContent: ''
    }
  },
  computed: {
    ...mapState(['user', 'statistics', 'platforms']),
    
    // 筛选后的趋势数据
    filteredTrends() {
      // 这里可以根据筛选条件过滤数据
      return this.trends
    }
  },
  methods: {
    ...mapActions(['updateStatistics']),
    
    // 处理统计卡片点击
    handleStatClick(stat) {
      this.$message.info(`查看${stat.label}详情`)
      // 这里可以跳转到对应的详情页面
    },
    
    // 快速处理
    handleQuickAction() {
      this.$message.success('开始快速处理...')
    },
    
    // 刷新平台状态
    refreshPlatforms() {
      this.$message.info('正在刷新平台状态...')
      // 模拟刷新
      setTimeout(() => {
        this.$message.success('平台状态已更新')
      }, 1000)
    },
    
    // 设置趋势筛选
    setTrendFilter(filter) {
      this.activeTrendFilter = filter
      this.$message.info(`切换到${filter}筛选`)
    },
    
    // 处理问题
    handleIssue(issue) {
      this.$message.success(`正在处理问题: ${issue.type}`)
    },
    
    // 处理全部问题
    handleAllIssues() {
      this.$message.success('正在批量处理所有问题...')
    },
    
    // 显示模态框
    showModal(type) {
      this.modalType = type
      this.showModalFlag = true
      
      const modalConfig = {
        addKnowledge: {
          title: '添加知识',
          content: '确定要添加新的知识条目吗？'
        },
        syncData: {
          title: '同步数据',
          content: '确定要同步平台数据吗？'
        },
        settings: {
          title: '系统设置',
          content: '确定要打开系统设置吗？'
        }
      }
      
      const config = modalConfig[type]
      this.modalTitle = config.title
      this.modalContent = config.content
    },
    
    // 隐藏模态框
    hideModal() {
      this.showModalFlag = false
    },
    
    // 确认模态框操作
    confirmModal() {
      this.$message.success(`${this.modalTitle}操作已确认`)
      this.hideModal()
    }
  },
  mounted() {
    // 模拟实时数据更新
    setInterval(() => {
      this.statsCards.forEach(card => {
        if (typeof card.value === 'number') {
          card.value += Math.floor(Math.random() * 10)
        }
      })
    }, 30000)
  }
}
</script>

<style scoped>
/* 仪表板容器 */
.dashboard {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem;
}

/* 欢迎区域 */
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 0.75rem;
  padding: 1.5rem;
  color: white;
  margin-bottom: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.welcome-subtitle {
  color: #bfdbfe;
  font-size: 0.875rem;
}

.btn-primary {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-primary:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 0.875rem;
  opacity: 0.9;
  margin-bottom: 0.25rem;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: bold;
  margin-bottom: 0.25rem;
}

.stat-trend {
  font-size: 0.875rem;
  opacity: 0.9;
}

.stat-icon {
  opacity: 0.8;
}

/* 平台接入区域 */
.platforms-section {
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
}

.btn-secondary {
  background: #f3f4f6;
  border: 1px solid #d1d5db;
  color: #374151;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.btn-secondary:hover {
  background: #e5e7eb;
}

.platforms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1rem;
}

.platform-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  transition: all 0.2s ease;
}

.platform-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.platform-card.offline {
  opacity: 0.6;
}

.platform-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.platform-icon {
  font-size: 1.5rem;
}

.platform-details {
  display: flex;
  flex-direction: column;
}

.platform-name {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.platform-shops {
  font-size: 0.875rem;
  color: #6b7280;
}

.platform-status {
  text-align: right;
}

.status-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.status-badge.online {
  background: #dcfce7;
  color: #166534;
}

.status-badge.offline {
  background: #fef2f2;
  color: #dc2626;
}

.platform-sessions {
  font-size: 0.875rem;
  color: #6b7280;
  margin-top: 0.25rem;
}

/* 趋势区域 */
.trends-section {
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.trend-filters {
  display: flex;
  gap: 0.5rem;
}

.filter-btn {
  padding: 0.375rem 0.75rem;
  border: 1px solid #d1d5db;
  background: white;
  color: #6b7280;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.filter-btn.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}

.trends-list {
  margin-top: 1rem;
}

.trend-item {
  margin-bottom: 0.75rem;
}

.trend-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.trend-name {
  font-size: 0.875rem;
  color: #6b7280;
}

.trend-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
  margin-left: 1rem;
}

.progress-bar {
  flex: 1;
  height: 0.5rem;
  background: #e5e7eb;
  border-radius: 0.25rem;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 0.25rem;
  transition: width 0.3s ease;
}

.trend-percentage {
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  min-width: 3rem;
  text-align: right;
}

/* 未处理问题区域 */
.pending-issues {
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  text-align: left;
  padding: 0.75rem;
  border-bottom: 1px solid #e5e7eb;
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
}

.table td {
  padding: 0.75rem;
  border-bottom: 1px solid #f3f4f6;
  font-size: 0.875rem;
}

.table tr:hover {
  background: #f9fafb;
}

.issue-type {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.issue-type.urgent {
  background: #fef2f2;
  color: #dc2626;
}

.issue-type.normal {
  background: #f0f9ff;
  color: #0369a1;
}

.issue-type.low {
  background: #f0fdf4;
  color: #166534;
}

/* 快速操作区域 */
.quick-actions {
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
}

.action-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.action-card i {
  font-size: 2rem;
  color: #3b82f6;
  margin-bottom: 0.75rem;
}

.action-card span {
  font-size: 0.875rem;
  color: #374151;
  font-weight: 500;
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
  padding: 0;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 20px 25px rgba(0, 0, 0, 0.1);
}

.modal-header {
  padding: 1.25rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  color: #6b7280;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.375rem;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background: #f3f4f6;
  color: #374151;
}

.modal-body {
  padding: 1.25rem;
}

.modal-footer {
  padding: 1.25rem;
  border-top: 1px solid #e5e7eb;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard {
    padding: 0.5rem;
  }
  
  .welcome-section {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
    padding: 1rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }
  
  .platforms-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .action-card {
    width: 100%;
    max-width: 200px;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
}
</style> 