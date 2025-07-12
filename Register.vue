<template>
	<div class="container">
		<div class="register-box">
			<br />
			<div>用户名&emsp;
			<el-input v-model=user.username style="width: 240px" placeholder="请输入用户名" /> </div>
			
			<br />
			<div>密码&emsp;&emsp;
			<el-input
			    v-model=user.pw
			    style="width: 240px"
			    type="password"
			    placeholder="请输入密码"
			    show-password
			  /></div>
			<br />
			<div>
				<el-button @click="goBack">返回</el-button>
				<el-button type="primary" @click="register">注册</el-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import axios from "axios";
	import { ref } from "vue";
	import { useRoute,useRouter } from "vue-router";
	import { ElNotification } from 'element-plus';
	
	let route = useRoute()
	let router = useRouter()
	let user = ref({
		id:0,
		pw:route.query.pw,
		username:route.query.name,
	})
	
	function goBack() {
		router.replace({name: "login"});
	}
	
	function register(){
		if(user.value.username == "" ||
		 user.value.pw == "" ){
			 ElNotification({
			     title: '注册失败',
			     message: '用户名或密码不能为空',
			     type: 'error',
			   })
		 }
		 else{
			 axios.post("http://localhost:8080/register",user.value)
			 .then(res=>{
				 if(res.data == 0){
					 ElNotification({
			     title: '注册失败',
			     message: '用户名已被占用',
			     type: 'error',
			   })
				 }
				 else{
					 ElNotification({
					 title: '注册成功',
					 message: '用户注册成功',
					 type: 'success',})
					 router.replace({name:"login"})
				 }
			 })
		 }
	}
</script>

<style scoped>
.container {
	width: 100vw;
	height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
}

.register-box {
	padding: 30px 50px;
	border-radius: 10px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	display: flex;
	flex-direction: column;
	align-items: center;
}
</style>
