<template>
  <div class="wrapper">
    <header>
      <i class="fa fa-angle-left" @click="back"></i>
      <p>商家与食品管理</p>
    </header>

    <div class="container">
      <!-- 商家管理部分 -->
      <section class="admin-section">
        <div class="section-header">
          <h3>商家列表</h3>
          <button class="add-btn" @click="showAddBusiness = !showAddBusiness">
            {{ showAddBusiness ? '取消' : '添加新商家' }}
          </button>
        </div>

        <!-- 添加商家表单 -->
        <div v-show="showAddBusiness" class="form-box">
          <input type="text" v-model="newBusiness.businessName" placeholder="商家名称">
          <input type="text" v-model="newBusiness.businessAddress" placeholder="商家地址">
          <input type="text" v-model="newBusiness.businessExplain" placeholder="商家介绍">
          <div class="form-row">
            <div class="input-item">
              <p>分类：</p>
              <select v-model.number="newBusiness.orderTypeId">
                <option :value="1">美食</option>
                <option :value="2">早餐</option>
                <option :value="3">跑腿</option>
                <option :value="4">汉堡</option>
                <option :value="5">甜点</option>
                <option :value="6">速食</option>
                <option :value="7">小吃</option>
                <option :value="8">面馆</option>
                <option :value="9">粥铺</option>
                <option :value="10">炸鸡</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="input-item">
              <p>起送费：</p>
              <input type="number" v-model="newBusiness.starPrice">
            </div>
            <div class="input-item">
              <p>配送费：</p>
              <input type="number" v-model="newBusiness.deliveryPrice">
            </div>
          </div>
          <div class="upload-box">
            <p>商家LOGO：</p>
            <input type="file" @change="handleFileUpload($event, 'business')">
            <div class="preview-img" v-if="newBusiness.businessImg">
              <img :src="getImg(newBusiness.businessImg)">
            </div>
          </div>
          <button class="save-btn" @click="saveBusiness">保存商家</button>
        </div>

        <!-- 商家列表 -->
        <ul class="list-box">
          <li v-for="item in businessList" :key="item.businessId" :class="{ active: selectedBusinessId === item.businessId }">
            <div class="item-info" @click="selectBusiness(item.businessId)">
              <div class="item-main">
                <img :src="getImg(item.businessImg)" class="list-img">
                <div>
                  <p class="name">{{ item.businessName }} (ID:{{ item.businessId }})</p>
                  <p class="desc">{{ item.businessExplain }}</p>
                </div>
              </div>
            </div>
            <i class="fa fa-trash-o delete-icon" @click.stop="removeBusiness(item.businessId)"></i>
          </li>
        </ul>
      </section>

      <!-- 食品管理部分 (仅在选中商家后显示) -->
      <section class="admin-section" v-if="selectedBusinessId">
        <div class="section-header">
          <h3>食品列表 (商家ID:{{ selectedBusinessId }})</h3>
          <button class="add-btn" @click="showAddFood = !showAddFood">
            {{ showAddFood ? '取消' : '添加新食品' }}
          </button>
        </div>

        <!-- 添加食品表单 -->
        <div v-show="showAddFood" class="form-box">
          <input type="text" v-model="newFood.foodName" placeholder="食品名称">
          <input type="text" v-model="newFood.foodExplain" placeholder="食品介绍">
          <input type="number" v-model="newFood.foodPrice" placeholder="食品价格">
          <div class="upload-box">
            <p>食品图片：</p>
            <input type="file" @change="handleFileUpload($event, 'food')">
            <div class="preview-img" v-if="newFood.foodImg">
              <img :src="getImg(newFood.foodImg)">
            </div>
          </div>
          <button class="save-btn" @click="saveFood">保存食品</button>
        </div>

        <!-- 食品列表 -->
        <ul class="list-box">
          <li v-for="item in foodList" :key="item.foodId">
            <div class="item-info">
              <div class="item-main">
                <img :src="getImg(item.foodImg)" class="list-img">
                <div>
                  <p class="name">{{ item.foodName }} - &#165;{{ item.foodPrice }}</p>
                  <p class="desc">{{ item.foodExplain }}</p>
                </div>
              </div>
            </div>
            <i class="fa fa-trash-o delete-icon" @click="removeFood(item.foodId)"></i>
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
  name: 'ManageBusinessFood',
  data() {
    return {
      businessList: [],
      foodList: [],
      selectedBusinessId: null,
      showAddBusiness: false,
      showAddFood: false,
      newBusiness: {
        businessName: '',
        businessAddress: '',
        businessExplain: '',
        businessImg: '',
        orderTypeId: 1, // 默认美食专区
        starPrice: 0,
        deliveryPrice: 0,
        remarks: '无'
      },
      newFood: {
        foodName: '',
        foodExplain: '',
        foodPrice: '',
        foodImg: '',
        businessId: null,
        remarks: '无'
      },
      uploading: false
    }
  },
  created() {
    this.refreshBusinessList();
  },
  methods: {
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
    back() {
      this.$router.push('/adminHome');
    },
    refreshBusinessList() {
      this.$axios.post('BusinessController/listBusiness').then(response => {
        this.businessList = response.data;
      }).catch(error => {
        console.error(error);
      });
    },
    selectBusiness(id) {
      this.selectedBusinessId = id;
      this.newFood.businessId = id;
      this.refreshFoodList();
    },
    refreshFoodList() {
      this.$axios.post('FoodController/listFoodByBusinessId', this.$qs.stringify({
        businessId: this.selectedBusinessId
      })).then(response => {
        this.foodList = response.data;
      });
    },
    handleFileUpload(event, type) {
      const file = event.target.files[0];
      if (!file) return;

      this.uploading = true;
      let formData = new FormData();
      formData.append('file', file);
      this.$axios.post('CommonController/upload', formData).then(response => {
        this.uploading = false;
        let result = response.data;
        if (result && !result.startsWith('error:')) {
          if (type === 'business') {
            this.newBusiness.businessImg = result;
          } else {
            this.newFood.foodImg = result;
          }
          alert('图片上传成功！');
        } else {
          console.error('上传返回错误:', result);
          alert('图片上传失败：' + (result ? result.replace('error:', '') : '服务器未返回有效信息'));
        }
      }).catch(error => {
        this.uploading = false;
        console.error('上传失败', error);
        const serverMsg = error.response && error.response.data ? error.response.data : '';
        alert('上传失败：' + (serverMsg || ('HTTP ' + (error.response ? error.response.status : '网络错误'))));
      });
    },
    saveBusiness() {
      if (!this.newBusiness.businessName || !this.newBusiness.businessImg) {
        alert('商家名称和LOGO不能为空！');
        return;
      }
      const payload = Object.assign({}, this.newBusiness, {
        starPrice: this.newBusiness.starPrice === '' || this.newBusiness.starPrice === null || this.newBusiness.starPrice === undefined ? 0 : this.newBusiness.starPrice,
        deliveryPrice: this.newBusiness.deliveryPrice === '' || this.newBusiness.deliveryPrice === null || this.newBusiness.deliveryPrice === undefined ? 0 : this.newBusiness.deliveryPrice
      });
      this.$axios.post('BusinessController/saveBusiness', this.$qs.stringify(payload)).then(response => {
        if (typeof response.data === 'string' && response.data.startsWith('error:')) {
          alert('添加失败：' + response.data.replace('error:', ''));
          return;
        }
        if (response.data > 0) {
          alert('商家添加成功！');
          this.showAddBusiness = false;
          // 重置表单
          this.newBusiness = {
            businessName: '',
            businessAddress: '',
            businessExplain: '',
            businessImg: '',
            orderTypeId: 1,
            starPrice: 0,
            deliveryPrice: 0,
            remarks: '无'
          };
          this.refreshBusinessList();
        } else {
          alert('添加失败，请检查数据是否完整');
        }
      }).catch(error => {
        console.error('保存失败', error);
        const serverMsg = error.response && error.response.data ? error.response.data : '';
        alert('保存失败：' + (serverMsg || ('HTTP ' + (error.response ? error.response.status : '网络错误'))));
      });
    },
    removeBusiness(id) {
      if (confirm('确定要删除该商家吗？相关食品也将不可见。')) {
        this.$axios.post('BusinessController/removeBusiness', this.$qs.stringify({ businessId: id })).then(response => {
          if (response.data > 0) {
            alert('删除成功！');
            this.refreshBusinessList();
            if (this.selectedBusinessId === id) {
              this.selectedBusinessId = null;
              this.foodList = [];
            }
          }
        });
      }
    },
    saveFood() {
      if (!this.newFood.foodName || !this.newFood.foodImg || !this.newFood.foodPrice) {
        alert('食品名称、价格和图片不能为空！');
        return;
      }
      const payload = Object.assign({}, this.newFood, {
        foodPrice: this.newFood.foodPrice === '' || this.newFood.foodPrice === null || this.newFood.foodPrice === undefined ? 0 : this.newFood.foodPrice
      });
      this.$axios.post('FoodController/saveFood', this.$qs.stringify(payload)).then(response => {
        if (typeof response.data === 'string' && response.data.startsWith('error:')) {
          alert('添加失败：' + response.data.replace('error:', ''));
          return;
        }
        if (response.data > 0) {
          alert('食品添加成功！');
          this.showAddFood = false;
          // 重置表单
          this.newFood = {
            foodName: '',
            foodExplain: '',
            foodPrice: '',
            foodImg: '',
            businessId: this.selectedBusinessId,
            remarks: '无'
          };
          this.refreshFoodList();
        } else {
          alert('添加失败，请检查数据是否完整');
        }
      }).catch(error => {
        console.error('保存失败', error);
        const serverMsg = error.response && error.response.data ? error.response.data : '';
        alert('保存失败：' + (serverMsg || ('HTTP ' + (error.response ? error.response.status : '网络错误'))));
      });
    },
    removeFood(id) {
      if (confirm('确定要删除该食品吗？')) {
        this.$axios.post('FoodController/removeFood', this.$qs.stringify({ foodId: id })).then(response => {
          if (response.data > 0) {
            alert('删除成功！');
            this.refreshFoodList();
          }
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
.wrapper { width: 100%; height: 100%; background-color: #f5f5f5; }
header {
  width: 100%; height: 12vw; background-color: #0097FF; color: #fff;
  display: flex; align-items: center; position: fixed; left: 0; top: 0; z-index: 1000;
}
header i { font-size: 8vw; margin-left: 3vw; width: 10vw; }
header p { flex: 1; text-align: center; font-size: 4.8vw; font-weight: 700; margin-right: 13vw; }

.container { margin-top: 12vw; padding: 3vw; margin-bottom: 14vw; }
.admin-section { background: #fff; border-radius: 2vw; padding: 3vw; margin-bottom: 4vw; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
.section-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding-bottom: 2vw; margin-bottom: 2vw; }
.section-header h3 { font-size: 4vw; color: #333; }

.add-btn { background: #0097FF; color: #fff; border: none; padding: 1.5vw 3vw; border-radius: 1vw; font-size: 3.2vw; }
.form-box { padding: 3vw; background: #fafafa; border-radius: 1vw; margin-bottom: 3vw; }
.form-box input, .form-box select { width: 100%; box-sizing: border-box; padding: 2vw; margin-bottom: 2vw; border: 1px solid #ddd; border-radius: 1vw; background-color: #fff; }
.form-row { display: flex; gap: 2vw; margin-bottom: 2vw; }
.input-item { flex: 1; }
.input-item p { font-size: 3vw; color: #999; margin-bottom: 1vw; }
.upload-box { margin-bottom: 2vw; font-size: 3.2vw; color: #666; }
.upload-box input { border: none; }
.save-btn { width: 100%; background: #4cd964; color: #fff; border: none; padding: 2.5vw; border-radius: 1vw; font-size: 3.8vw; }

.list-box li {
  display: flex; align-items: center; padding: 3vw; border-bottom: 1px solid #f9f9f9;
}
.list-box li.active { background-color: #f0f7ff; }
.item-info { flex: 1; cursor: pointer; }
.item-main { display: flex; align-items: center; }
.list-img { width: 12vw; height: 12vw; border-radius: 1vw; margin-right: 3vw; object-fit: cover; }
.preview-img { margin-top: 2vw; }
.preview-img img { width: 20vw; height: 20vw; border-radius: 1vw; object-fit: cover; border: 1px solid #eee; }
.item-info .name { font-size: 3.8vw; color: #333; margin-bottom: 1vw; }
.item-info .desc { font-size: 3vw; color: #999; }
.delete-icon { color: #ff4d4f; font-size: 5vw; padding: 2vw; cursor: pointer; }
</style>
