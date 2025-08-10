import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import interaction from './utils/interaction.js'
import axios from 'axios'

// 引入Font Awesome
import '@fortawesome/fontawesome-free/css/all.css'

Vue.config.productionTip = false

// 配置axios
Vue.prototype.$http = axios
// 统一使用代理路径，避免端口冲突
axios.defaults.baseURL = process.env.NODE_ENV === 'production' ? '' : '/api'
axios.defaults.timeout = 10000
axios.defaults.headers.common['Content-Type'] = 'application/json'

// 注册全局交互工具
Vue.prototype.$interaction = interaction

// 注册全局组件
Vue.component('LoadingSpinner', () => import('./components/LoadingSpinner.vue'))
Vue.component('FormField', () => import('./components/FormField.vue'))
Vue.component('Notification', () => import('./components/Notification.vue'))

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app') 