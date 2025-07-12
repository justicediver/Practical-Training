<template>
  <div class="tree-select-container">
    <h1 class="page-title">创建衍生因子</h1>
    
    <!-- 树形选择部分 -->
    <div class="selection-area">
      <el-tree
        ref="treeRef"
        :props="treeProps"
        :load="loadNode"
        lazy
        show-checkbox
        node-key="value"
        :default-expand-all="false"
        :check-strictly="true"
        :filter-node-method="filterNode"
        class="factor-tree"
      />
      <el-button 
        type="primary" 
        @click="handlePrintSelectedItems"
        class="print-button"
      >
        创建
      </el-button>
    </div>

    <!-- 衍生因子创建对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="创建衍生因子"
      width="800px"
      :before-close="handleDialogClose"
    >
      <div class="form-container">
        <div class="factor-name-input">
          <span class="input-label">因子名称</span>
          <el-input
            v-model="derivedFactorName"
            placeholder="请输入衍生因子名称"
            class="factor-input"
          />
        </div>
        
        <div class="factor-weights-container">
          <div class="factor-weights-table">
            <div class="table-header">
              <div class="header-cell factor-name">因子名称</div>
              <div class="header-cell factor-weight">因子权重(%)</div>
            </div>
            <div class="table-body">
              <div class="table-row" v-for="(factor, index) in selectedFactors" :key="index">
                <div class="row-cell factor-name">{{ factor.fatherName }}-{{ factor.value }}</div>
                <div class="row-cell factor-weight">
                  <el-input-number
                    v-model="factor.percent"
                    :min="0"
                    :max="100"
                    controls-position="right"
                    @change="calculateTotal"
                    class="weight-input"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="total-weight">
          总权重: {{ totalPercent }}%
          <span v-if="totalPercent !== 100" class="error-text">(权重总和必须等于100%)</span>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogCancel" class="dialog-button">取消</el-button>
          <el-button 
            type="primary" 
            @click="handleDialogConfirm"
            :disabled="totalPercent !== 100 || !derivedFactorName"
            class="dialog-button"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';

const route = useRoute();
const id = ref(route.query.id);

const treeRef = ref();
const treeProps = {
  label: 'name',
  value: 'value',
  isLeaf: 'isLeaf',
  disabled: (data: any) => data.level === 0
};

const loadNode = async (node: any, resolve: (data: any[]) => void) => {
  try {
    if (node.level === 0) {
      // 加载父节点
      const response = await axios.get('http://localhost:8080/factors/single/fathers');
      const fathers = response.data.map((fatherName: string) => ({
        name: fatherName,
        value: fatherName,
        isLeaf: false,
        level: 0
      }));
      resolve(fathers);
    } else if (node.level === 1) {
      // 加载子节点
      const fatherName = node.data.value;
      const response = await axios.get(`http://localhost:8080/factors/single/father/${fatherName}`);
      const children = response.data.map((child: any) => ({
        name: `${fatherName}-${child.factorname}`,
        value: child.factorname,
        isLeaf: true,
        level: 1,
        fatherName: fatherName
      }));
      resolve(children);
    }
  } catch (error) {
    console.error('加载数据失败:', error);
    resolve([]);
  }
};

const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.includes(value);
};

const dialogVisible = ref(false);
const derivedFactorName = ref('');
const selectedFactors = ref<Array<{fatherName: string, value: string, percent: number}>>([]);
const totalPercent = ref(0);

const handlePrintSelectedItems = () => {
  const checkedNodes = treeRef.value?.getCheckedNodes() || [];
  const children = checkedNodes.filter((node: any) => node.level === 1);
  
  if (children.length === 0) {
    ElMessage.warning('请至少选择一个因子');
    return;
  }
  
  selectedFactors.value = children.map((node: any) => ({
    fatherName: node.fatherName,
    value: node.value,
    percent: 0
  }));
  
  calculateTotal();
  dialogVisible.value = true;
};

// 计算总权重
const calculateTotal = () => {
  totalPercent.value = selectedFactors.value.reduce((sum, factor) => sum + (factor.percent || 0), 0);
};

const handleDialogClose = () => {
  dialogVisible.value = false;
  resetDialog();
};

const handleDialogCancel = () => {
  dialogVisible.value = false;
  resetDialog();
};

const handleDialogConfirm = async () => {
  if (totalPercent.value !== 100) {
    ElMessage.error('权重总和必须等于100%');
    return;
  }
  
  if (!derivedFactorName.value) {
    ElMessage.error('请输入衍生因子名称');
    return;
  }
  
  try {
    const factorData = {
      dfname: derivedFactorName.value.trim(),
      id: parseInt(id.value as string),
      factornames: selectedFactors.value.map(f => f.value).join(','),
      percents: selectedFactors.value.map(f => f.percent).join(',')
    };
    
    // 创建新衍生因子
    const response = await axios.post('http://localhost:8080/factors/add', factorData);
    console.log(factorData)
    if (response.status === 201) {
      ElMessage.success('衍生因子创建成功');
      dialogVisible.value = false;
      resetDialog();
    } else {
      ElMessage.error('创建衍生因子失败');
    }
  } catch (error: any) {
    console.error('创建衍生因子出错:', error);
    ElMessage.error('创建衍生因子出错: ' + (error.response?.data || error.message));
  }
};

const resetDialog = () => {
  derivedFactorName.value = '';
  selectedFactors.value = [];
  totalPercent.value = 0;
};
</script>

<style scoped>
.tree-select-container {
  font-size: 16px;
  max-width: 1200px;
  margin: 20px auto;
  padding: 30px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 20px 0;
  padding: 0;
  text-align: left;
}

.selection-area {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.factor-tree {
  flex: 1;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 15px;
  min-height: 500px;
  width: 100%;
}

.print-button {
  font-size: 16px;
  align-self: flex-start;
  padding: 12px 24px;
}

.form-container {
  font-size: 16px;
  display: flex;
  flex-direction: column;
  gap: 25px;
  width: 100%;
}

.factor-name-input {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;
}

.input-label {
  font-size: 16px;
  width: 100px;
  font-weight: bold;
  flex-shrink: 0;
}

.factor-input {
  flex: 1;
}

.factor-weights-container {
  width: 100%;
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.factor-weights-table {
  width: 100%;
  display: table;
}

.table-header, .table-row {
  display: table-row;
}

.header-cell, .row-cell {
  display: table-cell;
  padding: 15px;
  border-bottom: 1px solid #dcdfe6;
  vertical-align: middle;
}

.header-cell {
  background-color: #f5f7fa;
  font-weight: bold;
  text-align: center;
}

.factor-name {
  width: 60%;
  text-align: left;
  padding-left: 20px;
}

.factor-weight {
  width: 40%;
  text-align: center;
}

.weight-input {
  width: 120px;
}

.total-weight {
  font-size: 16px;
  text-align: right;
  font-weight: bold;
  margin-top: 15px;
  padding: 10px;
}

.error-text {
  font-size: 16px;
  color: #f56c6c;
  font-weight: normal;
}

.dialog-footer {
  font-size: 16px;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  padding-top: 15px;
}

.dialog-button {
  padding: 12px 24px;
}

:deep(.el-input__inner) {
  font-size: 16px;
  height: 40px;
  line-height: 40px;
}

:deep(.el-button) {
  font-size: 16px;
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: bold;
}

:deep(.el-input-number__decrease),
:deep(.el-input-number__increase),
:deep(.el-input-number .el-input__inner) {
  font-size: 16px;
}

:deep(.el-tree-node__content) {
  height: 40px;
  line-height: 40px;
  margin: 5px 0;
}

:deep(.el-checkbox__inner) {
  width: 18px;
  height: 18px;
}
</style>