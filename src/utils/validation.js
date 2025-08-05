// 表单验证工具函数
export const validators = {
  // 必填验证
  required: (value) => {
    if (value === null || value === undefined) return '此字段为必填项'
    if (typeof value === 'string' && value.trim() === '') return '此字段为必填项'
    if (Array.isArray(value) && value.length === 0) return '此字段为必填项'
    return null
  },

  // 邮箱验证
  email: (value) => {
    if (!value) return null
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) return '请输入有效的邮箱地址'
    return null
  },

  // 手机号验证
  phone: (value) => {
    if (!value) return null
    const phoneRegex = /^1[3-9]\d{9}$/
    if (!phoneRegex.test(value)) return '请输入有效的手机号码'
    return null
  },

  // 长度验证
  length: (min, max) => (value) => {
    if (!value) return null
    const len = value.length
    if (min && len < min) return `最少需要 ${min} 个字符`
    if (max && len > max) return `最多允许 ${max} 个字符`
    return null
  },

  // 数字验证
  number: (value) => {
    if (!value) return null
    if (isNaN(Number(value))) return '请输入有效的数字'
    return null
  },

  // 范围验证
  range: (min, max) => (value) => {
    if (!value) return null
    const num = Number(value)
    if (isNaN(num)) return '请输入有效的数字'
    if (min !== undefined && num < min) return `数值不能小于 ${min}`
    if (max !== undefined && num > max) return `数值不能大于 ${max}`
    return null
  },

  // URL验证
  url: (value) => {
    if (!value) return null
    try {
      new URL(value)
      return null
    } catch {
      return '请输入有效的URL地址'
    }
  },

  // 自定义正则验证
  pattern: (regex, message) => (value) => {
    if (!value) return null
    if (!regex.test(value)) return message || '格式不正确'
    return null
  }
}

// 验证单个字段
export const validateField = (value, rules) => {
  if (!rules || rules.length === 0) return null
  
  for (const rule of rules) {
    let validator
    let params = []
    
    if (typeof rule === 'string') {
      validator = validators[rule]
    } else if (typeof rule === 'function') {
      validator = rule
    } else if (rule.validator) {
      validator = rule.validator
      params = rule.params || []
    } else {
      continue
    }
    
    if (validator) {
      const result = validator(value, ...params)
      if (result) return result
    }
  }
  
  return null
}

// 验证整个表单
export const validateForm = (formData, validationRules) => {
  const errors = {}
  let isValid = true
  
  for (const [field, rules] of Object.entries(validationRules)) {
    const error = validateField(formData[field], rules)
    if (error) {
      errors[field] = error
      isValid = false
    }
  }
  
  return { isValid, errors }
}

// 实时验证（防抖）
export const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 创建防抖验证器
export const createDebouncedValidator = (validator, delay = 300) => {
  return debounce(validator, delay)
} 