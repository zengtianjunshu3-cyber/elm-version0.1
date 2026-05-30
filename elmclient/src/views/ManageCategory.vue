<template>
  <div class="wrapper">
    <header>
      <i class="fa fa-angle-left" @click="back"></i>
      <p>专区商家管理</p>
    </header>

    <div class="container">
      <!-- 专区选择 -->
      <div class="category-select">
        <p>选择专区：</p>
        <select v-model="selectedCategoryId" @change="onCategoryChange">
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">
            {{ cat.name }}
          </option>
        </select>
      </div>

      <!-- 当前专区商家列表 -->
      <section class="admin-section">
        <h3>当前专区内的商家</h3>
        <ul class="list-box">
          <li v-for="item in categoryBusinesses" :key="item.businessId">
            <div class="item-info">
              <p class="name">{{ item.businessName }}</p>
              <p class="desc">ID: {{ item.businessId }}</p>
            </div>
            <button class="remove-btn" @click="removeFromCategory(item)">移出该专区</button>
          </li>
          <li v-if="categoryBusinesses.length === 0" class="empty">该专区暂无商家</li>
        </ul>
      </section>

      <!-- 未归类或所有商家列表，用于添加 -->
      <section class="admin-section">
        <h3>添加商家到此专区</h3>
        <div class="search-box">
          <input type="text" v-model="searchId" placeholder="输入商家ID进行添加">
          <button @click="addToCategoryById">添加</button>
        </div>
        <p class="hint">或者从下方列表中选择：</p>
        <ul class="list-box">
          <li v-for="item in otherBusinesses" :key="item.businessId">
            <div class="item-info">
              <p class="name">{{ item.businessName }}</p>
              <p class="desc">当前专区: {{ getCategoryName(item.orderTypeId) }}</p>
            </div>
            <button class="add-btn" :disabled="item.orderTypeId == selectedCategoryId" @click="addToCategory(item)">
              {{ item.orderTypeId == selectedCategoryId ? '已在该专区' : '加入专区' }}
            </button>
          </li>
        </ul>
      </section>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'ManageCategory',
  data() {
    return {
      selectedCategoryId: 1,
      categories: [
        { id: 1, name: '美食' },
        { id: 2, name: '早餐' },
        { id: 3, name: '跑腿代购' },
        { id: 4, name: '汉堡披萨' },
        { id: 5, name: '甜品饮品' },
        { id: 6, name: '速食简餐' },
        { id: 7, name: '地方小吃' },
        { id: 8, name: '米粉面馆' },
        { id: 9, name: '包子粥铺' },
        { id: 10, name: '炸鸡炸串' }
      ],
      categoryBusinesses: [],
      otherBusinesses: [],
      searchId: ''
    }
  },
  created() {
    this.refreshCategoryBusinesses();
    this.refreshAllBusinesses();
  },
  methods: {
    back() {
      this.$router.push('/adminHome');
    },
    getCategoryName(id) {
      const cat = this.categories.find(c => c.id == id);
      return cat ? cat.name : '未归类';
    },
    onCategoryChange() {
      this.refreshCategoryBusinesses();
      this.refreshAllBusinesses();
    },
    refreshCategoryBusinesses() {
      this.$axios.post('BusinessController/listBusinessByOrderTypeId', this.$qs.stringify({
        orderTypeId: this.selectedCategoryId
      })).then(response => {
        this.categoryBusinesses = response.data;
      });
    },
    refreshAllBusinesses() {
      this.$axios.post('BusinessController/listBusiness').then(response => {
        this.otherBusinesses = response.data || [];
      }).catch(error => {
        console.error(error);
      });
    },
    addToCategory(business) {
      if (business.orderTypeId == this.selectedCategoryId) return;
      business.orderTypeId = this.selectedCategoryId;
      this.$axios.post('BusinessController/updateBusiness', this.$qs.stringify(business)).then(response => {
        if (response.data > 0) {
          alert('操作成功！');
          this.refreshCategoryBusinesses();
          this.refreshAllBusinesses();
        }
      });
    },
    addToCategoryById() {
      if (!this.searchId) return;
      this.$axios.post('BusinessController/getBusinessById', this.$qs.stringify({ businessId: this.searchId })).then(response => {
        const business = response.data;
        if (business) {
          this.addToCategory(business);
          this.searchId = '';
        } else {
          alert('未找到该商家！');
        }
      });
    },
    removeFromCategory(business) {
      business.orderTypeId = 0; // 设置为0代表不属于任何专区
      this.$axios.post('BusinessController/updateBusiness', this.$qs.stringify(business)).then(response => {
        if (response.data > 0) {
          alert('已移出专区！');
          this.refreshCategoryBusinesses();
          this.refreshAllBusinesses();
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
.wrapper { width: 100%; height: 100%; background-color: #f5f5f5; }
header {
  width: 100%; height: 12vw; background-color: #0097FF; color: #fff;
  display: flex; align-items: center; position: fixed; left: 0; top: 0; z-index: 1000;
}
header i { font-size: 8vw; margin-left: 3vw; width: 10vw; }
header p { flex: 1; text-align: center; font-size: 4.8vw; font-weight: 700; margin-right: 13vw; }

.container { margin-top: 12vw; padding: 3vw; margin-bottom: 14vw; }
.category-select { background: #fff; padding: 4vw; display: flex; align-items: center; margin-bottom: 4vw; border-radius: 2vw; }
.category-select p { font-size: 4vw; color: #333; margin-right: 2vw; }
.category-select select { flex: 1; padding: 2vw; border-radius: 1vw; border: 1px solid #ddd; }

.admin-section { background: #fff; border-radius: 2vw; padding: 3vw; margin-bottom: 4vw; }
.admin-section h3 { font-size: 4vw; border-bottom: 1px solid #eee; padding-bottom: 2vw; margin-bottom: 2vw; }

.search-box { display: flex; margin-bottom: 3vw; }
.search-box input { flex: 1; padding: 2vw; border: 1px solid #ddd; border-radius: 1vw 0 0 1vw; }
.search-box button { background: #0097FF; color: #fff; border: none; padding: 0 4vw; border-radius: 0 1vw 1vw 0; }

.hint { font-size: 3vw; color: #999; margin-bottom: 2vw; }

.list-box li { display: flex; align-items: center; padding: 3vw; border-bottom: 1px solid #f9f9f9; }
.item-info { flex: 1; }
.item-info .name { font-size: 3.8vw; color: #333; }
.item-info .desc { font-size: 3vw; color: #999; margin-top: 1vw; }

.add-btn { background: #4cd964; color: #fff; border: none; padding: 1.5vw 3vw; border-radius: 1vw; font-size: 3vw; }
.add-btn:disabled { background: #d9d9d9; color: #fff; }
.remove-btn { background: #ff4d4f; color: #fff; border: none; padding: 1.5vw 3vw; border-radius: 1vw; font-size: 3vw; }
.empty { justify-content: center; color: #999; font-size: 3.5vw; padding: 8vw 0; }
</style>
