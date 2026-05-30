<template>
  <div class="wrapper">
    <header>
      <i class="fa fa-angle-left" @click="back"></i>
      <p>修改个人信息</p>
    </header>

    <div class="container">
      <div class="edit-box">
        <!-- 头像修改 -->
        <div class="edit-item avatar-item" @click="triggerUpload">
          <span>头像</span>
          <div class="avatar-right">
            <img :src="userAvatar" class="avatar-img">
            <i class="fa fa-angle-right"></i>
          </div>
          <input type="file" ref="fileInput" @change="handleUpload" style="display: none">
        </div>

        <!-- 用户名修改 -->
        <div class="edit-item">
          <span>用户名</span>
          <div class="input-right">
            <input type="text" v-model="tempUser.userName" placeholder="请输入用户名">
          </div>
        </div>
      </div>

      <button class="save-btn" @click="saveProfile">保存修改</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EditProfile',
  data() {
    return {
      user: {},
      tempUser: {
        userName: '',
        userImg: ''
      }
    }
  },
  computed: {
    userAvatar() {
      return this.getImg(this.tempUser.userImg);
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
    if (!this.user) {
      this.$router.push('/login');
      return;
    }
    // 深拷贝一份用户信息用于编辑
    this.tempUser = JSON.parse(JSON.stringify(this.user));
  },
  methods: {
    back() {
      this.$router.go(-1);
    },
    getImg(imgName) {
      if (!imgName) {
        return 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      }
      if (imgName.startsWith('http') || imgName.startsWith('data:')) {
        return imgName;
      }
      if (imgName.length < 20 && !imgName.includes('-')) {
        try {
          let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
          return require('../assets/' + baseName + '.png');
        } catch (e) {
          // ignore
        }
      }
      return this.$axios.defaults.baseURL + 'uploads/' + imgName + '?t=' + new Date().getTime();
    },
    triggerUpload() {
      this.$refs.fileInput.click();
    },
    handleUpload(event) {
      const file = event.target.files[0];
      if (!file) return;

      // 检查文件大小 (例如限制在 2MB 以内)
      if (file.size > 2 * 1024 * 1024) {
        alert('图片大小不能超过 2MB');
        return;
      }

      // 本地预览
      const reader = new FileReader();
      reader.onload = (e) => {
        this.tempUser.userImg = e.target.result;
      };
      reader.readAsDataURL(file);

      // 上传到服务器
      let formData = new FormData();
      formData.append('file', file);
      this.$axios.post('CommonController/upload', formData).then(response => {
        // 后端返回的是文件名字符串
        if (response.data && typeof response.data === 'string') {
          this.tempUser.userImg = response.data;
          console.log('头像上传成功，文件名:', response.data);
        } else {
          console.error('上传返回数据格式不正确:', response.data);
        }
      }).catch(error => {
        console.error('上传失败', error);
        const serverMsg = error.response && error.response.data ? error.response.data : '';
        alert('图片上传失败：' + (serverMsg || ('HTTP ' + (error.response ? error.response.status : '网络错误'))));
        // 恢复之前的头像，避免一直处于 Base64 状态导致无法保存
        this.tempUser.userImg = this.user.userImg;
      });
    },
    saveProfile() {
      if (!this.tempUser.userName || !this.tempUser.userName.trim()) {
        alert('用户名不能为空');
        return;
      }
      
      // 如果头像还是 Base64，说明上传还没完成或者失败了
      if (this.tempUser.userImg && this.tempUser.userImg.startsWith('data:')) {
        alert('头像正在上传中或上传失败，请等待上传成功后再保存');
        return;
      }

      // 准备发送的数据，确保字段完整
      const updateData = {
        userId: this.tempUser.userId,
        userName: this.tempUser.userName,
        userImg: this.tempUser.userImg,
        userSex: this.tempUser.userSex !== undefined ? this.tempUser.userSex : 1,
        isVip: this.tempUser.isVip !== undefined ? this.tempUser.isVip : 0
      };

      console.log('正在提交修改:', updateData);

      this.$axios.post('UserController/updateUser', this.$qs.stringify(updateData)).then(response => {
        if (response.data > 0) {
          // 更新 SessionStorage 中的用户信息
          // 注意：这里要确保 tempUser 包含了所有字段，或者合并一下
          const updatedUser = { ...this.user, ...updateData };
          this.$setSessionStorage('user', updatedUser);
          alert('修改成功！');
          this.$router.push('/mine');
        } else {
          alert('修改失败，数据库未更新，请检查是否没有做任何变动');
        }
      }).catch(error => {
        console.error('更新失败', error);
        if (error.response) {
          alert(`请求失败: ${error.response.status} ${error.response.statusText}`);
        } else if (error.request) {
          alert('服务器未响应，请检查后端服务是否启动');
        } else {
          alert('网络请求发送失败，请检查网络连接');
        }
      });
    }
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
  display: flex;
  align-items: center;
  font-weight: 700;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
}
header i { font-size: 6vw; margin-left: 4vw; width: 10vw; }
header p { flex: 1; text-align: center; margin-right: 14vw; font-size: 4.5vw; }

.container { padding: 15vw 4vw; }
.edit-box { background-color: #fff; border-radius: 2vw; margin-bottom: 6vw; overflow: hidden; }
.edit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4vw;
  border-bottom: 1px solid #F0F0F0;
}
.edit-item:last-child { border-bottom: none; }
.edit-item span { font-size: 4vw; color: #333; }
.avatar-right { display: flex; align-items: center; }
.avatar-img { width: 12vw; height: 12vw; border-radius: 50%; margin-right: 2vw; object-fit: cover; }
.avatar-right i { color: #CCC; font-size: 5vw; }
.input-right input {
  border: none;
  text-align: right;
  font-size: 4vw;
  color: #666;
  outline: none;
}
.save-btn {
  width: 100%;
  background-color: #0097FF;
  color: #fff;
  border: none;
  padding: 3.5vw;
  border-radius: 1vw;
  font-size: 4vw;
  font-weight: 700;
}
</style>
