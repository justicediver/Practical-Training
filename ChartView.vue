<template>
  <div class="chart-view">
    <!-- 图表展示区域 -->
    <div ref="chartDom" class="chart-container"></div>
    
    <!-- 隐藏的数据控制区域，保留功能但不可见 -->
    <div class="data-controls" style="display: none">
      <el-input v-model="newItemName" placeholder="数据项名称"></el-input>
      <el-input-number v-model="newItemValue" :min="1" :max="1000"></el-input-number>
      <el-button @click="addDataItem">添加</el-button>
    </div>
    
    <div class="color-controls" style="display: none">
      <div v-for="(color, index) in colors" :key="index" class="color-item">
        <span class="color-label">{{ chartData[index]?.name || '项目'+(index+1) }}</span>
        <el-color-picker v-model="colors[index]"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch } from 'vue';
import * as echarts from 'echarts';
import { ElButton, ElInput, ElInputNumber, ElColorPicker } from 'element-plus';

// 响应式数据
const chartDom = ref<HTMLElement | null>(null);
const chartInstance = ref<echarts.ECharts | null>(null);
const newItemName = ref('');
const newItemValue = ref(100);
const chartData = ref([
  { name: '股票', value: 850 },
  { name: '债券', value: 100 },
  { name: '现金', value: 50 }
]);
const colors = ref(['#5470C6', '#91CC75', '#EE6666', '#FAC858', '#73C0DE']);

// 添加数据项功能
const addDataItem = () => {
  if (!newItemName.value || newItemValue.value <= 0) return;
  
  chartData.value.push({
    name: newItemName.value,
    value: newItemValue.value
  });
  
  newItemName.value = '';
  newItemValue.value = 100;
  updateChart();
};

// 初始化图表
const initChart = () => {
  if (!chartDom.value) return;
  
  if (chartInstance.value) {
    chartInstance.value.dispose();
  }
  
  chartInstance.value = echarts.init(chartDom.value);
  updateChart();
  
  window.addEventListener('resize', handleResize);
};

const handleResize = () => {
  chartInstance.value?.resize();
};

const updateChart = () => {
  if (!chartInstance.value) return;
  
  const fontSize = 14;
  
  const option = {
    title: {
      text: '基金持仓明细',
      left: 'center',
      textStyle: {
        fontSize: fontSize + 2,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
      textStyle: {
        fontSize: fontSize
      }
    },
    legend: {
      type: 'scroll',
      orient: 'horizontal',
      bottom: 0,
      textStyle: {
        fontSize: fontSize - 2
      },
      data: chartData.value.map(item => item.name)
    },
    series: [{
      name: '持仓占比',
      type: 'pie',
      radius: ['18%', '75%'],
      center: ['50%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: {
        borderRadius: 5,
        borderColor: '#fff',
        borderWidth: 2,
        color: (params: any) => colors.value[params.dataIndex % colors.value.length]
      },
      label: {
        show: true,
        position: 'outside',
        formatter: '{b|{b}}\n{d}%',
        alignTo: 'edge',
        margin: 10,
        lineHeight: 16,
        rich: {
          b: {
            fontWeight: 'bold',
            fontSize: fontSize
          }
        }
      },
      labelLayout: {
        hideOverlap: true,
        moveOverlap: 'shiftY'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: fontSize + 2,
          fontWeight: 'bold'
        },
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      data: chartData.value,
      animationType: 'scale',
      animationEasing: 'elasticOut',
      animationDelay: (idx: number) => idx * 100
    }],
    textStyle: {
      fontSize: fontSize
    }
  };

  chartInstance.value.setOption(option);
};

// 监听数据变化
watch([chartData, colors], () => {
  updateChart();
});

onMounted(() => {
  nextTick(() => {
    initChart();
  });
});
</script>

<style scoped>
.chart-view {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.chart-container {
  width: 100%;
  height: 85vh;
  min-height: 600px;
  margin: 0 auto;
}

.data-controls {
  display: flex;
  gap: 10px;
  margin: 20px 0;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
}

.color-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 20px;
  justify-content: center;
}

.color-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.color-label {
  font-size: 12px;
}

@media (max-width: 992px) {
  .chart-container {
    height: 70vh;
    min-height: 500px;
  }
}

@media (max-width: 768px) {
  .chart-container {
    height: 60vh;
    min-height: 450px;
  }
  
  .data-controls {
    flex-direction: column;
  }
}
</style>