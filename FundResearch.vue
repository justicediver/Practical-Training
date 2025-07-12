<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { ElMessage, ElDialog } from 'element-plus'
import { useRouter,useRoute } from 'vue-router'
import ChartView from './ChartView.vue'

let route = useRoute()
let id = ref(route.query.id)

interface Fund {
  fundcode: number
  fundname: string
  company: string
  manager: string
  net_worth: number
  annual_return: number
  total_return: number
  info: string
  inFOF?: boolean
}

const allFunds = ref<Fund[]>([])
const funds = ref<Fund[]>([])
const loading = ref(false)
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const searchParams = ref({
  fundcode: '',
  fundname: '',
  company: '',
  manager: ''
})

const filterParams = ref({
  companySizeLevel: '',
  companyRatingLevel: '',
  companyRiskLevel: '',
  managerAssetLevel: '',
  managerReturnLevel: '',
  managerExpLevel: ''
})

const dialogVisible = ref(false)
const filterDialogVisible = ref(false)
const currentFundInfo = ref('')

const updateCurrentPageData = () => {
  const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  funds.value = allFunds.value.slice(start, end)
}

const checkFOFStatus = async () => {
  if (!id.value) return
  
  try {
    const promises = allFunds.value.map(fund => 
      axios.get('http://localhost:8080/fund/checkInFOF', {
        params: { 
          id: id.value, 
          fundcode: fund.fundcode 
        }
      })
    )
    
    const results = await Promise.all(promises)
    
    allFunds.value.forEach((fund, index) => {
      fund.inFOF = results[index].data === 1
    })
    
    updateCurrentPageData()
  } catch (error) {
    console.error('检查FOF状态失败:', error)
  }
}

const showFundInfo = (info: string) => {
  currentFundInfo.value = info
  dialogVisible.value = true
}

const searchFunds = async () => {
  try {
    loading.value = true
    const params = {
      fundcode: searchParams.value.fundcode ? Number(searchParams.value.fundcode) : undefined,
      fundname: searchParams.value.fundname || undefined,
      company: searchParams.value.company || undefined,
      manager: searchParams.value.manager || undefined,
      page: pagination.value.currentPage,
      size: pagination.value.pageSize
    }

    const hasSearchParams = Object.values(searchParams.value).some(val => val !== '')
    const url = hasSearchParams ? 'http://localhost:8080/fund/search' : 'http://localhost:8080/fund/findAll'
    
    const response = await axios.get(url, { params })
    allFunds.value = response.data
    pagination.value.total = allFunds.value.length
    pagination.value.currentPage = 1
    
    await checkFOFStatus()
  } catch (error) {
    console.error('搜索基金失败:', error)
    ElMessage.error('搜索基金失败')
    allFunds.value = []
    funds.value = []
    pagination.value.total = 0
  } finally {
    loading.value = false
    updateCurrentPageData()
  }
}

const advancedSearch = async () => {
  try {
    loading.value = true
    
    const fundcode = searchParams.value.fundcode 
      ? Number(searchParams.value.fundcode)
      : undefined

    const params = {
      fundcode,
      fundname: searchParams.value.fundname || undefined,
      company: searchParams.value.company || undefined,
      manager: searchParams.value.manager || undefined,
      companySizeLevel: filterParams.value.companySizeLevel || undefined,
      companyRatingLevel: filterParams.value.companyRatingLevel || undefined,
      companyRiskLevel: filterParams.value.companyRiskLevel || undefined,
      managerAssetLevel: filterParams.value.managerAssetLevel || undefined,
      managerReturnLevel: filterParams.value.managerReturnLevel || undefined,
      managerExpLevel: filterParams.value.managerExpLevel || undefined,
      page: pagination.value.currentPage,
      size: pagination.value.pageSize
    }

    const response = await axios.get('http://localhost:8080/fund/advancedSearch', { 
      params,
      paramsSerializer: {
        indexes: null
      }
    })
    
    allFunds.value = response.data
    pagination.value.total = allFunds.value.length
    pagination.value.currentPage = 1
    
    await checkFOFStatus()
    
    filterDialogVisible.value = false
  } catch (error) {
    console.error('高级筛选失败:', error)
    ElMessage.error('高级筛选失败')
    allFunds.value = []
    funds.value = []
    pagination.value.total = 0
  } finally {
    loading.value = false
    updateCurrentPageData()
  }
}

const loadAllFunds = async () => {
  try {
    loading.value = true
    const response = await axios.get('http://localhost:8080/fund/findAll')
    allFunds.value = response.data
    pagination.value.total = allFunds.value.length
    pagination.value.currentPage = 1
    
    await checkFOFStatus()
  } catch (error) {
    console.error('加载基金数据失败:', error)
    ElMessage.error('加载基金数据失败')
    allFunds.value = []
    funds.value = []
    pagination.value.total = 0
  } finally {
    loading.value = false
    updateCurrentPageData()
  }
}

const addToFOF = async (fundcode: number, fundname: string) => {
  if (!id.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    const response = await axios.post('http://localhost:8080/fund/addToFOF', null, {
      params: { fundcode, id: id.value, fundname }
    })
    
    if (response.data === "添加成功") {
      ElMessage.success('已添加到FOF')
      
      const fund = allFunds.value.find(f => f.fundcode === fundcode)
      if (fund) fund.inFOF = true
      
      updateCurrentPageData()
    } else {
      ElMessage.warning(response.data)
    }
  } catch (error) {
    console.error('添加FOF失败:', error)
    ElMessage.error('添加失败')
  }
}

const removeFromFOF = async (fundcode: number) => {
  if (!id.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    const response = await axios.delete('http://localhost:8080/fund/removeFromFOF', {
      params: { fundcode, id: id.value }
    })
    
    if (response.data === "删除成功") {
      ElMessage.success('已从FOF中移除')
      
      const fund = allFunds.value.find(f => f.fundcode === fundcode)
      if (fund) fund.inFOF = false
      
      updateCurrentPageData()
    } else {
      ElMessage.warning(response.data)
    }
  } catch (error) {
    console.error('删除FOF失败:', error)
    ElMessage.error('删除失败')
  }
}

const resetSearch = () => {
  searchParams.value = {
    fundcode: '',
    fundname: '',
    company: '',
    manager: ''
  }
  filterParams.value = {
    companySizeLevel: '',
    companyRatingLevel: '',
    companyRiskLevel: '',
    managerAssetLevel: '',
    managerReturnLevel: '',
    managerExpLevel: ''
  }
  pagination.value.currentPage = 1
  loadAllFunds()
}

const resetFilters = () => {
  filterParams.value = {
    companySizeLevel: '',
    companyRatingLevel: '',
    companyRiskLevel: '',
    managerAssetLevel: '',
    managerReturnLevel: '',
    managerExpLevel: ''
  }
}

const handleSizeChange = (val: number) => {
  pagination.value.pageSize = val
  pagination.value.currentPage = 1
  updateCurrentPageData()
}

const handleCurrentChange = (val: number) => {
  pagination.value.currentPage = val
  updateCurrentPageData()
}

onMounted(() => {
  loadAllFunds()
})
</script>

<template>
  <div class="fund-research-container">
    <div class="page-header">
      <h2 class="page-title">基金总览</h2>
      <el-button type="primary" @click="filterDialogVisible = true">筛选</el-button>
    </div>
    
    <div class="search-container">
      <div class="search-item">
        <el-input
          v-model="searchParams.fundcode"
          type="number"
          placeholder="基金代码"
          clearable
          @keyup.enter="searchFunds"
          @clear="searchFunds"
        >
          <template #prepend>基金代码</template>
        </el-input>
      </div>
      
      <div class="search-item">
        <el-input
          v-model="searchParams.fundname"
          placeholder="基金名称"
          clearable
          @keyup.enter="searchFunds"
          @clear="searchFunds"
        >
          <template #prepend>基金名称</template>
        </el-input>
      </div>
      
      <div class="search-item">
        <el-input
          v-model="searchParams.company"
          placeholder="公司名称"
          clearable
          @keyup.enter="searchFunds"
          @clear="searchFunds"
        >
          <template #prepend>公司名称</template>
        </el-input>
      </div>
      
      <div class="search-item">
        <el-input
          v-model="searchParams.manager"
          placeholder="基金经理"
          clearable
          @keyup.enter="searchFunds"
          @clear="searchFunds"
        >
          <template #prepend>基金经理</template>
        </el-input>
      </div>
      
      <div class="search-buttons">
        <el-button type="primary" @click="searchFunds" :loading="loading">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>
    
    <div class="table-container">
      <el-table
        :data="funds"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="fundcode" label="基金代码" width="120" align="center" />
        <el-table-column prop="fundname" label="基金名称" min-width="180" />
        <el-table-column prop="company" label="公司名称" min-width="180" />
        <el-table-column prop="manager" label="基金经理" width="120" />
        <el-table-column prop="net_worth" label="单位净值" width="120" align="right">
          <template #default="{ row }">
            {{ row.net_worth?.toFixed(4) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="annual_return" label="年化收益(%)" width="120" align="right">
          <template #default="{ row }">
            <span :class="{ 'positive': row.annual_return > 0, 'negative': row.annual_return < 0 }">
              {{ row.annual_return?.toFixed(2) || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="total_return" label="累计收益(%)" width="120" align="right">
          <template #default="{ row }">
            <span :class="{ 'positive': row.total_return > 0, 'negative': row.total_return < 0 }">
              {{ row.total_return?.toFixed(2) || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="基金画像" width="120" align="center">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              size="small" 
              @click="showFundInfo(row.info)"
              :disabled="!row.info"
            >
              查看画像
            </el-button>
          </template>
        </el-table-column>
        
        <el-table-column label="基金选项" width="120" align="center">
          <template #default="{ row }">
            <el-button 
              v-if="!row.inFOF"
              type="success" 
              size="small" 
              @click="addToFOF(row.fundcode, row.fundname)"
            >
              添加
            </el-button>
            <el-button 
              v-else
              type="danger" 
              size="small" 
              @click="removeFromFOF(row.fundcode)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="基金画像"
      width="60%"
      top="5vh"
      class="chart-dialog"
    >
      <div class="dialog-container" style="height: 80vh">
        <div class="chart-container">
          <div class="fund-info" v-if="currentFundInfo">
            <h3>基金描述</h3>
            <div class="info-content">{{ currentFundInfo || '暂无基金画像信息' }}</div>
          </div>
          
          <div class="chart-view-container">
            <ChartView />
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="dialogVisible = true"></el-button>
      </template>
    </el-dialog>
    
    <el-dialog
      v-model="filterDialogVisible"
      title="高级筛选"
      width="50%"
      top="5vh"
    >
      <div class="filter-content">
        <div class="filter-section">
          <h3>基金公司筛选</h3>
          
          <div class="filter-group">
            <h4>公司规模</h4>
            <el-radio-group v-model="filterParams.companySizeLevel">
              <el-radio-button label="LARGE">大型(≥100亿)</el-radio-button>
              <el-radio-button label="MEDIUM">中型(50-100亿)</el-radio-button>
              <el-radio-button label="SMALL">小型(<50亿)</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="filter-group">
            <h4>公司评级</h4>
            <el-radio-group v-model="filterParams.companyRatingLevel">
              <el-radio-button label="HIGH">高(4星以上)</el-radio-button>
              <el-radio-button label="MEDIUM">中(3星)</el-radio-button>
              <el-radio-button label="LOW">低(2星以下)</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="filter-group">
            <h4>风险等级</h4>
            <el-radio-group v-model="filterParams.companyRiskLevel">
              <el-radio-button label="HIGH">高风险</el-radio-button>
              <el-radio-button label="MEDIUM">中风险</el-radio-button>
              <el-radio-button label="LOW">低风险</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        
        <div class="filter-section">
          <h3>基金经理筛选</h3>
          
          <div class="filter-group">
            <h4>管理规模</h4>
            <el-radio-group v-model="filterParams.managerAssetLevel">
              <el-radio-button label="LARGE">大型(≥50亿)</el-radio-button>
              <el-radio-button label="MEDIUM">中型(10-50亿)</el-radio-button>
              <el-radio-button label="SMALL">小型(<10亿)</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="filter-group">
            <h4>年化回报率</h4>
            <el-radio-group v-model="filterParams.managerReturnLevel">
              <el-radio-button label="HIGH">高(≥15%)</el-radio-button>
              <el-radio-button label="MEDIUM">中(8-15%)</el-radio-button>
              <el-radio-button label="LOW">低(<8%)</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="filter-group">
            <h4>从业经验</h4>
            <el-radio-group v-model="filterParams.managerExpLevel">
              <el-radio-button label="SENIOR">资深(≥10年)</el-radio-button>
              <el-radio-button label="MID">中等(5-10年)</el-radio-button>
              <el-radio-button label="JUNIOR">初级(<5年)</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="resetFilters">重置筛选</el-button>
        <el-button type="primary" @click="advancedSearch" :loading="loading">确认筛选</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.fund-research-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  color: #303133;
}

.search-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
}

.search-item {
  flex: 1;
  min-width: 200px;
}

.search-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.table-container {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.positive {
  color: #f56c6c;
}

.negative {
  color: #67c23a;
}

.dialog-layout {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-height: 70vh;
  overflow-y: auto;
}

.fund-info {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
}

.fund-info h3 {
  margin-top: 0;
  color: #333;
  font-size: 16px;
}

.fund-info .info-content {
  white-space: pre-wrap;
  line-height: 1.6;
}

.chart-container {
  flex: 1;
  min-height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart-container > div {
  width: 100%;
  height: 100%;
}

.fund-info-content {
  white-space: pre-wrap;
  line-height: 1.6;
  max-height: 60vh;
  overflow-y: auto;
  padding: 0 10px;
}

.filter-content {
  max-height: 60vh;
  overflow-y: auto;
  padding: 0 10px;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-section h3 {
  margin-bottom: 15px;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.filter-group {
  margin-bottom: 15px;
}

.filter-group h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #606266;
}

.el-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

:deep(.el-table .cell) {
  white-space: nowrap;
}
</style>