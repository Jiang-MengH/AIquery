<template>
  <div class="layout-container">
    <!-- 侧边栏导航 -->
    <aside 
      class="sidebar"
      :class="{ 'collapsed': isCollapsed }"
    >
      <!-- Logo区域 -->
      <div class="logo-section">
        <div class="logo-content">
          <i class="fas fa-robot text-2xl text-blue-600 mr-3"></i>
          <h1 class="text-xl font-bold text-gray-800" v-show="!isCollapsed">AI客服助手</h1>
        </div>
        <p class="text-sm text-gray-500 mt-1" v-show="!isCollapsed">训练语料库</p>
      </div>
      
      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <div class="nav-section" v-show="!isCollapsed">
          <span class="nav-section-title">主要功能</span>
        </div>
        
        <!-- 数据统计 -->
        <router-link 
          to="/" 
          class="nav-item"
          :class="{ 'active': activeMenu === 'dashboard' }"
          @click.native="switchMenu('dashboard')"
        >
          <i class="fas fa-chart-bar mr-3"></i>
          <span v-show="!isCollapsed">数据统计</span>
        </router-link>
        
        <!-- AI智能优化 -->
        <router-link 
          to="/ai-optimization" 
          class="nav-item"
          :class="{ 'active': activeMenu === 'ai-optimization' }"
          @click.native="switchMenu('ai-optimization')"
        >
          <i class="fas fa-brain mr-3"></i>
          <span v-show="!isCollapsed">AI智能优化</span>
        </router-link>
        
        <!-- 店铺知识库 -->
        <div class="nav-section" v-show="!isCollapsed">
          <span class="nav-section-title">知识管理</span>
        </div>
        
        <div class="nav-group">
          <div 
            class="nav-item nav-group-header"
            @click="toggleSubmenu('knowledge')"
            :class="{ 'active': isKnowledgeActive }"
          >
            <i class="fas fa-store mr-3"></i>
            <span v-show="!isCollapsed">店铺知识库</span>
            <i 
              class="fas ml-auto text-sm transition-transform duration-200"
              :class="[
                isKnowledgeExpanded ? 'fa-chevron-up' : 'fa-chevron-down',
                isCollapsed ? 'hidden' : ''
              ]"
            ></i>
          </div>
          
          <div 
            class="sub-nav-container"
            :class="{ 'expanded': isKnowledgeExpanded }"
          >
            <router-link 
              to="/knowledge-qa" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'knowledge-qa' }"
              @click.native="switchMenu('knowledge-qa')"
            >
              <i class="fas fa-comments mr-3"></i>
              <span v-show="!isCollapsed">问答知识库</span>
            </router-link>
            
            <router-link 
              to="/knowledge-keywords" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'knowledge-keywords' }"
              @click.native="switchMenu('knowledge-keywords')"
            >
              <i class="fas fa-key mr-3"></i>
              <span v-show="!isCollapsed">关键词知识库</span>
            </router-link>
            
            <router-link 
              to="/store-info" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'store-info' }"
              @click.native="switchMenu('store-info')"
            >
              <i class="fas fa-info-circle mr-3"></i>
              <span v-show="!isCollapsed">店铺信息</span>
            </router-link>
            
            <router-link 
              to="/welcome-message" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'welcome-message' }"
              @click.native="switchMenu('welcome-message')"
            >
              <i class="fas fa-hand-paper mr-3"></i>
              <span v-show="!isCollapsed">欢迎语</span>
            </router-link>
            
            <router-link 
              to="/prohibited-words" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'prohibited-words' }"
              @click.native="switchMenu('prohibited-words')"
            >
              <i class="fas fa-ban mr-3"></i>
              <span v-show="!isCollapsed">违禁词管理</span>
            </router-link>
            
            <router-link 
              to="/scenario-solutions" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'scenario-solutions' }"
              @click.native="switchMenu('scenario-solutions')"
            >
              <i class="fas fa-lightbulb mr-3"></i>
              <span v-show="!isCollapsed">场景解决方案</span>
            </router-link>
          </div>
        </div>
        
        <!-- 商品知识库 -->
        <div class="nav-section" v-show="!isCollapsed">
          <span class="nav-section-title">商品管理</span>
        </div>
        
        <div class="nav-group">
          <div 
            class="nav-item nav-group-header"
            @click="toggleSubmenu('products')"
            :class="{ 'active': isProductsActive }"
          >
            <i class="fas fa-box mr-3"></i>
            <span v-show="!isCollapsed">商品知识库</span>
            <i 
              class="fas ml-auto text-sm transition-transform duration-200"
              :class="[
                isProductsExpanded ? 'fa-chevron-up' : 'fa-chevron-down',
                isCollapsed ? 'hidden' : ''
              ]"
            ></i>
          </div>
          
          <div 
            class="sub-nav-container"
            :class="{ 'expanded': isProductsExpanded }"
          >
            <router-link 
              to="/products" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'products' }"
              @click.native="switchMenu('products')"
            >
              <i class="fas fa-cube mr-3"></i>
              <span v-show="!isCollapsed">商品管理</span>
            </router-link>
            
            <router-link 
              to="/product-templates" 
              class="sub-nav-item"
              :class="{ 'active': activeMenu === 'product-templates' }"
              @click.native="switchMenu('product-templates')"
            >
              <i class="fas fa-layer-group mr-3"></i>
              <span v-show="!isCollapsed">商品模板管理</span>
            </router-link>
          </div>
        </div>
        
        <!-- 自动回复 -->
        <router-link 
          to="/auto-reply" 
          class="nav-item"
          :class="{ 'active': activeMenu === 'auto-reply' }"
          @click.native="switchMenu('auto-reply')"
        >
          <i class="fas fa-reply mr-3"></i>
          <span v-show="!isCollapsed">自动回复</span>
        </router-link>
        
        <!-- 设置 -->
        <router-link 
          to="/settings" 
          class="nav-item"
          :class="{ 'active': activeMenu === 'settings' }"
          @click.native="switchMenu('settings')"
        >
          <i class="fas fa-cog mr-3"></i>
          <span v-show="!isCollapsed">设置</span>
        </router-link>
      </nav>
      
      <!-- 用户信息 -->
      <div class="user-section">
        <div class="user-info">
          <img :src="user.avatar" alt="用户头像" class="user-avatar">
          <div class="user-details" v-show="!isCollapsed">
            <p class="user-name">{{ user.name }}</p>
            <p class="user-phone">{{ user.phone }}</p>
          </div>
          <i class="fas fa-user text-gray-400 ml-auto" v-show="!isCollapsed"></i>
        </div>
      </div>
    </aside>

    <!-- 主内容区域 -->
    <div class="main-content" :class="{ 'expanded': isCollapsed }">
      <!-- 顶部操作区 -->
      <header class="top-header">
        <div class="header-left">
          <button 
            class="sidebar-toggle"
            @click="toggleSidebar"
            :title="isCollapsed ? '展开侧边栏' : '收起侧边栏'"
          >
            <i class="fas fa-bars"></i>
          </button>
          <div class="breadcrumb">
            <span class="breadcrumb-item">{{ currentPageTitle }}</span>
            <span class="breadcrumb-separator" v-if="currentPageSubtitle">/</span>
            <span class="breadcrumb-item" v-if="currentPageSubtitle">{{ currentPageSubtitle }}</span>
          </div>
        </div>
        
        <div class="header-right">
          <!-- 搜索框 -->
          <div class="search-container">
            <input 
              type="text" 
              placeholder="搜索..." 
              class="search-input"
              v-model="searchKeyword"
              @keyup.enter="handleSearch"
            >
            <i class="fas fa-search search-icon"></i>
          </div>
          
          <!-- 通知按钮 -->
          <div class="notification-container">
            <button 
              class="notification-btn"
              @click="toggleNotificationPanel"
              :title="`${unreadCount} 条未读通知`"
            >
              <i class="fas fa-bell"></i>
              <span class="notification-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
            </button>
            
            <!-- 通知面板 -->
            <div class="notification-panel" v-show="showNotificationPanel">
              <div class="notification-header">
                <h3>通知消息</h3>
                <button @click="markAllAsRead">全部标记为已读</button>
              </div>
              <div class="notification-list">
                <div 
                  v-for="notification in notifications" 
                  :key="notification.id"
                  class="notification-item"
                  :class="{ 'unread': !notification.read }"
                  @click="markAsRead(notification.id)"
                >
                  <div class="notification-icon">
                    <i :class="getNotificationIcon(notification.type)"></i>
                  </div>
                  <div class="notification-content">
                    <h4>{{ notification.title }}</h4>
                    <p>{{ notification.content }}</p>
                    <span class="notification-time">{{ notification.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 用户菜单 -->
          <div class="user-menu">
            <img :src="user.avatar" alt="用户头像" class="user-avatar-small">
            <span class="user-name-small">{{ user.name }}</span>
            <i class="fas fa-chevron-down"></i>
          </div>
        </div>
      </header>
      
      <!-- 页面内容 -->
      <main class="page-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from 'vuex'

export default {
  name: 'Layout',
  data() {
    return {
      searchKeyword: '',
      showNotificationPanel: false
    }
  },
  computed: {
    ...mapState(['user', 'navigation', 'notifications']),
    ...mapGetters(['unreadNotificationsCount']),
    
    // 当前激活菜单
    activeMenu() {
      return this.navigation.activeMenu
    },
    
    // 侧边栏折叠状态
    isCollapsed() {
      return this.navigation.collapsed
    },
    
    // 未读通知数量
    unreadCount() {
      return this.unreadNotificationsCount
    },
    
    // 当前页面标题
    currentPageTitle() {
      return this.$route.meta.title || '数据统计'
    },
    
    // 当前页面副标题
    currentPageSubtitle() {
      return this.$route.meta.subtitle || ''
    },

    // 知识管理二级导航展开状态
    isKnowledgeExpanded() {
      return this.navigation.expandedMenus.knowledge
    },

    // 商品管理二级导航展开状态
    isProductsExpanded() {
      return this.navigation.expandedMenus.products
    },

    // 知识管理导航项是否激活
    isKnowledgeActive() {
      return this.activeMenu === 'knowledge-qa' ||
             this.activeMenu === 'knowledge-keywords' ||
             this.activeMenu === 'store-info' ||
             this.activeMenu === 'welcome-message' ||
             this.activeMenu === 'prohibited-words' ||
             this.activeMenu === 'scenario-solutions'
    },

    // 商品管理导航项是否激活
    isProductsActive() {
      return this.activeMenu === 'products' ||
             this.activeMenu === 'product-templates'
    }
  },
  methods: {
    ...mapActions(['switchMenu', 'toggleSidebar', 'markNotificationRead', 'toggleSubmenu', 'setSubmenuExpanded']),
    
    // 处理搜索
    handleSearch() {
      if (this.searchKeyword.trim()) {
        this.$message.info(`搜索关键词: ${this.searchKeyword}`)
        this.searchKeyword = ''
      }
    },
    
    // 切换通知面板
    toggleNotificationPanel() {
      this.showNotificationPanel = !this.showNotificationPanel
    },
    
    // 标记通知为已读
    markAsRead(notificationId) {
      this.markNotificationRead(notificationId)
    },
    
    // 全部标记为已读
    markAllAsRead() {
      this.notifications.forEach(notification => {
        if (!notification.read) {
          this.markNotificationRead(notification.id)
        }
      })
    },
    

    
    // 获取通知图标
    getNotificationIcon(type) {
      const icons = {
        success: 'fas fa-check-circle text-green-500',
        warning: 'fas fa-exclamation-triangle text-yellow-500',
        error: 'fas fa-times-circle text-red-500',
        info: 'fas fa-info-circle text-blue-500'
      }
      return icons[type] || icons.info
    },
    
    // 根据当前路由自动展开对应的二级导航
    autoExpandSubmenu() {
      const routeName = this.$route.name
      if (routeName) {
        const menuName = routeName.toLowerCase()
        // 知识管理相关页面
        if (['knowledgeqa', 'knowledgekeywords', 'storeinfo', 'welcomemessage', 'prohibitedwords', 'scenariosolutions'].includes(menuName)) {
          this.setSubmenuExpanded({ menuKey: 'knowledge', expanded: true })
        }
        // 商品管理相关页面
        else if (['products', 'producttemplates'].includes(menuName)) {
          this.setSubmenuExpanded({ menuKey: 'products', expanded: true })
        }
      }
    }
  },
  mounted() {
    // 根据路由设置当前激活菜单
    const routeName = this.$route.name
    if (routeName) {
      this.switchMenu(routeName.toLowerCase())
    }
    // 根据当前路由自动展开对应的二级导航
    this.autoExpandSubmenu()
  },
  watch: {
    $route(to) {
      // 路由变化时更新激活菜单
      const routeName = to.name
      if (routeName) {
        this.switchMenu(routeName.toLowerCase())
      }
      // 根据新路由自动展开对应的二级导航
      this.autoExpandSubmenu()
    }
  }
}
</script>

<style scoped>
/* 布局容器 */
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

/* 侧边栏 */
.sidebar {
  width: 250px;
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  z-index: 50;
}

.sidebar.collapsed {
  width: 64px;
}

/* Logo区域 */
.logo-section {
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.logo-content {
  display: flex;
  align-items: center;
}

/* 导航菜单 */
.nav-menu {
  flex: 1;
  padding: 1rem 0;
  overflow-y: auto;
}

.nav-section {
  padding: 0 1rem 0.5rem;
  margin-bottom: 0.5rem;
}

.nav-section-title {
  font-size: 0.75rem;
  font-weight: 600;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #6b7280;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
}

.nav-item:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.nav-item.active {
  background-color: #dbeafe;
  color: #2563eb;
  border-right: 3px solid #2563eb;
}

.nav-group-header {
  font-weight: 500;
}

.sub-nav-item {
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem 0.5rem 2rem;
  color: #6b7280;
  text-decoration: none;
  transition: all 0.3s ease;
  cursor: pointer;
  font-size: 0.875rem;
}

.sub-nav-item:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.sub-nav-item.active {
  background-color: #dbeafe;
  color: #2563eb;
}

/* 二级导航容器 */
.sub-nav-container {
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.3s ease-out;
}

.sub-nav-container.expanded {
  max-height: 1000px; /* 足够大的值，确保所有子项都可见 */
}

/* 用户信息区域 */
.user-section {
  padding: 1rem;
  border-top: 1px solid #e5e7eb;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  margin-right: 0.75rem;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: #1f2937;
}

.user-phone {
  font-size: 0.75rem;
  color: #6b7280;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  transition: margin-left 0.3s ease;
}

.main-content.expanded {
  margin-left: 0;
}

/* 顶部操作区 */
.top-header {
  background: white;
  border-bottom: 1px solid #e5e7eb;
  padding: 1rem 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.sidebar-toggle {
  background: none;
  border: none;
  padding: 0.5rem;
  margin-right: 1rem;
  color: #6b7280;
  cursor: pointer;
  border-radius: 0.375rem;
  transition: all 0.2s ease;
}

.sidebar-toggle:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.breadcrumb {
  display: flex;
  align-items: center;
  font-size: 0.875rem;
}

.breadcrumb-item {
  color: #6b7280;
}

.breadcrumb-separator {
  margin: 0 0.5rem;
  color: #d1d5db;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 搜索框 */
.search-container {
  position: relative;
}

.search-input {
  width: 300px;
  padding: 0.5rem 1rem 0.5rem 2.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
}

/* 通知区域 */
.notification-container {
  position: relative;
}

.notification-btn {
  position: relative;
  background: none;
  border: none;
  padding: 0.5rem;
  color: #6b7280;
  cursor: pointer;
  border-radius: 0.375rem;
  transition: all 0.2s ease;
}

.notification-btn:hover {
  background-color: #f3f4f6;
  color: #374151;
}

.notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background: #ef4444;
  color: white;
  font-size: 0.75rem;
  padding: 0.125rem 0.375rem;
  border-radius: 9999px;
  min-width: 1.25rem;
  text-align: center;
}

.notification-panel {
  position: absolute;
  top: 100%;
  right: 0;
  width: 400px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  z-index: 100;
  margin-top: 0.5rem;
}

.notification-header {
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notification-header h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
}

.notification-header button {
  background: none;
  border: none;
  color: #3b82f6;
  font-size: 0.875rem;
  cursor: pointer;
}

.notification-list {
  max-height: 400px;
  overflow-y: auto;
}

.notification-item {
  padding: 1rem;
  border-bottom: 1px solid #f3f4f6;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.notification-item:hover {
  background-color: #f9fafb;
}

.notification-item.unread {
  background-color: #eff6ff;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-item {
  display: flex;
  align-items: flex-start;
}

.notification-icon {
  margin-right: 0.75rem;
  margin-top: 0.125rem;
}

.notification-content h4 {
  font-size: 0.875rem;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.notification-content p {
  font-size: 0.75rem;
  color: #6b7280;
  margin-bottom: 0.5rem;
}

.notification-time {
  font-size: 0.75rem;
  color: #9ca3af;
}

/* 用户菜单 */
.user-menu {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s ease;
}

.user-menu:hover {
  background-color: #f3f4f6;
}

.user-avatar-small {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  margin-right: 0.5rem;
}

.user-name-small {
  font-size: 0.875rem;
  color: #374151;
  margin-right: 0.5rem;
}

/* 页面内容区域 */
.page-content {
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;
  background-color: #f5f5f5;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
  }
  
  .sidebar.show {
    transform: translateX(0);
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .search-input {
    width: 200px;
  }
}

@media (max-width: 768px) {
  .header-right {
    gap: 0.5rem;
  }
  
  .search-input {
    width: 150px;
  }
  
  .user-name-small {
    display: none;
  }
}
</style> 