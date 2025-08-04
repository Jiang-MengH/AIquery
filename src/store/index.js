import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 用户信息
    user: {
      name: '踏雪飞燕',
      phone: '13950645054',
      avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face'
    },
    // 导航菜单状态
    navigation: {
      activeMenu: 'dashboard',
      collapsed: false,
      // 二级导航展开状态
      expandedMenus: {
        'knowledge': false,  // 店铺知识库
        'products': false    // 商品知识库
      }
    },
    // 通知消息
    notifications: [
      {
        id: 1,
        type: 'warning',
        title: '系统维护通知',
        content: '系统将于今晚22:00-24:00进行维护升级',
        time: '2024-01-15 10:30',
        read: false
      },
      {
        id: 2,
        type: 'info',
        title: '新功能上线',
        content: 'AI智能优化功能已上线，欢迎体验',
        time: '2024-01-15 09:15',
        read: false
      },
      {
        id: 3,
        type: 'success',
        title: '数据同步完成',
        content: '商品数据同步已完成，共更新1234条记录',
        time: '2024-01-15 08:45',
        read: true
      }
    ],
    // 统计数据
    statistics: {
      todaySessions: 1234,
      avgResponseTime: 2.3,
      problemSolveRate: 89,
      pendingTickets: 23
    },
    // 平台接入状态
    platforms: [
      {
        name: '淘宝/天猫',
        icon: 'fab fa-alipay',
        color: 'text-blue-600',
        status: 'online',
        shops: 3,
        sessions: 156
      },
      {
        name: '拼多多',
        icon: 'fas fa-shopping-cart',
        color: 'text-red-600',
        status: 'online',
        shops: 2,
        sessions: 89
      },
      {
        name: '抖音小店',
        icon: 'fab fa-tiktok',
        color: 'text-black',
        status: 'online',
        shops: 1,
        sessions: 67
      },
      {
        name: '小红书',
        icon: 'fas fa-heart',
        color: 'text-pink-600',
        status: 'online',
        shops: 1,
        sessions: 34
      }
    ]
  },
  mutations: {
    // 设置当前激活菜单
    SET_ACTIVE_MENU(state, menu) {
      state.navigation.activeMenu = menu
    },
    // 切换侧边栏折叠状态
    TOGGLE_SIDEBAR(state) {
      state.navigation.collapsed = !state.navigation.collapsed
    },
    // 切换二级导航展开状态
    TOGGLE_SUBMENU(state, menuKey) {
      state.navigation.expandedMenus[menuKey] = !state.navigation.expandedMenus[menuKey]
    },
    // 设置二级导航展开状态
    SET_SUBMENU_EXPANDED(state, { menuKey, expanded }) {
      state.navigation.expandedMenus[menuKey] = expanded
    },
    // 标记通知为已读
    MARK_NOTIFICATION_READ(state, notificationId) {
      const notification = state.notifications.find(n => n.id === notificationId)
      if (notification) {
        notification.read = true
      }
    },
    // 添加新通知
    ADD_NOTIFICATION(state, notification) {
      state.notifications.unshift({
        id: Date.now(),
        ...notification,
        read: false
      })
    },
    // 更新统计数据
    UPDATE_STATISTICS(state, statistics) {
      state.statistics = { ...state.statistics, ...statistics }
    }
  },
  actions: {
    // 切换菜单
    switchMenu({ commit }, menu) {
      commit('SET_ACTIVE_MENU', menu)
    },
    // 切换侧边栏
    toggleSidebar({ commit }) {
      commit('TOGGLE_SIDEBAR')
    },
    // 切换二级导航
    toggleSubmenu({ commit }, menuKey) {
      commit('TOGGLE_SUBMENU', menuKey)
    },
    // 设置二级导航展开状态
    setSubmenuExpanded({ commit }, { menuKey, expanded }) {
      commit('SET_SUBMENU_EXPANDED', { menuKey, expanded })
    },
    // 标记通知已读
    markNotificationRead({ commit }, notificationId) {
      commit('MARK_NOTIFICATION_READ', notificationId)
    },
    // 添加通知
    addNotification({ commit }, notification) {
      commit('ADD_NOTIFICATION', notification)
    },
    // 更新统计数据
    updateStatistics({ commit }, statistics) {
      commit('UPDATE_STATISTICS', statistics)
    }
  },
  getters: {
    // 获取未读通知数量
    unreadNotificationsCount: state => {
      return state.notifications.filter(n => !n.read).length
    },
    // 获取未读通知列表
    unreadNotifications: state => {
      return state.notifications.filter(n => !n.read)
    },
    // 获取在线平台数量
    onlinePlatformsCount: state => {
      return state.platforms.filter(p => p.status === 'online').length
    }
  }
}) 