<template>
  <div class="product-container">
    <h1 class="page-title">产品管理</h1>
    
    <el-table :data="productList" border style="width: 100%">
      <el-table-column prop="fofname" label="FOF名称" width="180" />
      <el-table-column prop="dfname" label="DF名称" width="180" />
      <el-table-column prop="id" label="用户ID" width="100" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="risk" label="风险等级" width="120" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="编辑产品信息" width="30%">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="风险等级">
          <el-select v-model="editForm.risk" placeholder="请选择风险等级">
            <el-option label="高风险" value="高风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="低风险" value="低风险" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="3"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";

const route = useRoute();
const id = ref(route.query.id);

const productList = ref([]);

const dialogVisible = ref(false);
const editForm = ref({
  fofname: "",
  id: 0,
  risk: "",
  description: "",
});

const fetchProducts = async () => {
  try {
    const response = await fetch(
      `http://localhost:8080/product/all?id=${id.value}`
    );
    if (!response.ok) {
      throw new Error("获取产品列表失败");
    }
    productList.value = await response.json();
  } catch (error) {
    ElMessage.error(error.message);
  }
};

const handleEdit = (row) => {
  editForm.value = {
    fofname: row.fofname,
    id: row.id,
    risk: row.risk,
    description: row.description,
  };
  dialogVisible.value = true;
};

const submitEdit = async () => {
  try {
    const { fofname, id, risk, description } = editForm.value;
    const response = await fetch(
      `http://localhost:8080/product/update?fofname=${encodeURIComponent(
        fofname
      )}&id=${id}&risk=${encodeURIComponent(
        risk
      )}&description=${encodeURIComponent(description)}`
    );
    if (!response.ok) {
      throw new Error("更新产品信息失败");
    }
    ElMessage.success("更新成功");
    dialogVisible.value = false;
    fetchProducts();
  } catch (error) {
    ElMessage.error(error.message);
  }
};

onMounted(() => {
  fetchProducts();
});
</script>

<style scoped>
.product-container {
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
</style>