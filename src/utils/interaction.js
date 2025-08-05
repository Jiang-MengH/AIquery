// 交互工具类
class InteractionManager {
  constructor() {
    this.loadingStates = new Map()
  }

  // 显示加载状态
  showLoading(key = 'global', text = '加载中...') {
    this.loadingStates.set(key, { visible: true, text })
  }

  // 隐藏加载状态
  hideLoading(key = 'global') {
    this.loadingStates.set(key, { visible: false, text: '' })
  }

  // 显示消息提示
  showMessage(text, type = 'info') {
    if (window.$message) {
      window.$message[type](text)
    }
  }

  // 显示成功消息
  success(text) {
    this.showMessage(text, 'success')
  }

  // 显示错误消息
  error(text) {
    this.showMessage(text, 'error')
  }

  // 显示警告消息
  warning(text) {
    this.showMessage(text, 'warning')
  }

  // 显示信息消息
  info(text) {
    this.showMessage(text, 'info')
  }

  // 显示确认对话框
  confirm(options) {
    return new Promise((resolve) => {
      window.dispatchEvent(new CustomEvent('show-confirm', {
        detail: { ...options, resolve }
      }))
    })
  }

  // 复制到剪贴板
  async copyToClipboard(text) {
    try {
      await navigator.clipboard.writeText(text)
      this.success('已复制到剪贴板')
      return true
    } catch (err) {
      this.error('复制失败')
      return false
    }
  }

  // 防抖函数
  debounce(func, wait) {
    let timeout
    return function(...args) {
      clearTimeout(timeout)
      timeout = setTimeout(() => func.apply(this, args), wait)
    }
  }

  // 获取加载状态
  getLoadingState(key = 'global') {
    return this.loadingStates.get(key) || { visible: false, text: '' }
  }
}

// 创建全局实例
const interaction = new InteractionManager()

// 挂载到全局
window.$interaction = interaction

export default interaction 