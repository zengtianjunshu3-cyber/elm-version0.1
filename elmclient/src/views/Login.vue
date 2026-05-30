<template>
  <div class="login-wrapper">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-placeholder">
          <i class="fa fa-cutlery"></i>
        </div>
        <h1>欢迎登录</h1>
        <p>饿了么后台管理系统</p>
      </div>

      <div class="login-form">
        <div class="input-group">
          <i class="fa fa-user"></i>
          <input type="text" v-model="userId" placeholder="请输入手机号码">
        </div>

        <div class="input-group">
          <i class="fa fa-lock"></i>
          <input type="password" v-model="password" placeholder="请输入密码">
        </div>

        <div class="type-selector">
          <div class="type-item" :class="{ active: userType === '0' }" @click="userType = '0'">
            <i class="fa fa-user-circle"></i>
            <span>普通用户</span>
          </div>
          <div class="type-item" :class="{ active: userType === '1' }" @click="userType = '1'">
            <i class="fa fa-vcard"></i>
            <span>系统管理员</span>
          </div>
        </div>

        <button class="btn-login" @click="login">立即登录</button>
        <div class="login-footer">
          <span>还没有账号？</span>
          <a @click="register">立即注册</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      userId: '',
      password: '',
      userType: '0'
    }
  },
  methods: {
    login() {
      if (this.userId == '') {
        alert('手机号码不能为空！');
        return;
      }
      if (this.password == '') {
        alert('密码不能为空！');
        return;
      }

      this.$axios.post('UserController/getUserByIdByPass', this.$qs.stringify({
        userId: this.userId,
        password: this.password
      })).then(response => {
        let user = response.data;
        if (user == null || user == '') {
          alert('用户名或密码不正确！');
        } else if (typeof user === 'string') {
          alert('后端服务器异常：' + user);
        } else {
          if (user.userType != this.userType) {
            alert('登录身份不匹配！');
            return;
          }
          this.$setSessionStorage('user', user);
          if (user.userType == 1) {
            this.$router.push('/adminHome');
          } else {
            this.$router.push('/index');
          }
        }
      }).catch(error => {
        console.error(error);
        alert('登录请求失败，请检查后端服务是否启动');
      });
    },
    register() {
      this.$router.push({ path: '/register' });
    }
  }
}
</script>

<style scoped>
/* 定义商务蓝变量 */
:root {
  --primary-color: #165DFF;
  --primary-hover: #0E42D2;
  --bg-color: #F2F3F5;
}

.login-wrapper {
  width: 100vw;
  height: 100vh;
  background-color: #F2F3F5;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: radial-gradient(circle at 0% 0%, #E8F3FF 0%, transparent 50%),
                    radial-gradient(circle at 100% 100%, #E8F3FF 0%, transparent 50%);
}

.login-card {
  width: 90vw;
  max-width: 400px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  padding: 40px 30px;
  box-sizing: border-box;
  animation: fadeIn 0.6s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-placeholder {
  width: 60px;
  height: 60px;
  background-color: #165DFF;
  border-radius: 16px;
  margin: 0 auto 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.2);
}

.logo-placeholder i {
  color: #fff;
  font-size: 30px;
}

.login-header h1 {
  font-size: 28px;
  color: #1D2129;
  margin: 0 0 10px 0;
  font-weight: 600;
}

.login-header p {
  font-size: 14px;
  color: #86909C;
  margin: 0;
}

.input-group {
  position: relative;
  margin-bottom: 20px;
  transition: all 0.2s;
}

.input-group i {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #86909C;
  font-size: 18px;
  transition: color 0.2s;
}

.input-group input {
  width: 100%;
  height: 48px;
  background-color: #F2F3F5;
  border: 1px solid transparent;
  border-radius: 8px;
  padding-left: 45px;
  padding-right: 15px;
  box-sizing: border-box;
  font-size: 15px;
  color: #1D2129;
  transition: all 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.input-group input:focus {
  background-color: #fff;
  border-color: #165DFF;
  box-shadow: 0 0 0 3px rgba(22, 93, 255, 0.1);
  outline: none;
}

.input-group input:focus + i {
  color: #165DFF;
}

/* 身份选择器样式 */
.type-selector {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
}

.type-item {
  flex: 1;
  height: 44px;
  background-color: #F2F3F5;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s;
  color: #4E5969;
  font-size: 14px;
  border: 1px solid transparent;
}

.type-item.active {
  background-color: rgba(22, 93, 255, 0.05);
  border-color: #165DFF;
  color: #165DFF;
}

/* 按钮按压感动画 */
.btn-login {
  width: 100%;
  height: 48px;
  background-color: #165DFF;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 4px 12px rgba(22, 93, 255, 0.2);
}

.btn-login:hover {
  background-color: #0E42D2;
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(22, 93, 255, 0.3);
}

.btn-login:active {
  transform: scale(0.96); /* 轻微缩小 */
  background-color: #0B35A8; /* 背景变深 */
  box-shadow: 0 2px 8px rgba(22, 93, 255, 0.2);
}

.login-footer {
  margin-top: 25px;
  text-align: center;
  font-size: 14px;
  color: #86909C;
}

.login-footer a {
  color: #165DFF;
  font-weight: 600;
  cursor: pointer;
  margin-left: 5px;
  transition: all 0.2s;
}

.login-footer a:active {
  opacity: 0.7;
  transform: scale(0.95);
  display: inline-block;
}

/* 针对移动端的微调 */
@media (max-width: 480px) {
  .login-card {
    width: 85vw;
    padding: 30px 20px;
  }
}
</style>
