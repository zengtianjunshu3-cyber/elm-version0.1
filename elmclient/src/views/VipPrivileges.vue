<template>
  <div class="wrapper">
    <!-- header部分 -->
    <header>
      <i class="fa fa-angle-left" @click="back"></i>
      <p>VIP特权</p>
    </header>

    <div class="container">
      <!-- VIP 展示卡片 -->
      <div class="vip-banner">
        <div class="banner-content">
          <img src="../assets/super_member.png" class="vip-logo">
          <div class="text">
            <h3>{{user.isVip == 1 ? '超级会员·已开通' : '超级会员·未开通'}}</h3>
            <p>预计一年可省 &#165;1200</p>
          </div>
        </div>
      </div>

      <!-- 特权列表 -->
      <div class="privilege-section">
        <h3 class="section-title">专属特权</h3>
        <div class="privilege-grid">
          <div class="privilege-item">
            <i class="fa fa-ticket" style="color: #FF80AB;"></i>
            <p>每月领红包</p>
            <span>总价值30元</span>
          </div>
          <div class="privilege-item">
            <i class="fa fa-truck" style="color: #4FC3F7;"></i>
            <p>配送费减免</p>
            <span>部分商家适用</span>
          </div>
          <div class="privilege-item">
            <i class="fa fa-star" style="color: #FFD54F;"></i>
            <p>双倍金币</p>
            <span>购物返利翻倍</span>
          </div>
          <div class="privilege-item">
            <i class="fa fa-headphones" style="color: #81C784;"></i>
            <p>专属客服</p>
            <span>优先接入响应</span>
          </div>
        </div>
      </div>

      <!-- 套餐选择 -->
      <div class="plan-section" v-if="user.isVip == 0">
        <h3 class="section-title">选择套餐</h3>
        <div class="plan-list">
          <div 
            v-for="(item, index) in plans" 
            :key="index" 
            class="plan-item" 
            :class="{ active: selectedIndex === index }"
            @click="selectedIndex = index"
          >
            <div class="plan-info">
              <span class="duration">{{item.duration}}</span>
              <span class="price">&#165;{{item.price}}</span>
            </div>
            <span class="original-price">&#165;{{item.originalPrice}}</span>
          </div>
        </div>
      </div>

      <!-- 底部支付按钮 -->
      <div class="footer-action" v-if="user.isVip == 0">
        <div class="price-summary">
          <span class="label">合计:</span>
          <span class="total">&#165;{{plans[selectedIndex].price}}</span>
        </div>
        <button class="pay-btn" @click="handleApply">立即申请开通</button>
      </div>
      <div class="footer-action disabled" v-else>
        <button class="pay-btn" disabled>您已是超级会员</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VipPrivileges',
  data() {
    return {
      user: {},
      selectedIndex: 0,
      plans: [
        { duration: '1个月', price: 15, originalPrice: 20 },
        { duration: '3个月', price: 40, originalPrice: 60 }
      ]
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
  },
  methods: {
    back() {
      this.$router.go(-1);
    },
    handleApply() {
      const selectedPlan = this.plans[this.selectedIndex];
      if (confirm(`确认支付 ${selectedPlan.price} 元开通 ${selectedPlan.duration}超级会员吗？`)) {
        this.user.isVip = 1;
        this.$axios.post('UserController/updateUser', this.$qs.stringify(this.user)).then(response => {
          if (response.data > 0) {
            this.$setSessionStorage('user', this.user);
            alert('恭喜！VIP开通成功！');
            this.$router.push('/mine');
          }
        });
      }
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
  padding: 15vw 4vw 25vw;
}

.vip-banner {
  background: linear-gradient(135deg, #4A4A4A 0%, #2D2D2D 100%);
  border-radius: 3vw;
  padding: 6vw;
  color: #FFE4B5;
  margin-bottom: 6vw;
}

.banner-content {
  display: flex;
  align-items: center;
}

.vip-logo {
  width: 12vw;
  height: 12vw;
  margin-right: 4vw;
}

.text h3 {
  font-size: 5vw;
  margin-bottom: 1vw;
}

.text p {
  font-size: 3vw;
  opacity: 0.8;
}

.section-title {
  font-size: 4.2vw;
  font-weight: 700;
  color: #333;
  margin-bottom: 4vw;
}

.privilege-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 3vw;
  margin-bottom: 8vw;
}

.privilege-item {
  background-color: #fff;
  padding: 4vw;
  border-radius: 2vw;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.privilege-item i {
  font-size: 8vw;
  margin-bottom: 2vw;
}

.privilege-item p {
  font-size: 3.8vw;
  font-weight: 700;
  color: #333;
  margin-bottom: 1vw;
}

.privilege-item span {
  font-size: 2.8vw;
  color: #999;
}

.plan-list {
  display: flex;
  gap: 3vw;
}

.plan-item {
  flex: 1;
  background-color: #fff;
  border: 2px solid #EEE;
  border-radius: 2vw;
  padding: 4vw;
  text-align: center;
  position: relative;
}

.plan-item.active {
  border-color: #FFD100;
  background-color: #FFFDF0;
}

.plan-info {
  display: flex;
  flex-direction: column;
}

.duration {
  font-size: 3.5vw;
  color: #333;
  margin-bottom: 2vw;
}

.price {
  font-size: 6vw;
  font-weight: 700;
  color: #E67E22;
}

.original-price {
  font-size: 3vw;
  color: #999;
  text-decoration: line-through;
  margin-top: 1vw;
}

.footer-action {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 18vw;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 4vw;
  box-sizing: border-box;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.05);
}

.price-summary {
  display: flex;
  align-items: baseline;
}

.price-summary .label {
  font-size: 3.5vw;
  color: #666;
}

.price-summary .total {
  font-size: 7vw;
  font-weight: 700;
  color: #E67E22;
  margin-left: 2vw;
}

.pay-btn {
  background-color: #FFD100;
  color: #333;
  border: none;
  padding: 3vw 8vw;
  border-radius: 6vw;
  font-size: 4vw;
  font-weight: 700;
  cursor: pointer;
}

.footer-action.disabled .pay-btn {
  background-color: #EEE;
  color: #999;
  cursor: not-allowed;
  width: 100%;
}
</style>