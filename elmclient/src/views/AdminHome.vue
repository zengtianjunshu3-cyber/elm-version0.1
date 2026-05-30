<template>
  <div class="wrapper">
    <header>
      <p>管理员后台</p>
    </header>

    <ul class="admin-menu">
      <li @click="toManageBusinessFood">
        <i class="fa fa-cutlery"></i>
        <div class="menu-text">
          <h3>选项1：商家与食品管理</h3>
          <p>增删商家信息及店铺内食品（支持图片上传）</p>
        </div>
        <i class="fa fa-angle-right"></i>
      </li>
      <li @click="toManageCategory">
        <i class="fa fa-th-large"></i>
        <div class="menu-text">
          <h3>选项2：专区商家管理</h3>
          <p>调整10个专区内包含的商家列表</p>
        </div>
        <i class="fa fa-angle-right"></i>
      </li>
      <li @click="logout">
        <i class="fa fa-sign-out"></i>
        <div class="menu-text">
          <h3>退出登录</h3>
          <p>安全退出管理后台</p>
        </div>
        <i class="fa fa-angle-right"></i>
      </li>
    </ul>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'AdminHome',
  methods: {
    toManageBusinessFood() {
      this.$router.push('/manageBusinessFood');
    },
    toManageCategory() {
      this.$router.push('/manageCategory');
    },
    logout() {
      this.$removeSessionStorage('user');
      this.$router.push('/login');
    }
  },
  components: {
    Footer
  }
}
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
}

header {
  width: 100%;
  height: 12vw;
  background-color: #0097FF;
  color: #fff;
  font-size: 4.8vw;
  font-weight: 700;
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
}

.admin-menu {
  width: 100%;
  margin-top: 12vw;
  background-color: #fff;
}

.admin-menu li {
  width: 100%;
  box-sizing: border-box;
  padding: 6vw 4vw;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.admin-menu li:active {
  background-color: #fafafa;
}

.admin-menu li i:first-child {
  font-size: 8vw;
  color: #0097FF;
  margin-right: 4vw;
  width: 10vw;
  text-align: center;
}

.admin-menu li .menu-text {
  flex: 1;
}

.admin-menu li .menu-text h3 {
  font-size: 4.2vw;
  color: #333;
  margin-bottom: 1.5vw;
}

.admin-menu li .menu-text p {
  font-size: 3.2vw;
  color: #999;
}

.admin-menu li i:last-child {
  font-size: 6vw;
  color: #ccc;
}
</style>
