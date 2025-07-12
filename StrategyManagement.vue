<template>
  <el-steps style="max-width: 600px" :active="active" finish-status="success">
    <el-step title="资产配置" />
    <el-step title="基金量化" />
    <el-step title="基金组合" />
  </el-steps>

  <div v-if="active === 0" class="strategy-container">
    <el-table :data="fundConfigList" style="width: 100%; margin-top: 20px">
      <el-table-column prop="fundcode" label="基金编号" width="150" />
      <el-table-column prop="fundname" label="基金名称" width="200" />
      <el-table-column label="权重 (%)" width="200">
        <template #default="scope">
          <el-input-number
            v-model="scope.row.percent"
            :min="0"
            :max="100"
            :precision="0"
            controls-position="right"
            @change="validateTotalPercent"
          />
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; text-align: right">
      <span>权重总和: {{ totalPercent }}%</span>
      <el-tag :type="totalPercent === 100 ? 'success' : 'danger'" style="margin-left: 10px">
        {{ totalPercent === 100 ? '符合要求' : '权重总和必须等于100%' }}
      </el-tag>
    </div>

    <div style="margin-top: 20px; text-align: center">
      <el-button 
        type="primary" 
        @click="saveConfigurations"
        :disabled="totalPercent !== 100"
      >
        保存配置
      </el-button>
    </div>

    <div style="margin-top: 30px; text-align: center">
      <el-button 
        type="primary" 
        @click="next"
        :disabled="totalPercent !== 100"
      >
        下一步
      </el-button>
    </div>
  </div>

  <div v-if="active === 1" class="strategy-container">
    <h3 style="margin-bottom: 20px;">选择衍生因子方案</h3>
    
    <el-table :data="derivedFactors" style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="dfname" label="方案名称" width="200" />
      <el-table-column prop="factornames" label="因子组合" />
      <el-table-column prop="percents" label="因子权重" width="200" />
      <el-table-column label="选择" width="100">
        <template #default="scope">
          <el-radio v-model="selectedFactor" :label="scope.row.dfname">
            &nbsp;
          </el-radio>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 30px; text-align: center">
      <el-button 
        type="primary" 
        @click="saveDfname"
        :disabled="!selectedFactor"
      >
        保存选择
      </el-button>
    </div>

    <div style="margin-top: 20px; text-align: center">
      <el-button 
        style="margin-right: 20px"
        @click="prev"
      >
        返回
      </el-button>
      <el-button 
        type="primary" 
        @click="next"
        :disabled="!selectedFactor"
      >
        下一步
      </el-button>
    </div>
  </div>

  <div v-if="active === 2" class="strategy-container">
    <h3 style="margin-bottom: 20px;">基金组合配置</h3>
    
    <div style="margin-bottom: 30px;">
      <el-form label-width="120px">
        <el-form-item label="基金组合名称" required>
          <el-input 
            v-model="fofName" 
            placeholder="请输入基金组合名称" 
            style="width: 300px"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <h4 style="margin-bottom: 15px;">当前配置</h4>
    <el-table :data="fundConfigList" style="width: 100%; margin-bottom: 30px">
      <el-table-column prop="fundcode" label="基金编号" width="150" />
      <el-table-column prop="fundname" label="基金名称" width="200" />
      <el-table-column prop="percent" label="权重 (%)" width="150" />
    </el-table>
    
    <h4 style="margin-bottom: 15px;">衍生因子方案</h4>
    <el-table :data="[selectedDerivedFactor]" style="width: 100%; margin-bottom: 30px">
      <el-table-column prop="dfname" label="方案名称" width="200" />
      <el-table-column prop="factornames" label="因子组合" />
      <el-table-column prop="percents" label="因子权重" width="200" />
    </el-table>
    
    <div style="margin-top: 30px; text-align: center">
      <el-button 
        style="margin-right: 20px"
        @click="prev"
      >
        返回
      </el-button>
      <el-button 
        type="primary" 
        @click="saveFofName"
        :disabled="!fofName"
      >
        保存组合
      </el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userId = ref(Number(route.query.id))

const fundConfigList = ref<Array<{
  fundcode: number
  fundname: string
  percent: number
}>>([])

const derivedFactors = ref<Array<{
  dfname: string
  factornames: string
  percents: string
}>>([])

const selectedFactor = ref('')

const selectedDerivedFactor = computed(() => {
  return derivedFactors.value.find(item => item.dfname === selectedFactor.value) || {}
})

const fofName = ref('')

const active = ref(0)

const totalPercent = computed(() => {
  return fundConfigList.value.reduce((sum, item) => sum + (item.percent || 0), 0)
})

const validateTotalPercent = () => {
  if (totalPercent.value > 100) {
    ElMessage.warning('权重总和不能超过100%')
  }
}

const fetchUserConfigurations = async () => {
  try {
    const response = await axios.get('http://localhost:8080/strategy/config', {
      params: { id: userId.value }
    })
    fundConfigList.value = response.data.map((item: any) => ({
      fundcode: item.fundcode,
      fundname: item.fundname,
      percent: item.percent
    }))
  } catch (error) {
    console.error('获取配置失败:', error)
    ElMessage.error('获取资产配置失败')
  }
}

const fetchDerivedFactors = async () => {
  try {
    const response = await axios.get('http://localhost:8080/strategy/derivedFactors', {
      params: { id: userId.value }
    })
    derivedFactors.value = response.data.map((item: any) => ({
      dfname: item.dfname,
      factornames: item.factornames,
      percents: item.percents
    }))
  } catch (error) {
    console.error('获取衍生因子失败:', error)
    ElMessage.error('获取衍生因子方案失败')
  }
}

const saveConfigurations = async () => {
  if (totalPercent.value !== 100) {
    ElMessage.warning('权重总和必须等于100%才能保存')
    return
  }

  const saveData = fundConfigList.value.map(item => ({
    id: userId.value,
    fundcode: item.fundcode,
    percent: item.percent
  }))

  try {
    const response = await axios.post('http://localhost:8080/strategy/save', saveData)
    if (response.data === 1) {
      ElMessage.success('资产配置保存成功')
    } else {
      ElMessage.warning('保存资产配置失败')
    }
  } catch (error) {
    console.error('保存资产配置失败:', error)
    ElMessage.error('保存资产配置失败')
  }
}

const saveDfname = async () => {
  if (!selectedFactor.value) {
    ElMessage.warning('请先选择一个衍生因子方案')
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/strategy/saveDfname', {
      id: userId.value,
      dfname: selectedFactor.value
    })
    
    if (response.data === 1) {
      ElMessage.success('衍生因子方案保存成功')
    } else {
      ElMessage.warning('保存衍生因子方案失败')
    }
  } catch (error) {
    console.error('保存衍生因子方案失败:', error)
    ElMessage.error('保存衍生因子方案失败')
  }
}

const saveFofName = async () => {
  if (!fofName.value) {
    ElMessage.warning('请输入基金组合名称')
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/strategy/updateFofname', {
      id: userId.value,
      fofname: fofName.value
    })
    
    if (response.data === 1) {
      ElMessage.success('基金组合保存成功')
    axios.get("http://localhost:8080/product/init", {
			params:{
				fofname: fofName.value,
        dfname: selectedFactor.value,
      id: userId.value,
			}
		})
		.then(res=>{})

    router.push({
        path: '/fund-research',
        query: { id: userId.value }
      })
    } else {
      ElMessage.warning('保存基金组合失败')
    }
  } catch (error) {
    console.error('保存基金组合失败:', error)
    ElMessage.error('保存基金组合失败')
  }
}

const next = () => {
  if (active.value === 0 && totalPercent.value !== 100) {
    ElMessage.warning('权重总和必须等于100%才能继续')
    return
  }
  
  if (active.value === 1 && !selectedFactor.value) {
    ElMessage.warning('请选择一个衍生因子方案')
    return
  }
  
  if (active.value < 2) {
    active.value++
    
    if (active.value === 1) {
      fetchDerivedFactors()
    }
  }
}

const prev = () => {
  if (active.value > 0) {
    active.value--
  }
}

onMounted(() => {
  fetchUserConfigurations()
})
</script>

<style scoped>
.strategy-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 30px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
}
</style>