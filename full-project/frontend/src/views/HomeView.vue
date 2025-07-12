<script setup lang="ts">
import { ref } from 'vue'
import { useRouter,useRoute } from 'vue-router'

let route = useRoute()
let id = ref(route.query.id)

const router = useRouter()
const activeIndex = ref('1')
router.push({path:'/fund-research',query:{id:id.value}})
const handleSelect = (key: string) => {
  switch (key) {
    case '1':
      router.push({path:'/fund-research',query:{id:id.value}})
      break
    case '2':
      router.push({path:'/factor-management',query:{id:id.value}})
      break
    case '3':
      router.push({path:'/strategy-management',query:{id:id.value}})
      break
    case '4':
      router.push({path:'/portfolio-management',query:{id:id.value}})
      break
    case '5':
      router.push({path:'/user-management',query:{id:id.value}})
      break
    case '6':
      router.push({path:'/Real-time-monitoring'})
      break
    case '7':
      router.push('/')
      break
    default:
      break
  }
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="160px" class="sidebar">
        <div class="sidebar-header">
          <span class="system-title">智能投顾</span>
        </div>
        <el-menu
          active-text-color="#1890ff"
          background-color="#e6f7ff"
          class="el-menu-vertical"
          :default-active="activeIndex"
          text-color="#333"
          @select="handleSelect"
        >
          <el-menu-item index="1">
            <span class="menu-text">基金研究</span>
          </el-menu-item>
          <el-menu-item index="2">
            <span class="menu-text">因子管理</span>
          </el-menu-item>
          <el-menu-item index="3">
            <span class="menu-text">策略管理</span>
          </el-menu-item>
          <el-menu-item index="4">
            <span class="menu-text">组合产品管理</span>
          </el-menu-item>
          <el-menu-item index="5">
            <span class="menu-text">用户管理</span>
          </el-menu-item>
          <el-menu-item index="6" class="Real-time-monitoring-item">
            <span class="menu-text">实时监控</span>
          </el-menu-item>
          <el-menu-item index="7" class="logout-item">
            <span class="menu-text">退出登录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="system-title">智能投顾系统</div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.common-layout {
  height: 100vh;
  display: flex;
}

.sidebar {
  background-color: #e6f7ff;
  border-right: 1px solid #d9e6f2;
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 160px !important;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #1890ff;
  flex-shrink: 0;
}

.el-menu-vertical {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  padding: 15px 0;
  background-color: #e6f7ff !important;
  border-right: none !important;
}

.el-menu-item {
  height: auto !important;
  padding: 12px 0 !important;
  margin: 6px 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}

.menu-text {
  font-size: 16px;
  font-weight: 500;
  white-space: nowrap;
}

.el-menu-item:hover {
  background-color: #d0e8ff !important;
}

.el-menu-item.is-active {
  background-color: #b3d8ff !important;
  color: #1890ff !important;
}

.logout-item {
  margin-top: auto;
  background-color: #f8aeae !important;
  color: #fff !important;
}

.logout-item .menu-text {
  color: #fff !important;
}

.logout-item:hover {
  background-color: #b71c1c !important;
}

.header {
  background-color: #e6f7ff;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 60px !important;
}

.system-title {
  font-family: 'Microsoft YaHei', 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  letter-spacing: 1px;
}

.header .system-title {
  color: #1890ff;
  margin-left: 20px;
  font-size: 20px;
}
</style>