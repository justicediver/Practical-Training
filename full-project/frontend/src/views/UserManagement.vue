<template>
  <div class="user-manage-container">
    <el-table :data="users" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="100" align="center">
        <template #default="{ row }">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="用户名" min-width="150" align="center">
        <template #default="{ row }">
          <el-input 
            v-model="row.username" 
            placeholder="请输入用户名"
            @change="handleInputChange(row)"></el-input>
        </template>
      </el-table-column>
      
      <el-table-column label="密码" min-width="150" align="center">
        <template #default="{ row }">
          <el-input 
            v-model="row.pw" 
            type="password"
            show-password
            placeholder="请输入密码"
            @change="handleInputChange(row)"></el-input>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template #default="{ row }">
          <el-button 
            type="primary" 
            size="small"
            @click="saveRow(row)"
            :loading="row.saving"
            :disabled="!row.modified">
            保存
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

interface User {
  id: number
  username: string
  pw: string
  modified?: boolean
  saving?: boolean
}

const users = ref<(User & { modified?: boolean, saving?: boolean })[]>([])

const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/allUser')
    users.value = response.data.map(user => ({
      ...user,
      modified: false,
      saving: false
    }))
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户数据失败')
  }
}

const handleInputChange = (row: User) => {
  row.modified = true
}

const saveRow = async (row: User) => {
  if (!row.modified) return
  
  // 基本数据验证
  if (!row.username || row.username.trim() === '') {
    ElMessage.warning('用户名不能为空')
    return
  }
  
  if (!row.pw || row.pw.trim() === '') {
    ElMessage.warning('密码不能为空')
    return
  }
  
  try {
    row.saving = true
    
    if (row.id) {
      await axios.post('http://localhost:8080/updateUserWithoutCheck', {
        id: row.id,
        username: row.username,
        pw: row.pw
      })
      ElMessage.success('用户信息更新成功')
    } else {
      await axios.post('http://localhost:8080/register', {
        username: row.username,
        pw: row.pw
      })
      ElMessage.success('用户添加成功')
      await fetchUsers()
    }
    
    row.modified = false
  } catch (error) {
    console.error('保存用户信息失败:', error)
    ElMessage.error('保存用户信息失败')
  } finally {
    row.saving = false
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-manage-container {
  padding: 20px;
}

.el-table {
  margin-top: 20px;
}
</style>