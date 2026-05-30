<template>
  <div class="wrapper">
    <header>
      <p>我的订单</p>
    </header>

    <div class="container">
      <h3>未支付订单</h3>
      <ul class="order-list">
        <li v-for="item in orderArr" :key="item.orderId" v-if="item.orderState==0" class="order-card">
          <div class="order-header">
            <div class="business-info">
              <img :src="getImg(item.business.businessImg)" class="business-logo">
              <div class="business-detail">
                <p class="business-name">{{item.business.businessName}} <i class="fa fa-angle-right"></i></p>
                <p class="order-time">{{item.orderDate}}</p>
              </div>
            </div>
            <div class="header-right">
              <div class="order-status unpay">未支付</div>
              <i class="fa fa-trash-o delete-icon" @click.stop="removeOrder(item.orderId)"></i>
            </div>
          </div>
          
          <div class="food-preview">
            <div class="food-images">
              <div v-for="odItem in item.list.slice(0, 3)" :key="odItem.odId" class="food-item" v-if="odItem.food">
                <img :src="getImg(odItem.food.foodImg)">
                <p>{{odItem.food.foodName}}</p>
              </div>
            </div>
            <div class="price-info">
              <p class="total-price">&#165;{{item.orderTotal}}</p>
              <p class="item-count">共{{item.list.length}}件</p>
            </div>
          </div>

          <div class="order-footer">
            <div class="address-info" v-if="item.deliveryAddress">
              <i class="fa fa-map-marker"></i>
              <p>{{item.deliveryAddress.address}}</p>
            </div>
            <div class="actions">
              <button class="pay-btn" @click="toPayment(item.orderId)">去支付</button>
            </div>
          </div>
        </li>
      </ul>

      <h3>已支付订单</h3>
      <ul class="order-list">
        <li v-for="item in orderArr" :key="item.orderId" v-if="item.orderState==1" class="order-card">
          <div class="order-header">
            <div class="business-info" @click="toBusinessInfo(item.businessId)">
              <img :src="getBusinessImg(item.business.businessImg)" class="business-logo">
              <div class="business-detail">
                <p class="business-name">{{item.business.businessName}} <i class="fa fa-angle-right"></i></p>
                <p class="order-time">{{item.orderDate}}</p>
              </div>
            </div>
            <div class="header-right">
              <div class="order-status paid">已支付</div>
              <i class="fa fa-trash-o delete-icon" @click.stop="removeOrder(item.orderId)"></i>
            </div>
          </div>
          
          <div class="food-preview">
            <div class="food-scroll-container">
              <div v-for="odItem in item.list" :key="odItem.odId" class="food-item" v-if="odItem.food">
                <div class="food-img-box">
                  <img :src="getFoodImg(odItem.food.foodImg)">
                  <span class="vip-badge" v-if="user.isVip == 1">VIP减{{getFoodVipDiscount(odItem)}}</span>
                </div>
                <p class="food-name">{{odItem.food.foodName}}</p>
              </div>
            </div>
            <div class="price-info">
              <div class="vip-savings" v-if="user.isVip == 1">
                VIP已省￥{{getOrderVipSavings(item)}}
              </div>
              <p class="total-price">&#165;{{item.orderTotal}}</p>
              <p class="item-count">共{{item.list.length}}件</p>
            </div>
          </div>

          <div class="order-footer">
            <div class="address-info" v-if="item.deliveryAddress">
              <i class="fa fa-map-marker"></i>
              <p>{{item.deliveryAddress.address}} ({{item.deliveryAddress.contactName}})</p>
            </div>
            <div class="actions">
              <button class="again-btn" @click="toBusinessInfo(item.businessId)">再来一单</button>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'OrderList',
  data() {
    return {
      orderArr: [],
      user: {}
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
    this.refreshOrderList();
  },
  methods: {
    refreshOrderList() {
      this.$axios.post('OrdersController/listOrdersByUserId', this.$qs.stringify({
        userId: this.user.userId
      })).then(response => {
        this.orderArr = response.data;
      }).catch(error => {
        console.error(error);
      });
    },
    // 专门获取商家Logo的方法
    getBusinessImg(imgName) {
      if (!imgName) return require('../assets/sj01.png');
      if (imgName.length > 20 || imgName.includes('-')) {
        return this.$axios.defaults.baseURL + 'uploads/' + imgName;
      }
      try {
        let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
        return require('../assets/' + baseName + '.png');
      } catch (e) {
        return require('../assets/sj01.png');
      }
    },
    // 专门获取食品图片的方法
    getFoodImg(imgName) {
      if (!imgName) return require('../assets/dcfl01.png');
      if (imgName.length > 20 || imgName.includes('-')) {
        return this.$axios.defaults.baseURL + 'uploads/' + imgName;
      }
      try {
        let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
        return require('../assets/' + baseName + '.png');
      } catch (e) {
        return require('../assets/dcfl01.png');
      }
    },
    // 兼容原有的 getImg 调用（虽然建议区分使用，但为了稳定性保留）
    getImg(imgName) {
      return this.getFoodImg(imgName);
    },
    // 获取食物的 VIP 随机立减金额（展示用，确定性随机）
    getFoodVipDiscount(odItem) {
      if (!odItem || !odItem.odId) return '0.0';
      // 使用 odId 作为种子，生成 0.2 到 1.5 之间的固定随机数
      const discount = ( (odItem.odId % 13) / 10 + 0.2 ).toFixed(1);
      return discount;
    },
    // 获取整个订单的 VIP 总节省金额
    getOrderVipSavings(order) {
      if (!order || !order.list) return '0.00';
      // 计算所有食物的原价总和 + 配送费
      let originalTotal = 0;
      for (let odItem of order.list) {
        if (odItem.food) {
          originalTotal += odItem.food.foodPrice * odItem.quantity;
        }
      }
      if (order.business) {
        originalTotal += order.business.deliveryPrice;
      }
      // 节省金额 = 原价总和 - 实际支付金额
      const savings = originalTotal - order.orderTotal;
      return savings > 0 ? savings.toFixed(2) : '0.00';
    },
    toPayment(orderId) {
      this.$router.push({
        path: '/payment',
        query: { orderId: orderId }
      });
    },
    toBusinessInfo(businessId) {
      this.$router.push({
        path: '/businessInfo',
        query: { businessId: businessId }
      });
    },
    removeOrder(orderId) {
      if (confirm('确定要删除这条订单记录吗？删除后不可恢复。')) {
        this.$axios.post('OrdersController/removeOrders', this.$qs.stringify({
          orderId: orderId
        })).then(response => {
          if (response.data > 0) {
            alert('订单删除成功！');
            this.refreshOrderList();
          } else {
            alert('订单删除失败，请稍后再试。');
          }
        }).catch(error => {
          console.error(error);
          alert('网络请求失败，请检查服务器连接。');
        });
      }
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
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container {
  padding: 12vw 3vw 14vw;
}

h3 {
  font-size: 4vw;
  color: #666;
  padding: 4vw 0 2vw;
  font-weight: 300;
}

.order-card {
  background-color: #fff;
  border-radius: 3vw;
  padding: 4vw;
  margin-bottom: 3vw;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 4vw;
}

.business-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.business-logo {
  width: 10vw;
  height: 10vw;
  border-radius: 1vw;
  margin-right: 3vw;
  object-fit: cover;
}

.business-name {
  font-size: 4.2vw;
  font-weight: 700;
  color: #333;
  margin-bottom: 1vw;
}

.order-time {
  font-size: 3vw;
  color: #999;
}

.order-status {
  font-size: 3.2vw;
}

.unpay { color: #ff4d4f; }
.paid { color: #999; }

.header-right {
  display: flex;
  align-items: center;
}

.delete-icon {
  margin-left: 3vw;
  font-size: 5vw;
  color: #999;
  cursor: pointer;
  transition: color 0.2s;
}

.delete-icon:hover {
  color: #ff4d4f;
}

.food-preview {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 4vw;
  overflow: hidden;
}

.food-scroll-container {
  display: flex;
  flex: 1;
  overflow-x: auto;
  padding-bottom: 1vw;
}

/* 隐藏滚动条 */
.food-scroll-container::-webkit-scrollbar {
  display: none;
}

.food-item {
  margin-right: 4vw;
  flex-shrink: 0;
  width: 18vw;
  text-align: center;
}

.food-img-box {
  position: relative;
  width: 18vw;
  height: 18vw;
  margin-bottom: 1.5vw;
}

.food-img-box img {
  width: 100%;
  height: 100%;
  border-radius: 2vw;
  object-fit: cover;
  background-color: #f9f9f9;
}

.vip-badge {
  position: absolute;
  top: 0;
  left: 0;
  background: linear-gradient(to right, #ff4d4f, #ff7875);
  color: #fff;
  font-size: 2.2vw;
  padding: 0.5vw 1.5vw;
  border-radius: 2vw 0 2vw 0;
  font-weight: 700;
  white-space: nowrap;
}

.food-name {
  font-size: 3vw;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.price-info {
  text-align: right;
  margin-left: 2vw;
  padding-bottom: 2vw; /* 调整间距以容纳 VIP 优惠文字 */
}

.vip-savings {
  font-size: 2.8vw;
  color: #ff4d4f;
  font-weight: 700;
  margin-bottom: 1vw;
  background-color: #fff1f0;
  padding: 0.5vw 1.5vw;
  border-radius: 1vw;
  display: inline-block;
}

.total-price {
  font-size: 4.5vw;
  font-weight: 700;
  color: #333;
}

.item-count {
  font-size: 3vw;
  color: #999;
}

.order-footer {
  border-top: 1px solid #eee;
  padding-top: 3vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.address-info {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 3.2vw;
  max-width: 60%;
}

.address-info i {
  color: #0097FF;
  margin-right: 1.5vw;
}

.address-info p {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.actions button {
  padding: 1.5vw 4vw;
  border-radius: 1vw;
  font-size: 3.5vw;
  border: none;
  cursor: pointer;
}

.pay-btn {
  background-color: #ff4d4f;
  color: #fff;
}

.again-btn {
  background-color: #FFD100;
  color: #333;
  font-weight: 700;
}
</style>
