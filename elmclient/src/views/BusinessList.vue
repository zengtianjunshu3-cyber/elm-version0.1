<template>
  <div class="wrapper">

    <!-- header部分 -->
    <header>
      <p>商家列表</p>
    </header>

    <!-- 商家列表部分 -->
    <ul class="business" v-if="businessArr.length > 0">
      <li v-for="item in businessArr" :key="item.businessId" @click="toBusinessInfo(item.businessId)">
        <div class="business-img">
          <img :src="getImg(item.businessImg)">
          <div class="business-img-quantity" v-show="item.quantity>0">{{item.quantity}}</div>
        </div>
        <div class="business-info">
          <h3>{{item.businessName}}</h3>
          <p>&#165;{{item.starPrice}}起送 | &#165;{{item.deliveryPrice}}配送</p>
          <p>{{item.businessExplain}}</p>
        </div>
      </li>
    </ul>

    <!-- 无搜索结果提示 -->
    <div class="no-results" v-else>
      <i class="fa fa-search"></i>
      <p>抱歉，没有找到相关商家</p>
      <button @click="$router.push('/index')">返回首页看看</button>
    </div>

    <!-- 底部菜单部分 -->
    <Footer></Footer>

  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'BusinessList',
  data() {
    return {
      orderTypeId: this.$route.query.orderTypeId,
      businessName: this.$route.query.businessName,
      businessArr: [],
      user: {}
    }
  },
  created() {
    this.user = this.$getSessionStorage('user');
    this.loadBusinessList();
  },
  watch: {
    '$route.query': {
      deep: true,
      handler() {
        this.orderTypeId = this.$route.query.orderTypeId;
        this.businessName = this.$route.query.businessName;
        this.loadBusinessList();
      }
    }
  },
  components: {
    Footer
  },
  methods: {
    loadBusinessList() {
      if (this.businessName) {
        this.$axios.post('BusinessController/listBusinessByName', this.$qs.stringify({
          businessName: this.businessName
        })).then(response => {
          this.businessArr = response.data || [];
          if (this.user != null) {
            this.listCart();
          }
        }).catch(error => {
          console.error(error);
          this.businessArr = [];
        });
        return;
      }

      let ot = this.orderTypeId;
      if (ot === undefined || ot === null || ot === '') {
        this.$axios.post('BusinessController/listBusiness').then(response => {
          this.businessArr = response.data || [];
          if (this.user != null) {
            this.listCart();
          }
        }).catch(error => {
          console.error(error);
          this.businessArr = [];
        });
        return;
      }

      ot = parseInt(ot, 10);
      if (!Number.isFinite(ot) || ot <= 0) {
        this.$axios.post('BusinessController/listBusiness').then(response => {
          this.businessArr = response.data || [];
          if (this.user != null) {
            this.listCart();
          }
        }).catch(error => {
          console.error(error);
          this.businessArr = [];
        });
        return;
      }
      this.$axios.post('BusinessController/listBusinessByOrderTypeId', this.$qs.stringify({
        orderTypeId: ot
      })).then(response => {
        this.businessArr = response.data || [];
        if (this.user != null) {
          this.listCart();
        }
      }).catch(error => {
        console.error(error);
        this.businessArr = [];
      });
    },
    getImg(imgName) {
      if (!imgName) return require('../assets/sj01.png');
      
      // 1. 如果是上传的图片（UUID 命名），通过后端映射路径访问
      if (imgName.length > 20 || imgName.includes('-')) {
        return this.$axios.defaults.baseURL + 'uploads/' + imgName;
      }

      // 2. 如果是系统内置图片名（如 sj01, dcfl01 等），从 assets 加载
      try {
        let baseName = imgName.replace(/\.(png|jpg|jpeg|gif)$/i, '');
        return require('../assets/' + baseName + '.png');
      } catch (e) {
        // 如果 assets 中没有该图片，返回默认商家图片
        return require('../assets/sj01.png');
      }
    },
    listCart() {
      this.$axios.post('CartController/listCart', this.$qs.stringify({
        userId: this.user.userId
      })).then(response => {
        let cartArr = response.data;
        //遍历所有食品列表 
        for (let businessItem of this.businessArr) {
          this.$set(businessItem, 'quantity', 0);
          for (let cartItem of cartArr) {
            if (cartItem.businessId == businessItem.businessId) {
              businessItem.quantity += cartItem.quantity;
            }
          }
        }
      }).catch(error => {
        console.error(error);
      });
    },
    toBusinessInfo(businessId) {
      this.$router.push({
        path: '/businessInfo',
        query: {
          businessId: businessId
        }
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

/****************** 商家列表部分 ******************/
.wrapper .business {
  width: 100%;
  margin-top: 12vw;
  margin-bottom: 14vw;
}

.wrapper .business li {
  width: 100%;
  box-sizing: border-box;
  padding: 2.5vw;
  border-bottom: solid 1px #DDD;
  user-select: none;
  cursor: pointer;
  transition: background-color 0.2s;

  display: flex;
  align-items: center;
}

.wrapper .business li:active {
  background-color: #f5f5f5;
}

.wrapper .business li .business-img {
  /*这里设置为相当定位，成为business-img-quantity元素的父元素*/
  position: relative;
}

.wrapper .business li .business-img img {
  width: 20vw;
  height: 20vw;
}

.wrapper .business li .business-img .business-img-quantity {
  width: 5vw;
  height: 5vw;
  background-color: red;
  color: #fff;
  font-size: 3.6vw;
  border-radius: 2.5vw;

  display: flex;
  justify-content: center;
  align-items: center;

  /*设置成绝对定位，不占文档流空间*/
  position: absolute;
  right: -1.5vw;
  top: -1.5vw;
}

.wrapper .business li .business-info {
  margin-left: 3vw;
}

.wrapper .business li .business-info h3 {
  font-size: 3.8vw;
  color: #555;
}

.wrapper .business li .business-info p {
  font-size: 3vw;
  color: #888;
  margin-top: 2vw;
}

/****************** 无搜索结果提示 ******************/
.no-results {
  width: 100%;
  padding-top: 30vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #999;
}

.no-results i {
  font-size: 15vw;
  color: #ddd;
  margin-bottom: 5vw;
}

.no-results p {
  font-size: 4vw;
  margin-bottom: 8vw;
}

.no-results button {
  padding: 2.5vw 8vw;
  background-color: #0097FF;
  color: #fff;
  border: none;
  border-radius: 1vw;
  font-size: 3.8vw;
  cursor: pointer;
}
</style>
