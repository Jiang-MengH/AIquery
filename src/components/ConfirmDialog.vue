<template>
  <div v-if="visible" class="dialog-overlay" @click="handleOverlayClick">
    <div class="dialog-container" @click.stop>
      <div class="dialog-header">
        <h3 class="dialog-title">{{ title }}</h3>
        <button class="dialog-close" @click="handleCancel">
          <i class="fas fa-times"></i>
        </button>
      </div>
      
      <div class="dialog-body">
        <div class="dialog-icon" :class="type">
          <i :class="iconClass"></i>
        </div>
        <p class="dialog-message">{{ message }}</p>
      </div>
      
      <div class="dialog-footer">
        <button class="btn-secondary" @click="handleCancel">
          {{ cancelText }}
        </button>
        <button class="btn-primary" :class="confirmButtonClass" @click="handleConfirm">
          {{ confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmDialog',
  props: {
    visible: Boolean,
    title: {
      type: String,
      default: '确认操作'
    },
    message: String,
    type: {
      type: String,
      default: 'warning'
    },
    confirmText: {
      type: String,
      default: '确认'
    },
    cancelText: {
      type: String,
      default: '取消'
    }
  },
  computed: {
    iconClass() {
      const icons = {
        warning: 'fas fa-exclamation-triangle',
        danger: 'fas fa-times-circle',
        info: 'fas fa-info-circle'
      }
      return icons[this.type] || icons.warning
    },
    confirmButtonClass() {
      return this.type === 'danger' ? 'btn-danger' : ''
    }
  },
  methods: {
    handleConfirm() {
      this.$emit('confirm')
    },
    handleCancel() {
      this.$emit('cancel')
    },
    handleOverlayClick() {
      this.handleCancel()
    }
  }
}
</script>

<style scoped>
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 1rem;
}

.dialog-container {
  background: white;
  border-radius: 0.75rem;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  max-width: 500px;
  width: 100%;
}

.dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 1.5rem 0;
}

.dialog-title {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.dialog-close {
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
}

.dialog-close:hover {
  background: #f3f4f6;
  color: #6b7280;
}

.dialog-body {
  padding: 1.5rem;
  text-align: center;
}

.dialog-icon {
  margin-bottom: 1rem;
  font-size: 3rem;
}

.dialog-icon.warning {
  color: #f59e0b;
}

.dialog-icon.danger {
  color: #ef4444;
}

.dialog-icon.info {
  color: #3b82f6;
}

.dialog-message {
  font-size: 1rem;
  color: #374151;
  margin: 0;
  line-height: 1.5;
}

.dialog-footer {
  display: flex;
  gap: 0.75rem;
  padding: 0 1.5rem 1.5rem;
  justify-content: flex-end;
}

.btn-secondary {
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
}

.btn-secondary:hover {
  background: #e5e7eb;
}

.btn-primary {
  background: #3b82f6;
  color: white;
  border: 1px solid #3b82f6;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-danger {
  background: #ef4444;
  border-color: #ef4444;
}

.btn-danger:hover {
  background: #dc2626;
}
</style> 