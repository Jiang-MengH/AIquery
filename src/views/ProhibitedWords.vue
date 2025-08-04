<template>
  <div class="prohibited-words">
    <!-- 顶部面包屑和统计 -->
    <div class="top-bar">
      <div class="breadcrumb">
        <span>内容管理</span> &gt; <span>违禁词管理</span>
      </div>
      <div class="top-actions">
        <span class="total-count">共 {{ totalCount }} 个违禁词</span>
        <button class="btn-primary" @click="openCommonWordsDialog">
          <i class="fas fa-list mr-2"></i>常用违禁词
        </button>
      </div>
    </div>

    <!-- 违禁词列表 -->
    <div class="words-container">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <div class="search-input">
          <i class="fas fa-search"></i>
          <input 
            v-model="searchKeyword" 
            placeholder="搜索违禁词..." 
            class="input"
            @input="filterWords"
          />
        </div>
        <button class="btn-add" @click="openAddWordDialog">
          <i class="fas fa-plus mr-2"></i>添加违禁词
        </button>
      </div>

      <!-- 违禁词列表 -->
      <div class="words-list">
        <div v-for="(word, idx) in filteredWords" :key="idx" class="word-item">
          <span class="word-text">{{ word }}</span>
          <button class="btn-delete" @click="deleteWord(idx)">
            <i class="fas fa-trash"></i>
          </button>
        </div>
        <div v-if="filteredWords.length === 0" class="empty-state">
          <i class="fas fa-search"></i>
          <p>暂无违禁词</p>
        </div>
      </div>
    </div>

    <!-- 底部清空按钮 -->
    <div class="footer-bar">
      <button class="btn-clear" @click="openClearConfirm">
        <i class="fas fa-trash-alt mr-2"></i>清空所有违禁词
      </button>
    </div>

    <!-- 添加违禁词弹窗 -->
    <div v-if="showAddDialog" class="dialog-mask">
      <div class="dialog">
        <h3>添加违禁词</h3>
        <div class="form-row">
          <label>违禁词：</label>
          <input 
            v-model="newWord" 
            class="input" 
            placeholder="请输入违禁词" 
            @keyup.enter="addWord"
          />
        </div>
        <div class="dialog-actions">
          <button class="btn-cancel" @click="closeAddDialog">取消</button>
          <button class="btn-save" @click="addWord">添加</button>
        </div>
      </div>
    </div>

    <!-- 常用违禁词弹窗 -->
    <div v-if="showCommonWordsDialog" class="dialog-mask">
      <div class="dialog large-dialog">
        <h3>选择常用违禁词</h3>
        <div class="common-words-section">
          <h4>常用违禁词</h4>
          <div class="common-words-grid">
            <label v-for="word in commonWords" :key="word" class="checkbox-item">
              <input 
                type="checkbox" 
                :value="word" 
                v-model="selectedCommonWords"
              />
              <span>{{ word }}</span>
            </label>
          </div>
        </div>
        <div class="custom-word-section">
          <h4>自定义违禁词</h4>
          <div class="custom-input">
            <input 
              v-model="customWord" 
              class="input" 
              placeholder="输入自定义违禁词，回车添加" 
              @keyup.enter="addCustomWord"
            />
            <button class="btn-add-small" @click="addCustomWord">添加</button>
          </div>
          <div class="custom-words-list">
            <span v-for="(word, idx) in customWords" :key="idx" class="word-chip">
              {{ word }}
              <span class="chip-close" @click="removeCustomWord(idx)">&times;</span>
            </span>
          </div>
        </div>
        <div class="dialog-actions">
          <button class="btn-cancel" @click="closeCommonWordsDialog">取消</button>
          <button class="btn-save" @click="addCommonWords">确定添加</button>
        </div>
      </div>
    </div>

    <!-- 清空确认弹窗 -->
    <div v-if="showClearConfirm" class="dialog-mask">
      <div class="dialog">
        <h3>确定清空所有违禁词吗？</h3>
        <p class="warning-text">此操作将删除所有违禁词，且无法恢复！</p>
        <div class="dialog-actions">
          <button class="btn-cancel" @click="showClearConfirm = false">取消</button>
          <button class="btn-danger" @click="clearAllWords">确认清空</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProhibitedWords',
  data() {
    return {
      // 违禁词列表
      words: [
        '色情', '赌博', '毒品', '暴力', '政治敏感',
        '诈骗', '传销', '非法集资', '虚假广告', '侵权'
      ],
      // 搜索关键词
      searchKeyword: '',
      // 过滤后的违禁词列表
      filteredWords: [],
      // 弹窗状态
      showAddDialog: false,
      showCommonWordsDialog: false,
      showClearConfirm: false,
      // 新增违禁词
      newWord: '',
      // 常用违禁词
      commonWords: [
        '色情', '赌博', '毒品', '暴力', '政治敏感',
        '诈骗', '传销', '非法集资', '虚假广告', '侵权',
        '恐怖主义', '极端主义', '民族歧视', '宗教歧视',
        '性别歧视', '年龄歧视', '地域歧视', '职业歧视'
      ],
      selectedCommonWords: [],
      customWords: [],
      customWord: ''
    }
  },
  computed: {
    totalCount() {
      return this.words.length
    }
  },
  mounted() {
    this.filterWords()
  },
  methods: {
    // 过滤违禁词
    filterWords() {
      if (!this.searchKeyword.trim()) {
        this.filteredWords = [...this.words]
      } else {
        this.filteredWords = this.words.filter(word => 
          word.toLowerCase().includes(this.searchKeyword.toLowerCase())
        )
      }
    },
    // 打开添加违禁词弹窗
    openAddWordDialog() {
      this.showAddDialog = true
      this.newWord = ''
    },
    // 关闭添加弹窗
    closeAddDialog() {
      this.showAddDialog = false
      this.newWord = ''
    },
    // 添加违禁词
    addWord() {
      const word = this.newWord.trim()
      if (word && !this.words.includes(word)) {
        this.words.push(word)
        this.filterWords()
        this.closeAddDialog()
      } else if (this.words.includes(word)) {
        alert('该违禁词已存在')
      }
    },
    // 删除违禁词
    deleteWord(idx) {
      const word = this.filteredWords[idx]
      const originalIdx = this.words.indexOf(word)
      if (originalIdx > -1) {
        this.words.splice(originalIdx, 1)
        this.filterWords()
      }
    },
    // 打开常用违禁词弹窗
    openCommonWordsDialog() {
      this.showCommonWordsDialog = true
      this.selectedCommonWords = []
      this.customWords = []
      this.customWord = ''
    },
    // 关闭常用违禁词弹窗
    closeCommonWordsDialog() {
      this.showCommonWordsDialog = false
      this.selectedCommonWords = []
      this.customWords = []
      this.customWord = ''
    },
    // 添加自定义违禁词
    addCustomWord() {
      const word = this.customWord.trim()
      if (word && !this.customWords.includes(word) && !this.commonWords.includes(word)) {
        this.customWords.push(word)
        this.customWord = ''
      } else if (this.customWords.includes(word)) {
        alert('该违禁词已添加')
      } else if (this.commonWords.includes(word)) {
        alert('该违禁词已在常用列表中')
      }
    },
    // 移除自定义违禁词
    removeCustomWord(idx) {
      this.customWords.splice(idx, 1)
    },
    // 添加常用违禁词
    addCommonWords() {
      const allWords = [...this.selectedCommonWords, ...this.customWords]
      const newWords = allWords.filter(word => !this.words.includes(word))
      
      if (newWords.length > 0) {
        this.words.push(...newWords)
        this.filterWords()
        this.closeCommonWordsDialog()
        alert(`成功添加 ${newWords.length} 个违禁词`)
      } else {
        alert('没有新的违禁词需要添加')
      }
    },
    // 打开清空确认弹窗
    openClearConfirm() {
      this.showClearConfirm = true
    },
    // 清空所有违禁词
    clearAllWords() {
      this.words = []
      this.filterWords()
      this.showClearConfirm = false
      alert('已清空所有违禁词')
    }
  }
}
</script>

<style scoped>
.prohibited-words {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem 0 3rem 0;
  min-height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  flex-shrink: 0;
}

.breadcrumb {
  color: #6b7280;
  font-size: 0.875rem;
}

.top-actions {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.total-count {
  font-size: 1rem;
  color: #2563eb;
  margin-right: 1rem;
}

.btn-primary {
  background: #2563eb;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.5rem 1rem;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 0.875rem;
}

.words-container {
  background: #fff;
  border-radius: 0.75rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  overflow: hidden;
  margin-bottom: 1rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
}

.search-input {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-input i {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
}

.search-input input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.btn-add {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 0.875rem;
}

.words-list {
  padding: 0.5rem 1.5rem;
  flex: 1;
  overflow-y: auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 0.5rem;
  align-content: start;
}

.word-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  border: 1px solid #f3f4f6;
  border-radius: 0.5rem;
  transition: all 0.2s ease;
  background: #fff;
}

.word-item:hover {
  border-color: #2563eb;
  box-shadow: 0 2px 8px rgba(37, 102, 235, 0.1);
}

.word-item:last-child {
  border-bottom: none;
}

.word-text {
  font-size: 0.875rem;
  color: #374151;
  flex: 1;
  font-weight: 500;
}

.btn-delete {
  background: none;
  border: none;
  color: #ef4444;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s;
  margin-left: 0.5rem;
  font-size: 0.875rem;
}

.btn-delete:hover {
  background: #fef2f2;
}

.empty-state {
  text-align: center;
  padding: 2rem 1.5rem;
  color: #9ca3af;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  grid-column: 1 / -1;
}

.empty-state i {
  font-size: 2.5rem;
  margin-bottom: 0.75rem;
}

.empty-state p {
  font-size: 0.875rem;
  color: #6b7280;
}

.footer-bar {
  margin-top: 1rem;
  text-align: center;
  flex-shrink: 0;
}

.btn-clear {
  background: #ef4444;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 2rem;
  font-weight: 500;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  font-size: 0.875rem;
}

.dialog-mask {
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

.dialog {
  background: #fff;
  border-radius: 0.75rem;
  padding: 1.5rem;
  min-width: 400px;
  max-width: 90vw;
  max-height: 90vh;
  overflow-y: auto;
}

.large-dialog {
  min-width: 600px;
}

.dialog h3 {
  margin-bottom: 1.5rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
}

.form-row {
  margin-bottom: 1.25rem;
}

.form-row label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
}

.input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.dialog-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: flex-end;
  margin-top: 1.5rem;
}

.btn-cancel {
  background: #6b7280;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  cursor: pointer;
  font-size: 0.875rem;
}

.btn-save {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  cursor: pointer;
  font-size: 0.875rem;
}

.btn-danger {
  background: #ef4444;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  font-weight: 500;
  cursor: pointer;
  font-size: 0.875rem;
}

.warning-text {
  color: #ef4444;
  font-weight: 500;
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

.common-words-section,
.custom-word-section {
  margin-bottom: 1.5rem;
}

.common-words-section h4,
.custom-word-section h4 {
  margin-bottom: 0.75rem;
  font-weight: 600;
  color: #374151;
  font-size: 1rem;
}

.common-words-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 0.5rem;
}

.checkbox-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s;
  font-size: 0.875rem;
}

.checkbox-item:hover {
  background: #f9fafb;
}

.checkbox-item input[type="checkbox"] {
  margin: 0;
}

.custom-input {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}

.btn-add-small {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 1rem;
  font-weight: 500;
  cursor: pointer;
  white-space: nowrap;
  font-size: 0.875rem;
}

.custom-words-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.word-chip {
  background: #e5e7eb;
  color: #374151;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.chip-close {
  cursor: pointer;
  font-weight: bold;
  color: #6b7280;
}

.chip-close:hover {
  color: #ef4444;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .prohibited-words {
    padding: 0.5rem 0 3rem 0;
  }
  
  .top-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .search-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .search-input {
    max-width: 100%;
  }
  
  .words-list {
    grid-template-columns: 1fr;
    gap: 0.25rem;
  }
  
  .word-item {
    padding: 0.5rem;
  }
  
  .dialog {
    min-width: 90vw;
    margin: 1rem;
  }
  
  .large-dialog {
    min-width: 90vw;
  }
  
  .common-words-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
}
</style>