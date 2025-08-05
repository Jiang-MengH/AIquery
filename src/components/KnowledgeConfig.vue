<template>
  <div class="knowledge-config">
    <div class="config-header">
      <h3>问答知识库配置</h3>
      <button class="config-close" @click="$emit('close')">
        <i class="fas fa-times"></i>
      </button>
    </div>
    
    <div class="config-content">
      <!-- 智能配置 -->
      <div class="config-section">
        <h4>智能配置</h4>
        <div class="config-grid">
          <div class="config-item">
            <label>AI模型选择</label>
            <select v-model="config.aiModel">
              <option value="gpt-3.5">GPT-3.5 Turbo</option>
              <option value="gpt-4">GPT-4</option>
              <option value="claude">Claude</option>
              <option value="custom">自定义模型</option>
            </select>
          </div>
          
          <div class="config-item">
            <label>响应温度</label>
            <div class="slider-container">
              <input 
                type="range" 
                min="0" 
                max="2" 
                step="0.1" 
                v-model="config.temperature"
                class="slider"
              >
              <span class="slider-value">{{ config.temperature }}</span>
            </div>
          </div>
          
          <div class="config-item">
            <label>最大响应长度</label>
            <input 
              type="number" 
              v-model="config.maxLength" 
              min="100" 
              max="2000"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>知识匹配阈值</label>
            <div class="slider-container">
              <input 
                type="range" 
                min="0.1" 
                max="1" 
                step="0.1" 
                v-model="config.matchThreshold"
                class="slider"
              >
              <span class="slider-value">{{ config.matchThreshold }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 自动回复规则 -->
      <div class="config-section">
        <h4>自动回复规则</h4>
        <div class="config-grid">
          <div class="config-item">
            <label>启用自动回复</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="autoReply"
                v-model="config.autoReplyEnabled"
              >
              <label for="autoReply"></label>
            </div>
          </div>
          
          <div class="config-item">
            <label>回复延迟(秒)</label>
            <input 
              type="number" 
              v-model="config.replyDelay" 
              min="0" 
              max="10"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>最大重试次数</label>
            <input 
              type="number" 
              v-model="config.maxRetries" 
              min="1" 
              max="5"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>启用智能推荐</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="smartRecommend"
                v-model="config.smartRecommend"
              >
              <label for="smartRecommend"></label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 知识库设置 -->
      <div class="config-section">
        <h4>知识库设置</h4>
        <div class="config-grid">
          <div class="config-item">
            <label>知识更新频率</label>
            <select v-model="config.updateFrequency">
              <option value="realtime">实时更新</option>
              <option value="hourly">每小时</option>
              <option value="daily">每日</option>
              <option value="weekly">每周</option>
            </select>
          </div>
          
          <div class="config-item">
            <label>知识库备份</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="backupEnabled"
                v-model="config.backupEnabled"
              >
              <label for="backupEnabled"></label>
            </div>
          </div>
          
          <div class="config-item">
            <label>数据保留天数</label>
            <input 
              type="number" 
              v-model="config.retentionDays" 
              min="7" 
              max="365"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>启用知识审核</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="reviewEnabled"
                v-model="config.reviewEnabled"
              >
              <label for="reviewEnabled"></label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 性能优化 -->
      <div class="config-section">
        <h4>性能优化</h4>
        <div class="config-grid">
          <div class="config-item">
            <label>缓存启用</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="cacheEnabled"
                v-model="config.cacheEnabled"
              >
              <label for="cacheEnabled"></label>
            </div>
          </div>
          
          <div class="config-item">
            <label>缓存过期时间(分钟)</label>
            <input 
              type="number" 
              v-model="config.cacheExpire" 
              min="5" 
              max="1440"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>并发请求限制</label>
            <input 
              type="number" 
              v-model="config.maxConcurrent" 
              min="1" 
              max="100"
              class="config-input"
            >
          </div>
          
          <div class="config-item">
            <label>启用负载均衡</label>
            <div class="toggle-switch">
              <input 
                type="checkbox" 
                id="loadBalance"
                v-model="config.loadBalance"
              >
              <label for="loadBalance"></label>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="config-footer">
      <button class="btn-secondary" @click="resetConfig">重置</button>
      <button class="btn-secondary" @click="testConfig">测试配置</button>
      <button class="btn-primary" @click="saveConfig">保存配置</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KnowledgeConfig',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      config: {
        // AI模型配置
        aiModel: 'gpt-3.5',
        temperature: 0.7,
        maxLength: 500,
        matchThreshold: 0.8,
        
        // 自动回复配置
        autoReplyEnabled: true,
        replyDelay: 2,
        maxRetries: 3,
        smartRecommend: true,
        
        // 知识库配置
        updateFrequency: 'realtime',
        backupEnabled: true,
        retentionDays: 90,
        reviewEnabled: false,
        
        // 性能配置
        cacheEnabled: true,
        cacheExpire: 30,
        maxConcurrent: 10,
        loadBalance: false
      }
    }
  },
  methods: {
    saveConfig() {
      // 保存配置到后端
      this.$message.success('配置保存成功')
      this.$emit('close')
    },
    
    resetConfig() {
      // 重置为默认配置
      this.config = {
        aiModel: 'gpt-3.5',
        temperature: 0.7,
        maxLength: 500,
        matchThreshold: 0.8,
        autoReplyEnabled: true,
        replyDelay: 2,
        maxRetries: 3,
        smartRecommend: true,
        updateFrequency: 'realtime',
        backupEnabled: true,
        retentionDays: 90,
        reviewEnabled: false,
        cacheEnabled: true,
        cacheExpire: 30,
        maxConcurrent: 10,
        loadBalance: false
      }
      this.$message.info('配置已重置')
    },
    
    testConfig() {
      // 测试配置
      this.$message.info('正在测试配置...')
      setTimeout(() => {
        this.$message.success('配置测试通过')
      }, 2000)
    }
  }
}
</script>

<style scoped>
.knowledge-config {
  background: white;
  border-radius: 0.75rem;
  width: 90%;
  max-width: 1000px;
  max-height: 90vh;
  overflow-y: auto;
}

.config-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.config-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
}

.config-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  color: #6b7280;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: all 0.2s ease;
}

.config-close:hover {
  background: #f3f4f6;
  color: #374151;
}

.config-content {
  padding: 1.5rem;
}

.config-section {
  margin-bottom: 2rem;
}

.config-section h4 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.config-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.config-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.config-item label {
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
}

.config-item select,
.config-input {
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.config-item select:focus,
.config-input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.slider-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.slider {
  flex: 1;
  height: 0.375rem;
  background: #d1d5db;
  border-radius: 0.1875rem;
  outline: none;
  -webkit-appearance: none;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 1.25rem;
  height: 1.25rem;
  background: #2563eb;
  border-radius: 50%;
  cursor: pointer;
}

.slider::-moz-range-thumb {
  width: 1.25rem;
  height: 1.25rem;
  background: #2563eb;
  border-radius: 50%;
  cursor: pointer;
  border: none;
}

.slider-value {
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  min-width: 2rem;
  text-align: center;
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

.config-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  padding: 1.5rem;
  border-top: 1px solid #e5e7eb;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .knowledge-config {
    width: 95%;
    margin: 1rem;
  }
  
  .config-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .config-footer {
    flex-direction: column;
  }
  
  .config-footer button {
    width: 100%;
    justify-content: center;
  }
}
</style> 