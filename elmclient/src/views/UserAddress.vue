<template>
  <div class="wrapper">
    <!-- header部分 -->
    <header>
      <i class="fa fa-angle-left" @click="back"></i>
      <p>我的收货地址</p>
    </header>

    <div class="container">
      <!-- 地址列表 -->
      <ul class="address-list">
        <li v-for="item in deliveryAddressArr" :key="item.daId" class="address-card">
          <div class="card-header">
            <div class="user-info">
              <span class="contact-name">{{item.contactName}}</span>
              <span class="contact-tel">{{item.contactTel}}</span>
              <span v-if="isDefault(item.daId)" class="default-badge">默认</span>
              <span class="tag-badge">学校</span>
            </div>
          </div>
          <div class="address-detail">
            {{item.address}}
          </div>
          <div class="card-footer">
            <div class="left" @click="setDeliveryAddress(item)">
              <i class="fa" :class="isDefault(item.daId) ? 'fa-check-square' : 'fa-square-o'"></i>
              <span>已设为默认地址</span>
            </div>
            <div class="right">
              <span @click.stop="removeUserAddress(item.daId)"><i class="fa fa-trash-o"></i> 删除</span>
              <span @click.stop="copyAddress(item.address)"><i class="fa fa-copy"></i> 复制</span>
              <span @click.stop="editUserAddress(item.daId)"><i class="fa fa-edit"></i> 修改</span>
            </div>
          </div>
        </li>
      </ul>

      <!-- 新增地址按钮 -->
      <div class="add-btn-fixed" @click="toAddUserAddress">
        <i class="fa fa-plus-circle"></i> 新增收货地址
      </div>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'UserAddress',
  data() {
    return {
      businessId: this.$route.query.businessId,
      user: {},
      deliveryAddressArr: []
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
    this.listDeliveryAddressByUserId();
  },
  methods: {
    back() {
      this.$router.go(-1);
    },
    isDefault(daId) {
      let defaultAddress = this.$getLocalStorage(this.user.userId);
      return defaultAddress && defaultAddress.daId == daId;
    },
    listDeliveryAddressByUserId() {
      this.$axios.post('DeliveryAddressController/listDeliveryAddressByUserId', this.$qs.stringify({
        userId: this.user.userId
      })).then(response => {
        this.deliveryAddressArr = response.data;
      }).catch(error => {
        console.error(error);
      });
    },
    setDeliveryAddress(deliveryAddress) {
      this.$setLocalStorage(this.user.userId, deliveryAddress);
      if (this.businessId) {
        this.$router.push({
          path: '/orders',
          query: { businessId: this.businessId }
        });
      } else {
        // 如果是从“我的”进来的，仅提示修改成功
        alert('默认地址设置成功！');
      }
    },
    toAddUserAddress() {
      this.$router.push({
        path: '/addUserAddress',
        query: { businessId: this.businessId }
      });
    },
    editUserAddress(daId) {
      this.$router.push({
        path: '/editUserAddress',
        query: {
          businessId: this.businessId,
          daId: daId
        }
      });
    },
    copyAddress(text) {
      const input = document.createElement('input');
      input.setAttribute('value', text);
      document.body.appendChild(input);
      input.select();
      document.execCommand('copy');
      document.body.removeChild(input);
      alert('地址已复制到剪贴板');
    },
    removeUserAddress(daId) {
      if (!confirm('确认要删除此收货地址吗？')) return;
      this.$axios.post('DeliveryAddressController/removeDeliveryAddress', this.$qs.stringify({
        daId: daId
      })).then(response => {
        if (response.data > 0) {
          let deliveryAddress = this.$getLocalStorage(this.user.userId);
          if (deliveryAddress != null && deliveryAddress.daId == daId) {
            this.$removeLocalStorage(this.user.userId);
          }
          this.listDeliveryAddressByUserId();
        } else {
          alert('删除地址失败！');
        }
      });
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
  display: flex;
  align-items: center;
  color: #333;
  font-size: 4.5vw;
  font-weight: 700;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
}

header i {
  font-size: 6vw;
  margin-left: 4vw;
  width: 10vw;
}

header p {
  flex: 1;
  text-align: center;
  margin-right: 14vw;
}

.container {
  padding: 12vw 0 20vw;
}

.address-list {
  padding: 3vw;
}

.address-card {
  background-color: #fff;
  border-radius: 3vw;
  padding: 4vw;
  margin-bottom: 3vw;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.card-header {
  margin-bottom: 2vw;
}

.contact-name {
  font-size: 4.5vw;
  font-weight: 700;
  color: #333;
  margin-right: 2vw;
}

.contact-tel {
  font-size: 4vw;
  color: #333;
  margin-right: 2vw;
}

.default-badge {
  font-size: 2.5vw;
  color: #FF4D4F;
  background-color: #FFF1F0;
  border: 1px solid #FFCCC7;
  padding: 0 1vw;
  border-radius: 0.5vw;
  margin-right: 2vw;
}

.tag-badge {
  font-size: 2.5vw;
  color: #FA8C16;
  background-color: #FFF7E6;
  border: 1px solid #FFE7BA;
  padding: 0 1vw;
  border-radius: 0.5vw;
}

.address-detail {
  font-size: 3.8vw;
  color: #666;
  line-height: 1.5;
  margin-bottom: 4vw;
  padding-bottom: 4vw;
  border-bottom: 1px solid #F0F0F0;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 3.5vw;
  color: #999;
}

.card-footer .left {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.card-footer .left i {
  font-size: 4.5vw;
  margin-right: 1.5vw;
}

.fa-check-square { color: #0097FF; }

.card-footer .right span {
  margin-left: 4vw;
  cursor: pointer;
}

.card-footer .right span i {
  margin-right: 1vw;
}

.add-btn-fixed {
  position: fixed;
  bottom: 18vw;
  left: 5%;
  width: 90%;
  height: 12vw;
  background-color: #fff;
  border: 1px solid #0097FF;
  color: #0097FF;
  border-radius: 6vw;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 4vw;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(0,151,255,0.1);
}
</style>
