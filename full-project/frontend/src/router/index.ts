import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue'),
      meta: { title: '注册' }
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      children: [
        {
          path: '/fund-research',
          name: 'fund-research',
          component: () => import('../views/FundResearch.vue'),
          meta: { title: '基金研究' }
        },
        {
          path: '/factor-management',
          name: 'factor-management',
          component: () => import('../views/FactorManagement.vue'),
          meta: { title: '因子管理' }
        },
        {
          path: '/strategy-management',
          name: 'strategy-management',
          component: () => import('../views/StrategyManagement.vue'),
          meta: { title: '策略管理' }
        },
        {
          path: '/portfolio-management',
          name: 'portfolio-management',
          component: () => import('../views/PortfolioManagement.vue'),
          meta: { title: '组合产品管理' }
        },
        {
          path: '/user-management',
          name: 'user-management',
          component: () => import('../views/UserManagement.vue'),
          meta: { title: '交易管理',
		   requiresAdmin: true  // 添加管理员权限标记
		   }
        },
        {
          path: '/Real-time-monitoring',
          name: 'Real-time-monitoring',
          component: () => import('../views/RealTimeMonitoring.vue'),
          meta: { title: '实时监控' ,
            requiresAdmin: true
          }
        },
      ]
    }
  ]
})

// 添加全局路由守卫
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    // 检查是否是管理员
    const isAdmin = localStorage.getItem('isAdmin') === 'true'
    if (!isAdmin) {
      ElMessage.error('无权访问该模块')
      next('/home') // 重定向到首页或其他页面
    } else {
      next()
    }
  } else {
    next()
  }
})



export default router
