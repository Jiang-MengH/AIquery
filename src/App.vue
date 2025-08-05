<template>
  <div id="app">
    <Layout />
    <Message 
      v-if="showMessage"
      :message="messageText"
      :type="messageType"
      @close="hideMessage"
    />
    <ConfirmDialog
      v-if="showConfirm"
      :visible="showConfirm"
      :title="confirmData.title"
      :message="confirmData.message"
      :type="confirmData.type"
      :confirm-text="confirmData.confirmText"
      :cancel-text="confirmData.cancelText"
      @confirm="handleConfirm"
      @cancel="handleCancel"
    />
  </div>
</template>

<script>
import Layout from '@/components/Layout.vue'
import Message from '@/components/Message.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

export default {
  name: 'App',
  components: {
    Layout,
    Message,
    ConfirmDialog
  },
  data() {
    return {
      showMessage: false,
      messageText: '',
      messageType: 'info',
      showConfirm: false,
      confirmData: {},
      confirmResolve: null
    }
  },
  mounted() {
    // 全局消息提示方法
    this.$message = {
      success: (text) => this.showMessageFunc(text, 'success'),
      error: (text) => this.showMessageFunc(text, 'error'),
      warning: (text) => this.showMessageFunc(text, 'warning'),
      info: (text) => this.showMessageFunc(text, 'info')
    }

    // 监听确认对话框事件
    window.addEventListener('show-confirm', this.handleShowConfirm)
  },
  beforeDestroy() {
    window.removeEventListener('show-confirm', this.handleShowConfirm)
  },
  methods: {
    showMessageFunc(text, type) {
      this.messageText = text
      this.messageType = type
      this.showMessage = true
    },
    hideMessage() {
      this.showMessage = false
    },
    handleShowConfirm(event) {
      const { resolve, ...data } = event.detail
      this.confirmData = {
        title: '确认操作',
        type: 'warning',
        confirmText: '确认',
        cancelText: '取消',
        ...data
      }
      this.confirmResolve = resolve
      this.showConfirm = true
    },
    handleConfirm() {
      this.showConfirm = false
      if (this.confirmResolve) {
        this.confirmResolve(true)
        this.confirmResolve = null
      }
    },
    handleCancel() {
      this.showConfirm = false
      if (this.confirmResolve) {
        this.confirmResolve(false)
        this.confirmResolve = null
      }
    }
  }
}
</script>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  background-color: #f5f5f5;
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

.slide-enter-active, .slide-leave-active {
  transition: transform 0.3s;
}

.slide-enter, .slide-leave-to {
  transform: translateX(-100%);
}

/* 通用按钮样式 */
.btn-primary {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-secondary {
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.btn-secondary:hover {
  background: #e5e7eb;
}

/* 通用卡片样式 */
.card {
  background: white;
  border-radius: 0.75rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

/* 通用表格样式 */
.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  text-align: left;
  padding: 0.75rem;
  font-weight: 500;
  color: #374151;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.table td {
  padding: 0.75rem;
  border-bottom: 1px solid #f3f4f6;
}

.table tr:hover {
  background: #f9fafb;
}

/* 通用表单样式 */
.form-group {
  margin-bottom: 1rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #374151;
}

.form-input {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* 通用徽章样式 */
.badge {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
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

.badge-info {
  background: #dbeafe;
  color: #1e40af;
}

/* 响应式工具类 */
@media (max-width: 768px) {
  .hidden-mobile {
    display: none !important;
  }
}

@media (min-width: 769px) {
  .hidden-desktop {
    display: none !important;
  }
}
</style> 