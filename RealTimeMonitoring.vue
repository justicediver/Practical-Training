<template>
  <div class="monitor-container">
    <h2>实时用户监控</h2>
    <div class="stats-panel">
      <el-card>
        <div slot="header">
          <span>在线用户统计</span>
        </div>
        <div class="stats-content">
          <p>当前在线用户数: {{ onlineUsers.length }}</p>
          <el-button type="primary" @click="refreshOnlineUsers">刷新列表</el-button>
        </div>
      </el-card>
    </div>
    
    <div class="user-list">
      <el-table :data="onlineUsers" style="width: 100%">
        <el-table-column prop="id" label="用户ID" width="180"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="mini" @click="viewUserDetails(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog 
      :title="`用户详情 - ${currentUser?.username || '未知用户'}`" 
      v-model="dialogVisible" 
      width="80%"
      top="5vh"
      destroy-on-close
    >
      <div v-if="currentUser" class="user-detail-container">
        <div class="user-basic">
          <el-descriptions title="基本信息" :column="3" border>
            <el-descriptions-item label="用户ID">{{ currentUser.id }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
            <el-descriptions-item label="角色">基金投资顾问</el-descriptions-item>
            <el-descriptions-item label="在线时长">{{ currentUser.onlineTime }} 小时</el-descriptions-item>
            <el-descriptions-item label="活跃度">
              <el-rate v-model="currentUser.activityLevel" disabled show-score />
            </el-descriptions-item>
            <el-descriptions-item label="最后操作">{{ currentUser.lastActivity }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="behavior-stats">
          <h3>行为统计</h3>
          <div class="chart-row">
            <div class="chart-container">
              <h4>行为类型分布</h4>
              <div ref="behaviorChart" style="width: 100%; height: 300px;"></div>
            </div>
            <div class="chart-container">
              <h4>时段活跃度</h4>
              <div ref="activityChart" style="width: 100%; height: 300px;"></div>
            </div>
          </div>
          
          <div class="chart-row">
            <div class="chart-container">
              <h4>基金选择分布</h4>
              <div ref="fundChart" style="width: 100%; height: 300px;"></div>
            </div>
            <div class="chart-container">
              <h4>因子使用分布</h4>
              <div ref="factorChart" style="width: 100%; height: 300px;"></div>
            </div>
          </div>
        </div>
        
        <div class="user-profile">
          <h3>用户画像标签</h3>
          <div class="tag-group">
            <el-tag 
              v-for="tag in currentUser.profileTags" 
              :key="tag" 
              :type="getTagType(tag)"
              size="large"
              class="profile-tag"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { ElNotification } from 'element-plus';
import * as echarts from 'echarts';

const router = useRouter();
const onlineUsers = ref<any[]>([]);
let socket: WebSocket | null = null;

const dialogVisible = ref(false);
const currentUser = ref<any>(null);

const behaviorChart = ref<HTMLElement | null>(null);
const activityChart = ref<HTMLElement | null>(null);
const fundChart = ref<HTMLElement | null>(null);
const factorChart = ref<HTMLElement | null>(null);
let behaviorChartInstance: echarts.ECharts | null = null;
let activityChartInstance: echarts.ECharts | null = null;
let fundChartInstance: echarts.ECharts | null = null;
let factorChartInstance: echarts.ECharts | null = null;

const tagTypes = ['', 'success', 'info', 'warning', 'danger'];

const getCurrentTime = () => {
  const now = new Date();
  const hours = now.getHours().toString().padStart(2, '0');
  const minutes = now.getMinutes().toString().padStart(2, '0');
  const seconds = now.getSeconds().toString().padStart(2, '0');
  return `今天 ${hours}:${minutes}:${seconds}`;
};

const connectWebSocket = () => {
  const adminId = localStorage.getItem('adminId') || '0';
  
  const wsUrl = `ws://localhost:8080/ws/online-users?userId=${adminId}`;
  socket = new WebSocket(wsUrl);

  socket.onopen = () => {
    console.log('WebSocket连接已建立');
    fetchOnlineUsers();
  };

  socket.onmessage = (event) => {
    try {
      onlineUsers.value = JSON.parse(event.data);
    } catch (e) {
      console.error('解析WebSocket消息失败:', e);
    }
  };

  socket.onclose = () => {
    console.log('WebSocket连接已关闭');
    setTimeout(connectWebSocket, 5000);
  };

  socket.onerror = (error) => {
    console.error('WebSocket错误:', error);
  };
};

const fetchOnlineUsers = async () => {
  try {
    const response = await fetch('http://localhost:8080/online-users');
    const data = await response.json();
    
    const adminExists = data.some((user: any) => user.id === 0);
    if (!adminExists) {
      data.push({ id: 0, username: 'root' });
    }

    onlineUsers.value = data.map((user: any) => ({
      ...user,
      activityLevel: Math.floor(Math.random() * 3) + 3,
      onlineTime: (Math.random() * 10).toFixed(1),
      lastActivity: getCurrentTime(),
      profileTags: generateProfileTags()
    }));
  } catch (error) {
    console.error('获取在线用户失败:', error);
    ElNotification({
      title: '错误',
      message: '获取在线用户数据失败',
      type: 'error',
    });
  }
};

const generateProfileTags = () => {
  const tags = [];
  const riskTypes = ['保守型', '稳健型', '平衡型', '进取型', '激进型'];
  const expertise = ['基金筛选专家', '因子组合大师', '资产配置能手', '新手投顾', '资深投顾'];
  const styles = ['高频交易型', '长期持有型', '价值投资型', '趋势交易型'];
  
  tags.push(riskTypes[Math.floor(Math.random() * riskTypes.length)]);
  tags.push(expertise[Math.floor(Math.random() * expertise.length)]);
  tags.push(styles[Math.floor(Math.random() * styles.length)]);
  
  return tags;
};

const getTagType = (tag: string) => {
  return tagTypes[tag.length % tagTypes.length];
};

const initCharts = () => {
  nextTick(() => {
    behaviorChartInstance?.dispose();
    activityChartInstance?.dispose();
    fundChartInstance?.dispose();
    factorChartInstance?.dispose();
    
    if (behaviorChart.value) {
      behaviorChartInstance = echarts.init(behaviorChart.value);
      renderBehaviorChart();
    }
    
    if (activityChart.value) {
      activityChartInstance = echarts.init(activityChart.value);
      renderActivityChart();
    }
    
    if (fundChart.value) {
      fundChartInstance = echarts.init(fundChart.value);
      renderFundChart();
    }
    
    if (factorChart.value) {
      factorChartInstance = echarts.init(factorChart.value);
      renderFactorChart();
    }
  });
};

const renderBehaviorChart = () => {
  if (!behaviorChartInstance) return;
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [
      {
        name: '行为类型',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['40%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 124, name: '页面浏览' },
          { value: 67, name: '按钮点击' },
          { value: 48, name: '基金筛选' },
          { value: 36, name: '因子选择' },
          { value: 22, name: '组合创建' },
          { value: 18, name: '表单提交' }
        ]
      }
    ]
  };
  
  behaviorChartInstance.setOption(option);
};

const renderActivityChart = () => {
  if (!activityChartInstance) return;
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['9:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00'],
      axisLine: {
        lineStyle: {
          color: '#999'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: '操作次数',
      axisLine: {
        show: true,
        lineStyle: {
          color: '#999'
        }
      }
    },
    series: [
      {
        name: '操作次数',
        type: 'bar',
        barWidth: '60%',
        data: [15, 8, 12, 25, 37, 20, 13],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  };
  
  activityChartInstance.setOption(option);
};

const renderFundChart = () => {
  if (!fundChartInstance) return;
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      axisLine: {
        lineStyle: {
          color: '#999'
        }
      }
    },
    yAxis: {
      type: 'category',
      data: ['货币基金', '债券基金', '混合基金', '股票基金', '指数基金'],
      axisLine: {
        lineStyle: {
          color: '#999'
        }
      }
    },
    series: [
      {
        name: '选择次数',
        type: 'bar',
        data: [12, 28, 45, 38, 32],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
            { offset: 0, color: '#ff9f7f' },
            { offset: 1, color: '#f759ab' }
          ])
        }
      }
    ]
  };
  
  fundChartInstance.setOption(option);
};

const renderFactorChart = () => {
  if (!factorChartInstance) return;
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: ['估值因子', '质量因子', '动量因子', '波动率因子']
    },
    series: [
      {
        name: '使用比例',
        type: 'pie',
        radius: ['40%', '55%'],
        center: ['60%', '50%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 8
        },
        data: [
          { value: 30, name: '估值因子' },
          { value: 20, name: '质量因子' },
          { value: 15, name: '动量因子' },
          { value: 5, name: '波动率因子' }
        ]
      }
    ]
  };
  
  factorChartInstance.setOption(option);
};

const refreshOnlineUsers = () => {
  fetchOnlineUsers();
};

const viewUserDetails = (user: any) => {
  currentUser.value = user;
  dialogVisible.value = true;
  
  setTimeout(initCharts, 100);
};

onMounted(() => {
  connectWebSocket();
});

onUnmounted(() => {
  if (socket) {
    socket.close();
  }
  
  behaviorChartInstance?.dispose();
  activityChartInstance?.dispose();
  fundChartInstance?.dispose();
  factorChartInstance?.dispose();
});
</script>

<style scoped>
.monitor-container {
  padding: 20px;
}

.stats-panel {
  margin-bottom: 20px;
}

.stats-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-list {
  margin-top: 20px;
}

.user-detail-container {
  max-height: 75vh;
  overflow-y: auto;
}

.user-basic {
  margin-bottom: 20px;
}

.behavior-stats {
  margin: 30px 0;
}

.chart-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-container {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-container h4 {
  margin-top: 0;
  text-align: center;
  color: #606266;
}

.user-profile {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.profile-tag {
  font-size: 14px;
  padding: 8px 15px;
  border-radius: 20px;
}
</style>