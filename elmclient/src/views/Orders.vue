<template>
  <div class="wrapper">

    <!-- header部分 -->
    <header>
      <p>确认订单</p>
    </header>

    <!-- 订单信息部分 -->
    <div class="order-info">
      <h5>订单配送至：</h5>
      <div class="order-info-address" @click="toUserAddress">
        <p>{{deliveryaddress!=null?deliveryaddress.address:'请选择送货地址'}}</p>
        <i class="fa fa-angle-right"></i>
      </div>
      <p v-if="deliveryaddress!=null">{{deliveryaddress.contactName}}{{deliveryaddress.contactSex | sexFilter}} {{deliveryaddress.contactTel}}</p>
      <p v-else>{{user.userName}}{{user.userSex | sexFilter}} {{user.userId}}</p>
    </div>

    <h3>{{business.businessName}}</h3>

    <!-- 订单明细部分 -->
    <ul class="order-detailed">
      <li v-for="item in cartArr" :key="item.cartId">
        <div class="order-detailed-left">
          <img :src="getImg(item.food.foodImg)">
          <p>{{item.food.foodName}} x {{item.quantity}}</p>
        </div>
        <p>&#165;{{item.food.foodPrice*item.quantity}}</p>
      </li>
    </ul>
    <div class="order-deliveryfee">
      <p>配送费</p>
      <p>&#165;{{business.deliveryPrice}}</p>
    </div>

    <!-- 合计部分 -->
    <div class="total">
      <div class="total-left">
        &#165;{{totalPrice}}
      </div>
      <div class="total-right" @click="toPayment">
        去支付
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Orders',
  data() {
    return {
      businessId: this.$route.query.businessId,
      business: {},
      user: {},
      cartArr: [],
      deliveryaddress: {}
    }
  },
  created() {
    this.syncFromStorage();

    //查询当前商家 
    this.$axios.post('BusinessController/getBusinessById', this.$qs.stringify({
      businessId: this.businessId
    })).then(response => {
      this.business = response.data;
    }).catch(error => {
      console.error(error);
    });

    //查询当前用户在购物车中的当前商家食品列表 
    this.$axios.post('CartController/listCart', this.$qs.stringify({
      userId: this.user.userId,
      businessId: this.businessId
    })).then(response => {
      this.cartArr = response.data;
    }).catch(error => {
      console.error(error);
    });
  },
  activated() {
    this.syncFromStorage();
  },
  computed: {
    totalPrice() {
      let totalPrice = 0;
      for (let cartItem of this.cartArr) {
        totalPrice += cartItem.food.foodPrice * cartItem.quantity;
      }
      totalPrice += this.business.deliveryPrice;
      return totalPrice;
    }
  },
  filters: {
    sexFilter(value) {
      return value == 1 ? '先生' : '女士';
    }
  },
  methods: {
    syncFromStorage() {
      this.user = this.$getSessionStorage('user');
      if (this.user) {
        this.deliveryaddress = this.$getLocalStorage(this.user.userId);
      } else {
        this.deliveryaddress = null;
      }
    },
    getImg(imgName) {
      if (!imgName) return require('../assets/dcfl01.png');
      
      // 1. 如果是上传的图片（UUID 命名），通过后端映射路径访问
      if (imgName.length > 20 || imgName.includes('-')) {
        return this.$axios.defaults.baseURL + 'uploads/' + imgName;
      }

      // 2. 如果是系统内置图片名（如 sj01, dcfl01 等），从 assets 加载
      try {
        let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
        return require('../assets/' + baseName + '.png');
      } catch (e) {
        // 如果 assets 中没有该图片，返回默认食品图片
        return require('../assets/dcfl01.png');
      }
    },
    toUserAddress() {
      this.$router.push({
        path: '/userAddress',
        query: {
          businessId: this.businessId
        }
      });
    },
    toPayment() {
      if (this.deliveryaddress == null) {
        alert('请选择送货地址！');
        return;
      }

      let finalPrice = this.totalPrice;
      let vipDiscount = 0;

      // 如果是 VIP 用户，应用随机立减逻辑
      if (this.user.isVip == 1) {
        // 每个订单随机减 1.00 到 5.00 元，且不超过总价的 10%
        vipDiscount = parseFloat((Math.random() * 4 + 1).toFixed(2));
        const maxDiscount = parseFloat((this.totalPrice * 0.1).toFixed(2));
        if (vipDiscount > maxDiscount) {
          vipDiscount = maxDiscount;
        }
        finalPrice = parseFloat((this.totalPrice - vipDiscount).toFixed(2));
        console.log(`VIP 用户立减: ￥${vipDiscount}, 原价: ￥${this.totalPrice}, 现价: ￥${finalPrice}`);
      }

      //创建订单 
      this.$axios.post('OrdersController/createOrders', this.$qs.stringify({
        userId: this.user.userId,
        businessId: this.businessId,
        daId: this.deliveryaddress.daId,
        orderTotal: finalPrice // 发送立减后的价格
      })).then(response => {
        let orderId = response.data;
        if (orderId > 0) {
          if (vipDiscount > 0) {
            alert(`您是尊贵的 VIP 会员，本次订单已为您随机立减 ￥${vipDiscount}！`);
          }
          this.$router.push({
            path: '/payment',
            query: {
              orderId: orderId
            }
          });
        } else {
          alert('创建订单失败！可能原因是购物车数据异常，请重新添加商品。');
        }
      }).catch(error => {
        console.error(error);
        alert('系统繁忙，订单创建失败，请稍后再试。');
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
}

/****************** header部分 ******************/
.wrapper header {
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

/****************** 订单信息部分 ******************/
.wrapper .order-info {
  /*注意这里，不设置高，靠内容撑开。因为地址有可能折行*/
  width: 100%;
  margin-top: 12vw;
  background-color: #0097EF;
  box-sizing: border-box;
  padding: 2vw;
  color: #fff;
}

.wrapper .order-info h5 {
  font-size: 3vw;
  font-weight: 300;
}

.wrapper .order-info .order-info-address {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;

  font-weight: 700;
  user-select: none;
  cursor: pointer;
  margin: 1vw 0;
}

.wrapper .order-info .order-info-address p {
  width: 90%;
  font-size: 5vw;
}

.wrapper .order-info .order-info-address i {
  font-size: 6vw;
}

.wrapper .order-info p {
  font-size: 3vw;
}

.wrapper h3 {
  box-sizing: border-box;
  padding: 3vw;
  font-size: 4vw;
  color: #666;
  border-bottom: solid 1px #DDD;
}

/****************** 订单明细部分 ******************/
.wrapper .order-detailed {
  width: 100%;
}

.wrapper .order-detailed li {
  width: 100%;
  height: 16vw;
  box-sizing: border-box;
  padding: 3vw;
  color: #666;

  display: flex;
  justify-content: space-between;
  align-items: center;
}

.wrapper .order-detailed li .order-detailed-left {
  display: flex;
  align-items: center;
}

.wrapper .order-detailed li .order-detailed-left img {
  width: 10vw;
  height: 10vw;
}

.wrapper .order-detailed li .order-detailed-left p {
  font-size: 3.5vw;
  margin-left: 3vw;
}

.wrapper .order-detailed li p {
  font-size: 3.5vw;
}

.wrapper .order-deliveryfee {
  width: 100%;
  height: 16vw;
  box-sizing: border-box;
  padding: 3vw;
  color: #666;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 3.5vw;
}

/****************** 订单合计部分 ******************/
.wrapper .total {
  width: 100%;
  height: 14vw;

  position: fixed;
  left: 0;
  bottom: 0;

  display: flex;
}

.wrapper .total .total-left {
  flex: 2;
  background-color: #505051;
  color: #fff;
  font-size: 4.5vw;
  font-weight: 700;
  user-select: none;

  display: flex;
  justify-content: center;
  align-items: center;
}

.wrapper .total .total-right {
  flex: 1;
  background-color: #38CA73;
  color: #fff;
  font-size: 4.5vw;
  font-weight: 700;
  user-select: none;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.2s;
}

.wrapper .total .total-right:active {
  opacity: 0.8;
}
</style>
