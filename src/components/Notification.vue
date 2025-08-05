<template>
  <div v-if="visible" class="notification" :class="type">
    <div class="notification-icon">
      <i :class="iconClass"></i>
    </div>
    <div class="notification-content">
      <h4 class="notification-title">{{ title }}</h4>
      <p class="notification-message">{{ message }}</p>
    </div>
    <button class="notification-close" @click="close">
      <i class="fas fa-times"></i>
    </button>
  </div>
</template>

<script>
export default {
  name: 'Notification',
  props: {
    visible: Boolean,
    title: String,
    message: String,
    type: {
      type: String,
      default: 'info'
    }
  },
  computed: {
    iconClass() {
      const icons = {
        success: 'fas fa-check-circle',
        warning: 'fas fa-exclamation-triangle',
        error: 'fas fa-times-circle',
        info: 'fas fa-info-circle'
      }
      return icons[this.type] || icons.info
    }
  },
  methods: {
    close() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
.notification {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 0.75rem;
  background: white;
  border-left: 4px solid;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.notification.success {
  border-left-color: #10b981;
}

.notification.warning {
  border-left-color: #f59e0b;
}

.notification.error {
  border-left-color: #ef4444;
}

.notification.info {
  border-left-color: #3b82f6;
}

.notification-icon {
  font-size: 1.25rem;
}

.notification.success .notification-icon {
  color: #10b981;
}

.notification.warning .notification-icon {
  color: #f59e0b;
}

.notification.error .notification-icon {
  color: #ef4444;
}

.notification.info .notification-icon {
  color: #3b82f6;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 0.25rem 0;
}

.notification-message {
  font-size: 0.75rem;
  color: #6b7280;
  margin: 0;
}

.notification-close {
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.25rem;
}

.notification-close:hover {
  background: #f3f4f6;
  color: #6b7280;
}
</style> 