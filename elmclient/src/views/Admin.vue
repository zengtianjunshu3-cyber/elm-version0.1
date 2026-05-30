<template>
  <div class="wrapper">
    <header>
      <p>管理后台</p>
    </header>

    <div class="container">
      <!-- 添加商家表单 -->
      <section class="admin-section">
        <h3>添加商家</h3>
        <div class="form-group">
          <input type="text" v-model="business.businessName" placeholder="商家名称">
          <input type="text" v-model="business.businessAddress" placeholder="商家地址">
          <input type="text" v-model="business.businessExplain" placeholder="商家说明">
          <select v-model="business.orderTypeId">
            <option value="1">美食</option>
            <option value="2">早餐</option>
            <option value="3">跑腿代购</option>
            <option value="4">汉堡披萨</option>
            <option value="5">甜品饮品</option>
            <option value="6">速食简餐</option>
            <option value="7">地方小吃</option>
            <option value="8">米粉面馆</option>
            <option value="9">包子粥铺</option>
            <option value="10">炸鸡炸串</option>
          </select>
          <input type="number" v-model="business.starPrice" placeholder="起送费">
          <input type="number" v-model="business.deliveryPrice" placeholder="配送费">
          <button @click="addBusiness">保存商家</button>
        </div>
      </section>

      <div class="divider"></div>

      <!-- 添加食品表单 -->
      <section class="admin-section">
        <h3>添加食品</h3>
        <div class="form-group">
          <input type="number" v-model="food.businessId" placeholder="商家ID (businessId)">
          <input type="text" v-model="food.foodName" placeholder="食品名称">
          <input type="text" v-model="food.foodExplain" placeholder="食品说明">
          <input type="number" v-model="food.foodPrice" placeholder="食品价格">
          <button @click="addFood">保存食品</button>
        </div>
      </section>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'Admin',
  data() {
    return {
      business: {
        businessName: '',
        businessAddress: '',
        businessExplain: '',
        orderTypeId: 1,
        starPrice: '',
        deliveryPrice: '',
        businessImg: 'sj01.png'
      },
      food: {
        businessId: '',
        foodName: '',
        foodExplain: '',
        foodPrice: '',
        foodImg: 'dcfl01.png'
      }
    }
  },
  methods: {
    addBusiness() {
      if (!this.business.businessName) {
        alert('请输入商家名称');
        return;
      }
      this.$axios.post('BusinessController/saveBusiness', this.$qs.stringify(this.business))
        .then(response => {
          if (response.data > 0) {
            alert('商家添加成功，ID为: ' + response.data);
            this.food.businessId = response.data; // 方便接着添加食品
          } else {
            alert('商家添加失败');
          }
        }).catch(error => {
          console.error(error);
        });
    },
    addFood() {
      if (!this.food.businessId || !this.food.foodName) {
        alert('请完整填写食品信息');
        return;
      }
      this.$axios.post('FoodController/saveFood', this.$qs.stringify(this.food))
        .then(response => {
          if (response.data > 0) {
            alert('食品添加成功');
          } else {
            alert('食品添加失败');
          }
        }).catch(error => {
          console.error(error);
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
  margin-top: 12vw;
  padding: 4vw;
  margin-bottom: 14vw;
}
.admin-section {
  background: #f9f9f9;
  padding: 4vw;
  border-radius: 2vw;
  margin-bottom: 4vw;
}
h3 {
  font-size: 4.5vw;
  margin-bottom: 3vw;
  color: #333;
}
.form-group {
  display: flex;
  flex-direction: column;
}
input, select {
  margin-bottom: 2vw;
  padding: 2vw;
  border: 1px solid #ddd;
  border-radius: 1vw;
  font-size: 3.5vw;
}
button {
  background-color: #0097FF;
  color: white;
  border: none;
  padding: 3vw;
  border-radius: 1vw;
  font-size: 4vw;
  cursor: pointer;
}
button:active {
  background-color: #0080ff;
}
.divider {
  height: 1px;
  background: #eee;
  margin: 4vw 0;
}
</style>
