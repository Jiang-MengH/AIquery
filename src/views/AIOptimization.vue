<template>
  <div class="ai-optimization">
    <h1 class="page-title">AI智能优化</h1>
    <p class="page-subtitle">模型训练和性能监控</p>
    
    <!-- 优化概览 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-brain text-blue-600"></i>
        </div>
        <div class="stat-content">
          <h3>模型准确率</h3>
          <p class="stat-value">94.2%</p>
          <p class="stat-trend text-green-600">↑ 2.1% 较上周</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-chart-line text-green-600"></i>
        </div>
        <div class="stat-content">
          <h3>训练数据量</h3>
          <p class="stat-value">12,456</p>
          <p class="stat-trend text-blue-600">+1,234 新增</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-cog text-purple-600"></i>
        </div>
        <div class="stat-content">
          <h3>优化次数</h3>
          <p class="stat-value">156</p>
          <p class="stat-trend text-orange-600">本周优化</p>
        </div>
      </div>
    </div>

    <!-- 模型训练状态 -->
    <div class="card">
      <h2 class="card-title">模型训练状态</h2>
      <div class="model-list">
        <div class="model-item">
          <div class="model-info">
            <div class="status-dot online"></div>
            <div>
              <h4>基础问答模型</h4>
              <p>最后更新: 2024-01-15 14:30</p>
            </div>
          </div>
          <div class="model-status">
            <span class="badge badge-success">运行中</span>
            <p>准确率: 94.2%</p>
          </div>
        </div>
        
        <div class="model-item">
          <div class="model-info">
            <div class="status-dot training"></div>
            <div>
              <h4>商品推荐模型</h4>
              <p>最后更新: 2024-01-15 12:15</p>
            </div>
          </div>
          <div class="model-status">
            <span class="badge badge-warning">训练中</span>
            <p>进度: 78%</p>
          </div>
        </div>
        
        <div class="model-item">
          <div class="model-info">
            <div class="status-dot offline"></div>
            <div>
              <h4>情感分析模型</h4>
              <p>最后更新: 2024-01-14 18:45</p>
            </div>
          </div>
          <div class="model-status">
            <span class="badge badge-error">失败</span>
            <p>准确率: 87.5%</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 优化建议 -->
    <div class="card">
      <h2 class="card-title">优化建议</h2>
      <div class="suggestions">
        <div class="suggestion-item suggestion-info">
          <i class="fas fa-lightbulb text-blue-600"></i>
          <div>
            <h4>增加训练数据</h4>
            <p>建议增加500条商品相关问答数据，可提升商品推荐准确率3-5%</p>
          </div>
        </div>
        
        <div class="suggestion-item suggestion-success">
          <i class="fas fa-chart-bar text-green-600"></i>
          <div>
            <h4>优化关键词匹配</h4>
            <p>当前关键词匹配准确率为89%，建议优化关键词库，可提升至92%</p>
          </div>
        </div>
        
        <div class="suggestion-item suggestion-warning">
          <i class="fas fa-exclamation-triangle text-orange-600"></i>
          <div>
            <h4>情感分析优化</h4>
            <p>情感分析模型准确率偏低，建议重新训练或调整参数</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AIOptimization',
  data() {
    return {
      // 页面数据
    }
  },
  methods: {
    // 页面方法
  }
}
</script>

<style scoped>
.ai-optimization {
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.2s ease;
}

.stat-card:hover {
  border-color: #2563eb;
  box-shadow: 0 2px 8px rgba(37, 102, 235, 0.1);
}

.stat-icon {
  font-size: 1.5rem;
}

.stat-content h3 {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.25rem;
  font-weight: 500;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.stat-trend {
  font-size: 0.75rem;
  font-weight: 500;
}

.text-green-600 {
  color: #059669;
}

.text-blue-600 {
  color: #2563eb;
}

.text-purple-600 {
  color: #7c3aed;
}

.text-orange-600 {
  color: #ea580c;
}

.card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.25rem;
  margin-bottom: 1.5rem;
}

.card-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
}

.model-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.model-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #f3f4f6;
  border-radius: 0.5rem;
  transition: all 0.2s ease;
}

.model-item:hover {
  border-color: #2563eb;
  background: #f8fafc;
}

.model-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.status-dot {
  width: 0.75rem;
  height: 0.75rem;
  border-radius: 50%;
}

.status-dot.online {
  background: #10b981;
}

.status-dot.training {
  background: #f59e0b;
}

.status-dot.offline {
  background: #ef4444;
}

.model-info h4 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.model-info p {
  font-size: 0.875rem;
  color: #6b7280;
}

.model-status {
  text-align: right;
}

.badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.badge-success {
  background: #dcfce7;
  color: #166534;
}

.badge-warning {
  background: #fef3c7;
  color: #92400e;
}

.badge-error {
  background: #fef2f2;
  color: #dc2626;
}

.model-status p {
  font-size: 0.875rem;
  color: #6b7280;
}

.suggestions {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.suggestion-item {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 0.5rem;
  border: 1px solid #e5e7eb;
}

.suggestion-item.suggestion-info {
  background: #eff6ff;
  border-color: #bfdbfe;
}

.suggestion-item.suggestion-success {
  background: #dcfce7;
  border-left-color: #10b981;
}

.suggestion-item.suggestion-warning {
  background: #fffbeb;
  border-color: #fed7aa;
}

.suggestion-item.suggestion-error {
  background: #fef2f2;
  border-color: #fecaca;
}

.suggestion-item i {
  font-size: 1.25rem;
  margin-top: 0.125rem;
}

.suggestion-item h4 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.suggestion-item p {
  font-size: 0.875rem;
  color: #6b7280;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-optimization {
    padding: 0.5rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }
  
  .model-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
  
  .model-status {
    text-align: left;
  }
  
  .suggestion-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style> 