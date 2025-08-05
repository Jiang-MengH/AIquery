<template>
  <div class="form-field">
    <label v-if="label" class="form-label">
      {{ label }}
      <span v-if="required" class="required">*</span>
    </label>
    
    <input
      :type="type"
      :value="value"
      :placeholder="placeholder"
      class="form-input"
      :class="{ 'error': hasError }"
      @input="$emit('input', $event.target.value)"
      @blur="validate"
    />
    
    <div v-if="hasError" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormField',
  props: {
    value: String,
    type: {
      type: String,
      default: 'text'
    },
    label: String,
    placeholder: String,
    required: Boolean,
    rules: Array
  },
  data() {
    return {
      errorMessage: '',
      isTouched: false
    }
  },
  computed: {
    hasError() {
      return this.isTouched && this.errorMessage
    }
  },
  methods: {
    validate() {
      this.isTouched = true
      if (this.required && !this.value) {
        this.errorMessage = '此字段为必填项'
      } else {
        this.errorMessage = ''
      }
    }
  }
}
</script>

<style scoped>
.form-field {
  margin-bottom: 1rem;
}

.form-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  margin-bottom: 0.5rem;
}

.required {
  color: #ef4444;
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
}

.form-input:focus {
  outline: none;
  border-color: #3b82f6;
}

.form-input.error {
  border-color: #ef4444;
}

.error-message {
  margin-top: 0.5rem;
  font-size: 0.75rem;
  color: #ef4444;
}
</style> 