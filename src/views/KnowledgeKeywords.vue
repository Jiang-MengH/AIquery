<template>
  <div class="knowledge-keywords">
    <!-- 顶部面包屑和统计 -->
    <div class="top-bar">
      <div class="breadcrumb">
        <span>训练语料库</span> &gt; <span>店铺知识库</span> &gt; <span>关键词知识库</span>
      </div>
      <div class="top-actions">
        <span class="total-count">共 {{ totalCount }} 个关键词</span>
        <button class="btn-primary" @click="openAddDialog">
          <i class="fas fa-plus mr-2"></i>添加关键词
        </button>
      </div>
    </div>

    <!-- 关键词组卡片区 -->
    <div class="group-list">
      <div v-for="(group, idx) in groups" :key="group.keyword_group" class="group-card">
        <div class="group-header">
          <div class="group-title">
            <span class="group-name">{{ group.keyword_group }}</span>
            <span class="hit-count">近7日命中：{{ group.hit_count }}</span>
          </div>
          <div class="group-ops">
            <button class="btn-edit" @click="openEditDialog(idx)"><i class="fas fa-edit"></i> 编辑</button>
            <button class="btn-delete" @click="confirmDelete(idx)"><i class="fas fa-trash"></i> 删除</button>
            <button class="btn-toggle" @click="toggleExpand(idx)">
              <i :class="['fas', group.expanded ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
            </button>
          </div>
        </div>
        <transition name="fade">
        <div v-show="group.expanded" class="group-body">
          <div class="keywords-list">
            <span v-for="(kw, kidx) in group.keywords" :key="kidx" class="keyword-chip">
              {{ kw }}
              <span class="chip-close" @click="removeKeywordFromGroup(idx, kidx)">&times;</span>
            </span>
          </div>
          <div class="answer-block">
            <span class="answer-label">应答内容：</span>
            <span v-if="editAnswerIdx !== idx" class="answer-text" @click="startEditAnswer(idx)">{{ group.answer }}</span>
            <textarea v-else v-model="answerEditValue" class="textarea answer-edit" @blur="saveAnswerEdit(idx)" @keyup.enter="saveAnswerEdit(idx)" />
          </div>
        </div>
        </transition>
      </div>
    </div>

    <!-- 底部保存按钮 -->
    <div class="footer-bar">
      <button class="btn-save" @click="saveAll">保存</button>
    </div>

    <!-- 预留弹窗 -->
    <div v-if="showAddDialog || showEditDialog" class="dialog-mask">
      <div class="dialog">
        <h3>{{ showAddDialog ? '添加关键词组' : '编辑关键词组' }}</h3>
        <div class="form-row">
          <label>关键词组名称：</label>
          <input v-model="editForm.keyword_group" class="input" placeholder="请输入关键词组名称" />
        </div>
        <div class="form-row">
          <label>关键词：</label>
          <div class="keywords-input">
            <input v-model="keywordInput" class="input" placeholder="输入关键词，回车添加" @keyup.enter="addKeyword" />
            <div class="keyword-chip" v-for="(kw, kidx) in editForm.keywords" :key="kidx">
              {{ kw }}
              <span class="chip-close" @click="removeKeyword(kidx)">&times;</span>
            </div>
          </div>
        </div>
        <div class="form-row">
          <label>应答内容：</label>
          <textarea v-model="editForm.answer" class="textarea" placeholder="请输入应答内容"></textarea>
        </div>
        <div class="dialog-actions">
          <button class="btn-cancel" @click="closeDialog">取消</button>
          <button class="btn-save" @click="saveGroup">保存</button>
        </div>
      </div>
    </div>
    <div v-if="showDeleteConfirm" class="dialog-mask">
      <div class="dialog">
        <h3>确定删除该关键词组吗？删除后将无法恢复！</h3>
        <button class="btn-danger" @click="deleteGroup">确认删除</button>
        <button class="btn-cancel" @click="showDeleteConfirm=false">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'KnowledgeKeywords',
  data() {
    return {
      groups: [
        {
          keyword_group: '退货政策',
          keywords: ['退货', '退款', '退货政策'],
          answer: '退货申请流程：请提供订单号并告知退货原因。',
          hit_count: 25,
          expanded: true
        },
        {
          keyword_group: '运费补偿',
          keywords: ['运费', '补偿', '快递费'],
          answer: '如因质量问题产生运费，可联系客服补偿。',
          hit_count: 12,
          expanded: false
        }
      ],
      showAddDialog: false,
      showEditDialog: false,
      showDeleteConfirm: false,
      deleteIdx: null,
      // 新增/编辑表单
      editForm: {
        keyword_group: '',
        keywords: [],
        answer: ''
      },
      keywordInput: '',
      editIdx: null,
      // 新增
      editAnswerIdx: null,
      answerEditValue: ''
    }
  },
  computed: {
    totalCount() {
      return this.groups.reduce((sum, g) => sum + g.keywords.length, 0)
    }
  },
  methods: {
    openAddDialog() {
      this.showAddDialog = true
      this.editForm = { keyword_group: '', keywords: [], answer: '' }
      this.keywordInput = ''
      this.editIdx = null
    },
    openEditDialog(idx) {
      const g = this.groups[idx]
      this.editForm = {
        keyword_group: g.keyword_group,
        keywords: [...g.keywords],
        answer: g.answer
      }
      this.keywordInput = ''
      this.editIdx = idx
      this.showEditDialog = true
    },
    addKeyword() {
      const kw = this.keywordInput.trim()
      if (kw && !this.editForm.keywords.includes(kw)) {
        this.editForm.keywords.push(kw)
      }
      this.keywordInput = ''
    },
    removeKeyword(kidx) {
      this.editForm.keywords.splice(kidx, 1)
    },
    removeKeywordFromGroup(idx, kidx) {
      this.groups[idx].keywords.splice(kidx, 1)
    },
    startEditAnswer(idx) {
      this.editAnswerIdx = idx
      this.answerEditValue = this.groups[idx].answer
      this.$nextTick(() => {
        const ta = document.querySelector('.answer-edit')
        if (ta) ta.focus()
      })
    },
    saveAnswerEdit(idx) {
      if (this.answerEditValue.trim()) {
        this.groups[idx].answer = this.answerEditValue.trim()
      }
      this.editAnswerIdx = null
      this.answerEditValue = ''
    },
    closeDialog() {
      this.showAddDialog = false
      this.showEditDialog = false
      this.editForm = { keyword_group: '', keywords: [], answer: '' }
      this.keywordInput = ''
      this.editIdx = null
    },
    saveGroup() {
      if (!this.editForm.keyword_group.trim()) {
        alert('请输入关键词组名称')
        return
      }
      if (!this.editForm.keywords.length) {
        alert('请至少输入一个关键词')
        return
      }
      if (!this.editForm.answer.trim()) {
        alert('请输入应答内容')
        return
      }
      if (this.editIdx === null) {
        // 新增
        this.groups.push({
          ...this.editForm,
          hit_count: 0,
          expanded: false
        })
      } else {
        // 编辑
        this.groups[this.editIdx] = {
          ...this.groups[this.editIdx],
          ...this.editForm
        }
      }
      this.closeDialog()
    },
    confirmDelete(idx) {
      this.deleteIdx = idx
      this.showDeleteConfirm = true
    },
    deleteGroup() {
      if (this.deleteIdx !== null) {
        this.groups.splice(this.deleteIdx, 1)
        this.deleteIdx = null
        this.showDeleteConfirm = false
      }
    },
    toggleExpand(idx) {
      this.groups[idx].expanded = !this.groups[idx].expanded
    },
    saveAll() {
      alert('保存功能开发中')
    }
  }
}
</script>

<style scoped>
.knowledge-keywords {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem 0 3rem 0;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
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

.group-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.group-card {
  background: #fff;
  border-radius: 0.75rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  overflow: hidden;
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  background: #f9fafb;
}

.group-title {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.group-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
}

.hit-count {
  font-size: 0.875rem;
  color: #6b7280;
  background: #e5e7eb;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
}

.group-ops {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-edit, .btn-delete, .btn-toggle {
  background: none;
  border: none;
  padding: 0.5rem;
  border-radius: 0.25rem;
  cursor: pointer;
  font-size: 0.875rem;
  transition: background-color 0.2s;
}

.btn-edit {
  color: #2563eb;
}

.btn-delete {
  color: #ef4444;
}

.btn-toggle {
  color: #6b7280;
}

.btn-edit:hover, .btn-delete:hover, .btn-toggle:hover {
  background: #f3f4f6;
}

.group-body {
  padding: 1.5rem;
}

.keywords-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.keyword-chip {
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
  font-size: 1rem;
}

.chip-close:hover {
  color: #ef4444;
}

.answer-block {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
}

.answer-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
  white-space: nowrap;
  margin-top: 0.25rem;
}

.answer-text {
  flex: 1;
  font-size: 0.875rem;
  color: #374151;
  line-height: 1.5;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s;
}

.answer-text:hover {
  background: #f9fafb;
}

.answer-edit {
  flex: 1;
  font-size: 0.875rem;
  color: #374151;
  line-height: 1.5;
  border: 1px solid #d1d5db;
  border-radius: 0.25rem;
  padding: 0.5rem;
  resize: vertical;
  min-height: 2.5rem;
}

.footer-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-top: 1px solid #e5e7eb;
  padding: 1rem 2rem;
  display: flex;
  justify-content: center;
  z-index: 100;
}

.btn-save {
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 0.5rem;
  padding: 0.75rem 2rem;
  font-weight: 500;
  cursor: pointer;
  font-size: 1rem;
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
  min-width: 500px;
  max-width: 90vw;
  max-height: 90vh;
  overflow-y: auto;
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

.keywords-input {
  position: relative;
}

.keywords-input .input {
  margin-bottom: 0.75rem;
}

.textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  resize: vertical;
  min-height: 4rem;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .knowledge-keywords {
    padding: 0.5rem 0 4rem 0;
  }
  
  .top-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .group-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
  
  .group-title {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
  
  .group-ops {
    width: 100%;
    justify-content: flex-end;
  }
  
  .dialog {
    min-width: 90vw;
    margin: 1rem;
  }
  
  .footer-bar {
    padding: 0.75rem 1rem;
  }
}
</style> 