<template>
  <transition name="message-fade">
    <div 
      v-if="visible"
      class="message"
      :class="type"
    >
      <i :class="iconClass"></i>
      <span class="message-text">{{ message }}</span>
      <button class="message-close" @click="close">
        <i class="fas fa-times"></i>
      </button>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'Message',
  props: {
    message: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'info',
      validator: value => ['success', 'warning', 'error', 'info'].includes(value)
    },
    duration: {
      type: Number,
      default: 3000
    }
  },
  data() {
    return {
      visible: false
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
      return icons[this.type]
    }
  },
  mounted() {
    this.show()
  },
  methods: {
    show() {
      this.visible = true
      if (this.duration > 0) {
        setTimeout(() => {
          this.close()
        }, this.duration)
      }
    },
    close() {
      this.visible = false
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 1rem 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  gap: 0.75rem;
  z-index: 1000;
  min-width: 300px;
  max-width: 500px;
}

.message.success {
  background: #dcfce7;
  color: #166534;
  border: 1px solid #bbf7d0;
}

.message.warning {
  background: #fef3c7;
  color: #92400e;
  border: 1px solid #fde68a;
}

.message.error {
  background: #fef2f2;
  color: #dc2626;
  border: 1px solid #fecaca;
}

.message.info {
  background: #dbeafe;
  color: #1e40af;
  border: 1px solid #bfdbfe;
}

.message-text {
  flex: 1;
  font-size: 0.875rem;
  font-weight: 500;
}

.message-close {
  background: none;
  border: none;
  color: inherit;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.25rem;
  transition: all 0.2s ease;
}

.message-close:hover {
  background: rgba(0, 0, 0, 0.1);
}

.message-fade-enter-active,
.message-fade-leave-active {
  transition: all 0.3s ease;
}

.message-fade-enter,
.message-fade-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style> 