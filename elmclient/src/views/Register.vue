<template>
	<div class="wrapper">

		<!-- header部分 -->
		<header>
			<p>用户注册</p>
		</header>

		<!-- 表单部分 -->
		<ul class="form-box">
			<li>
				<div class="title">
					手机号码：
				</div>
				<div class="content">
					<input type="text" @blur="checkUserId" v-model="user.userId" placeholder="手机号码">
				</div>
			</li>
			<li>
				<div class="title">
					密码：
				</div>
				<div class="content">
					<input type="password" v-model="user.password" placeholder="密码">
				</div>
			</li>
			<li>
				<div class="title">
					确认密码：
				</div>
				<div class="content">
					<input type="password" v-model="confirmPassword" placeholder="确认密码">
				</div>
			</li>
			<li>
				<div class="title">
					用户名称：
				</div>
				<div class="content">
					<input type="text" v-model="user.userName" placeholder="用户名称">
				</div>
			</li>
			<li>
				<div class="title">
					性别：
				</div>
				<div class="content" style="font-size: 3vw;">
					<input type="radio" v-model="user.userSex" value="1" style="width:6vw;height: 3.2vw;">男
					<input type="radio" v-model="user.userSex" value="0" style="width:6vw;height: 3.2vw;">女
				</div>
			</li>
		</ul>

		<div class="button-login">
			<button @click="register">注册</button>
		</div>
		<div class="button-register">
			<button @click="toLogin">已有账号？去登录</button>
		</div>

	</div>
</template>

<script>
	export default {
		name: 'Register',
		data() {
			return {
				user: {
					userId: '',
					password: '',
					userName: '',
					userSex: 1
				},
				confirmPassword: ''
			}
		},
		methods: {
			checkUserId() {
				this.$axios.post('UserController/getUserById', this.$qs.stringify({
					userId: this.user.userId,
				})).then(response => {
					if (response.data == 1) {
						this.user.userId = '';
						alert('此手机号码已存在！')
					}
				}).catch(error => {
					console.error(error);
				});
			},
			register() {
				if (this.user.userId == '') {
					alert('手机号码不能为空！');
					return;
				}
				if (this.user.password == '') {
					alert('密码不能为空！');
					return;
				}
				if (this.user.password != this.confirmPassword) {
					alert('两次输入的密码不一致！');
					return;
				}
				if (this.user.userName == '') {
					alert('用户名不能为空！');
					return;
				}

				//注册请求 
				this.$axios.post('UserController/saveUser', this.$qs.stringify(
					this.user
				)).then(response => {
					if (response.data > 0) {
						alert('注册成功！');
						this.$router.push({
							path: '/login'
						});
					} else {
						alert('注册失败！');
					}
				}).catch(error => {
					console.error(error);
				});
			},
			toLogin() {
				this.$router.push({
					path: '/login'
				});
			}
		}
	}
</script>

<style scoped>
/****************** 总容器 ******************/
.wrapper {
  width: 100%;
  height: 100%;
  background-color: #f8f9fa;
}

/****************** header部分 ******************/
.wrapper header {
  width: 100%;
  height: 12vw;
  background-color: #0097FF;
  color: #fff;
  font-size: 4.8vw;
  font-weight: 700;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/****************** 表单部分 ******************/
.form-box {
  width: 90%;
  margin: 5vw auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  padding: 2vw 0;
}

.form-box li {
  box-sizing: border-box;
  padding: 3.5vw 6vw;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.form-box li:last-child {
  border-bottom: none;
}

.form-box li .title {
  flex: 0 0 22vw;
  font-size: 3.8vw;
  font-weight: 700;
  color: #666;
}

.form-box li .content {
  flex: 1;
  display: flex; 
  align-items: center;
}

.form-box li .content input[type="text"],
.form-box li .content input[type="password"] {
  border: none;
  outline: none;
  width: 100%;
  height: 6vw;
  font-size: 3.8vw;
}

.form-box li .content input::placeholder {
  color: #ccc;
}

.form-box li .content input[type="radio"] {
  width: 4vw;
  height: 4vw;
  margin-right: 2vw;
}

/****************** 按钮部分 ******************/
.button-login, .button-register {
  width: 90%;
  margin: 3vw auto;
}

.button-login button {
  width: 100%;
  height: 12vw;
  border: none;
  outline: none;
  border-radius: 6vw;
  background-color: #0097FF;
  color: #fff;
  font-size: 4.5vw;
  font-weight: 700;
  cursor: pointer;
  transition: opacity 0.2s;
}

.button-login button:active {
  opacity: 0.8;
}

.button-register button {
  width: 100%;
  height: 12vw;
  border: 1px solid #0097FF;
  outline: none;
  border-radius: 6vw;
  background-color: transparent;
  color: #0097FF;
  font-size: 4.5vw;
  font-weight: 700;
  cursor: pointer;
  transition: background-color 0.2s;
}

.button-register button:active {
  background-color: rgba(0, 151, 255, 0.05);
}
</style>
