<template>
  <div class="wrapper">
    <!-- header部分 -->
    <header>
      <p>我的</p>
    </header>

    <div class="container">
      <!-- 用户信息部分 (现代风格) -->
      <div class="user-info-section">
        <div class="user-header">
          <div class="user-main">
            <div class="avatar-wrapper">
              <img :src="userAvatar" class="avatar-img" @error="handleAvatarError">
            </div>
            <div class="user-text">
              <h3 class="username">{{user.userName}}</h3>
              <p class="user-id">账号: {{user.userId}}</p>
            </div>
          </div>
          <div class="settings-box" @click="toEditProfile">
            <i class="fa fa-cog"></i>
          </div>
        </div>
        
        <!-- 会员卡片 (现代风格) -->
        <div class="vip-card" @click="toVipPrivileges">
          <div class="vip-left">
            <img src="../assets/super_member.png" class="vip-logo">
            <div class="vip-text">
              <p class="vip-title">{{user.isVip == 1 ? '超级会员' : '加入超级会员'}}</p>
              <p class="vip-subtitle">{{user.isVip == 1 ? '已享会员特权' : '每月领大额红包'}}</p>
            </div>
          </div>
          <button class="vip-action-btn">{{user.isVip == 1 ? '立即查看' : '立即开通'}}</button>
        </div>
      </div>

      <!-- 资产信息 -->
      <div class="assets-section">
        <div class="asset-item">
          <span class="asset-value">{{user.redEnvelope || 0}}</span>
          <span class="asset-label">红包</span>
        </div>
        <div class="asset-item">
          <span class="asset-value">{{user.goldCoins || 0}}</span>
          <span class="asset-label">金币</span>
        </div>
        <div class="asset-item">
          <span class="asset-value">0</span>
          <span class="asset-label">余额</span>
        </div>
      </div>

      <!-- 功能列表 (现代风格) -->
      <div class="menu-section">
        <div class="menu-item" @click="toUserAddress">
          <div class="menu-left">
            <i class="fa fa-map-marker address-icon"></i>
            <span>我的地址</span>
          </div>
          <i class="fa fa-angle-right"></i>
        </div>
        <div class="menu-item" @click="toVipPrivileges">
          <div class="menu-left">
            <i class="fa fa-diamond vip-icon-menu"></i>
            <span>VIP特权</span>
          </div>
          <i class="fa fa-angle-right"></i>
        </div>
      </div>

      <!-- 退出/切换账号 -->
      <div class="logout-section">
        <button class="action-btn switch-account" @click="switchAccount">切换账号</button>
        <button class="action-btn logout" @click="logout">退出登录</button>
      </div>
    </div>

    <!-- 底部菜单 -->
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'Mine',
  data() {
    return {
      user: {}
    }
  },
  computed: {
    userAvatar() {
      return this.getImg(this.user.userImg);
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
    if (!this.user) {
      this.$router.push('/login');
    }
  },
  methods: {
    getImg(imgName) {
      // 1. 如果没有图片名，使用默认的用户占位图标
      if (!imgName) {
        return 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      }
      
      // 2. 如果是完整的 URL 或 base64，直接返回
      if (imgName.startsWith('http') || imgName.startsWith('data:')) {
        return imgName;
      }
      
      // 3. 针对系统内置的简短图片名（如 sj01, dcfl01 等），从 assets 加载
      if (imgName.length < 20 && !imgName.includes('-')) {
        try {
          let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
          return require('../assets/' + baseName + '.png');
        } catch (e) {
          // 加载失败则回退
        }
      }
      
      // 4. 对于上传的图片（UUID 命名），通过后端映射的 /uploads/ 路径访问
      // 使用 axios 的 baseURL，确保图片加载地址与接口地址一致
      return this.$axios.defaults.baseURL + 'uploads/' + imgName + '?t=' + new Date().getTime();
    },
    handleAvatarError(e) {
      // 如果图片加载失败，切换到默认占位图
      e.target.src = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
    },
    triggerUpload() {
      this.$refs.fileInput.click();
    },
    handleUpload(event) {
      const file = event.target.files[0];
      if (!file) return;

      // 立即生成本地预览，提升用户体验
      const reader = new FileReader();
      reader.onload = (e) => {
        // 先在界面上显示预览图
        this.user.userImg = e.target.result;
      };
      reader.readAsDataURL(file);

      let formData = new FormData();
      formData.append('file', file);

      this.$axios.post('CommonController/upload', formData).then(response => {
        if (response.data) {
          // 上传成功后，将文件名保存到数据库
          this.user.userImg = response.data;
          this.updateUserInfo();
        }
      }).catch(error => {
        console.error('上传失败', error);
        const serverMsg = error.response && error.response.data ? error.response.data : '';
        alert('头像上传失败：' + (serverMsg || ('HTTP ' + (error.response ? error.response.status : '网络错误'))));
      });
    },
    updateUserInfo() {
      this.$axios.post('UserController/updateUser', this.$qs.stringify(this.user)).then(response => {
        if (response.data > 0) {
          // 同步更新 session 中的 user 对象
          this.$setSessionStorage('user', this.user);
          alert('头像更新成功！');
        }
      }).catch(error => {
        console.error('更新失败', error);
      });
    },
    toUserAddress() {
      this.$router.push('/userAddress');
    },
    toVipPrivileges() {
      this.$router.push('/vipPrivileges');
    },
    toEditProfile() {
      this.$router.push('/editProfile');
    },
    logout() {
      if (confirm('确定要退出登录吗？')) {
        this.$removeSessionStorage('user');
        this.$router.push('/login');
      }
    },
    switchAccount() {
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
  background-color: #F8F8F8;
}

header {
  width: 100%;
  height: 12vw;
  background-color: #fff;
  color: #333;
  font-size: 4.5vw;
  font-weight: 700;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container {
  padding: 12vw 0 16vw;
}

/* 用户信息区域 */
.user-info-section {
  background-color: #fff;
  padding: 6vw 4vw 4vw;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 6vw;
}

.user-main {
  display: flex;
  align-items: center;
}

.settings-box {
  padding: 2vw;
  cursor: pointer;
}

.settings-box i {
  font-size: 5.5vw;
  color: #999;
}

.avatar-wrapper {
  position: relative;
  width: 16vw;
  height: 16vw;
  margin-right: 4vw;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid #eee;
}

.camera-icon {
  position: absolute;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  color: #fff;
  width: 5vw;
  height: 5vw;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2.5vw;
}

.user-text {
  flex: 1;
}

.username {
  font-size: 5.5vw;
  font-weight: 700;
  color: #333;
  margin-bottom: 1vw;
}

.user-id {
  font-size: 3.2vw;
  color: #999;
}

.settings-icon {
  font-size: 5vw;
  color: #666;
}

/* VIP 卡片 */
.vip-card {
  background: linear-gradient(90deg, #3C3C3C 0%, #1A1A1A 100%);
  border-radius: 2vw;
  padding: 3vw 4vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.vip-left {
  display: flex;
  align-items: center;
}

.vip-logo {
  width: 8vw;
  height: 8vw;
  margin-right: 2vw;
}

.vip-title {
  color: #FFE4B5;
  font-size: 3.8vw;
  font-weight: 700;
}

.vip-subtitle {
  color: #999;
  font-size: 2.8vw;
  margin-top: 0.5vw;
}

.vip-action-btn {
  background-color: #FFE4B5;
  color: #5C4033;
  border: none;
  padding: 1.5vw 3vw;
  border-radius: 4vw;
  font-size: 3vw;
  font-weight: 700;
}

/* 资产区域 */
.assets-section {
  display: flex;
  background-color: #fff;
  padding: 4vw 0;
  margin-top: 1px;
}

.asset-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.asset-value {
  font-size: 4.5vw;
  font-weight: 700;
  color: #333;
}

.asset-label {
  font-size: 3vw;
  color: #666;
  margin-top: 1vw;
}

/* 菜单区域 */
.menu-section {
  margin-top: 3vw;
  background-color: #fff;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4vw;
  border-bottom: 1px solid #F0F0F0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-left {
  display: flex;
  align-items: center;
}

.menu-left i {
  font-size: 5vw;
  margin-right: 3vw;
  width: 6vw;
  text-align: center;
}

.address-icon { color: #0097FF; }
.vip-icon-menu { color: #FFD100; }

.menu-left span {
  font-size: 4vw;
  color: #333;
}

.fa-angle-right {
  color: #CCC;
  font-size: 5vw;
}

/* 退出区域 */
.logout-section {
  margin-top: 6vw;
  padding: 0 4vw;
  display: flex;
  flex-direction: column;
  gap: 3vw;
}

.action-btn {
  width: 100%;
  padding: 3.5vw;
  border-radius: 1vw;
  font-size: 4vw;
  font-weight: 700;
  border: none;
}

.switch-account {
  background-color: #fff;
  color: #333;
  border: 1px solid #DDD;
}

.logout {
  background-color: #FF4D4F;
  color: #fff;
}
</style>
