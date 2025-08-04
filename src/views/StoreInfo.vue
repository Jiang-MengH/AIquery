<template>
  <div class="storeinfo">
    <h1 class="page-title">店铺信息</h1>
    <p class="page-subtitle">管理店铺基本信息</p>
    
    <div class="card">
      <h2 class="card-title">店铺信息</h2>
      <p class="card-subtitle">管理店铺基本信息功能正在开发中...</p>
      
      <div class="content">
        <p>该功能模块正在开发中，敬请期待...</p>
        <button class="btn-primary">
          <i class="fas fa-plus mr-2"></i>添加内容
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StoreInfo'
}
</script>

<style scoped>
.storeinfo {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 2rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.page-subtitle {
  color: #6b7280;
  margin-bottom: 2rem;
}

.card {
  background: white;
  border-radius: 0.75rem;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.card-subtitle {
  color: #6b7280;
  margin-bottom: 2rem;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.content p {
  color: #6b7280;
  font-size: 1.125rem;
}
</style>