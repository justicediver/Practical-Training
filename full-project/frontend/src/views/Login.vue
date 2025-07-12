<template>
	<meta charset="utf-8">
	<div class="container" :style="{ backgroundImage: `url(${backgroundImage})` }">
		<div class="login-box">
			<br />
			<div class="single">
				用户名<el-input v-model=ac style="width: 240px" placeholder="请输入用户名" /> 
			<br /><br />
			</div>
			<div class="single">
				密码&emsp;
			<el-input
			    v-model=pw
			    style="width: 240px"
			    type="password"
			    placeholder="请输入密码"
			    show-password
			  />
			  <br /><br />
			</div>
			<div class="single">
				<el-button type="primary" @click="login">登录</el-button>
			<el-button type="primary" @click="register">注册</el-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import axios from 'axios';
	import { ElNotification } from 'element-plus';
	import { ref } from 'vue';
	import { useRoute, useRouter } from 'vue-router';
	import backgroundImage from '@/assets/background.jpg';
	let route = useRoute()
	let ac = ref("")
	let pw = ref("")
	let id = ref()
	let router = useRouter()

function login(){
  axios.get("http://localhost:8080/login", {
    params: {
      username: ac.value,
      password: pw.value
    }
  })
  .then(res => {
    if (res.data == 0) {
      ElNotification({
        title: '登录失败',
        message: '用户名或密码错误',
        type: 'error',
      })
    } else {
      // 管理员特殊处理
      if (ac.value === 'root' && pw.value === '123456') {
        localStorage.setItem('isAdmin', 'true')
        id.value = 0;
        localStorage.setItem('adminId', '0')
      } else {
        localStorage.setItem('isAdmin', 'false')
        id.value = res.data
      }
      
      router.replace({
        name: "home",
        query: { id: id.value }
      })
      
      ElNotification({
        title: '登录成功',
        message: '',
        type: 'success',
      })
      
      const userIdParam = ac.value === 'root' ? 'admin_root' : id.value;
      const wsUrl = `ws://localhost:8080/ws/online-users?userId=${userIdParam}`;
      const socket = new WebSocket(wsUrl);
      
	  socket.onopen = () => {
                console.log('WebSocket连接已建立');
            };
            
            socket.onclose = () => {
                console.log('WebSocket连接已关闭');
            };
            
            localStorage.setItem('userSocket', JSON.stringify({ userId: id.value }));

    }
  })
}

	function register(){
		router.push({name:"register",
		query:{
			name:ac.value,
			pw:pw.value
		}})
	}
</script>

<style>
  .container {
    width: 100vw;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

  .login-box {
    padding: 30px 50px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: white;
  }

  .single {
    margin: 10px 0;
  }
</style>