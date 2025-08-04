import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 路由配置
const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { title: '数据统计' }
  },
  {
    path: '/ai-optimization',
    name: 'AIOptimization',
    component: () => import('@/views/AIOptimization.vue'),
    meta: { title: 'AI智能优化' }
  },
  {
    path: '/knowledge-qa',
    name: 'KnowledgeQA',
    component: () => import('@/views/KnowledgeQA.vue'),
    meta: { title: '问答知识库' }
  },
  {
    path: '/knowledge-keywords',
    name: 'KnowledgeKeywords',
    component: () => import('@/views/KnowledgeKeywords.vue'),
    meta: { title: '关键词知识库' }
  },
  {
    path: '/store-info',
    name: 'StoreInfo',
    component: () => import('@/views/StoreInfo.vue'),
    meta: { title: '店铺信息' }
  },
  {
    path: '/welcome-message',
    name: 'WelcomeMessage',
    component: () => import('@/views/WelcomeMessage.vue'),
    meta: { title: '欢迎语' }
  },
  {
    path: '/prohibited-words',
    name: 'ProhibitedWords',
    component: () => import('@/views/ProhibitedWords.vue'),
    meta: { title: '违禁词管理' }
  },
  {
    path: '/scenario-solutions',
    name: 'ScenarioSolutions',
    component: () => import('@/views/ScenarioSolutions.vue'),
    meta: { title: '场景解决方案' }
  },
  {
    path: '/products',
    name: 'Products',
    component: () => import('@/views/Products.vue'),
    meta: { title: '商品管理' }
  },
  {
    path: '/product-templates',
    name: 'ProductTemplates',
    component: () => import('@/views/ProductTemplates.vue'),
    meta: { title: '商品模板管理' }
  },
  {
    path: '/auto-reply',
    name: 'AutoReply',
    component: () => import('@/views/AutoReply.vue'),
    meta: { title: '自动回复' }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/Settings.vue'),
    meta: { title: '设置' }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - AI客服后台管理系统` : 'AI客服后台管理系统'
  next()
})

export default router 