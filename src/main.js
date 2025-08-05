import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import interaction from './utils/interaction.js'

// 引入Font Awesome
import '@fortawesome/fontawesome-free/css/all.css'

Vue.config.productionTip = false

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