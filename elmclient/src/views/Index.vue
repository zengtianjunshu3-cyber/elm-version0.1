<template>
  <div class="wrapper">
    <!-- header部分 -->
    <header>
      <div class="icon-location-box" @click="changeLocation">
        <i class="fa fa-map-marker icon-location"></i>
        <div class="location-text">{{location}}<i class="fa fa-caret-down"></i></div>
      </div>
    </header>

    <!-- search部分 -->
    <div class="search">
      <div class="search-fixed-top" ref="fixedBox">
        <div class="search-box">
          <i class="fa fa-search"></i>
          <input type="text" v-model="searchKeywords" placeholder="搜索饿了么商家、商品名称" 
                 @keyup.enter="searchBusiness" @focus="showHistory = true">
        </div>
        
        <!-- 搜索历史面板 -->
        <div class="search-history" v-if="showHistory && (historyList.length > 0 || searchKeywords)">
          <div class="history-header" v-if="historyList.length > 0">
            <span>最近搜索</span>
            <i class="fa fa-trash-o" @click="clearHistory"></i>
          </div>
          <ul class="history-list">
            <li v-for="(item, index) in historyList" :key="index" @click="searchByHistory(item)">
              <div class="history-text">{{item}}</div>
              <i class="fa fa-close" @click.stop="removeHistory(index)"></i>
            </li>
          </ul>
          <div class="history-footer" @click="showHistory = false">关闭</div>
        </div>
      </div>
    </div>

    <!-- 点餐分类部分 -->
    <ul class="foodtype">
      <li @click="toBusinessList(1)">
        <img src="../assets/dcfl01.png">
        <p>美食</p>
      </li>
      <li @click="toBusinessList(2)">
        <img src="../assets/dcfl02.png">
        <p>早餐</p>
      </li>
      <li @click="toBusinessList(3)">
        <img src="../assets/dcfl03.png">
        <p>跑腿代购</p>
      </li>
      <li @click="toBusinessList(4)">
        <img src="../assets/dcfl04.png">
        <p>汉堡披萨</p>
      </li>
      <li @click="toBusinessList(5)">
        <img src="../assets/dcfl05.png">
        <p>甜品饮品</p>
      </li>
      <li @click="toBusinessList(6)">
        <img src="../assets/dcfl06.png">
        <p>速食简餐</p>
      </li>
      <li @click="toBusinessList(7)">
        <img src="../assets/dcfl07.png">
        <p>地方小吃</p>
      </li>
      <li @click="toBusinessList(8)">
        <img src="../assets/dcfl08.png">
        <p>米粉面馆</p>
      </li>
      <li @click="toBusinessList(9)">
        <img src="../assets/dcfl09.png">
        <p>包子粥铺</p>
      </li>
      <li @click="toBusinessList(10)">
        <img src="../assets/dcfl10.png">
        <p>炸鸡炸串</p>
      </li>
    </ul>

    <!-- 横幅广告部分 -->
    <div class="banner" @click="toBusinessList(6)">
      <h3>品质套餐</h3>
      <p>搭配齐全吃得好</p>
      <a>立即抢购 &gt;</a>
    </div>

    <!-- 超级会员部分 -->
    <div class="supermember" @click="toVipPrivileges">
      <div class="left">
        <img src="../assets/super_member.png">
        <h3>超级会员</h3>
        <p>&#8226; 每月享超值权益</p>
      </div>
      <div class="right">
        立即开通 &gt;
      </div>
    </div>

    <!-- 推荐商家部分 -->
    <div class="recommend">
      <div class="recommend-line"></div>
      <p>推荐商家</p>
      <div class="recommend-line"></div>
    </div>

    <!-- 推荐方式部分 -->
    <ul class="recommendtype">
      <li>综合排序<i class="fa fa-caret-down"></i></li>
      <li>距离最近</li>
      <li>销量最高</li>
      <li>筛选<i class="fa fa-filter"></i></li>
    </ul>

    <!-- 推荐商家列表部分 -->
    <ul class="business">
      <li v-for="item in businessArr" :key="item.businessId" @click="toBusinessInfo(item.businessId)">
        <img :src="getImg(item.businessImg)">
        <div class="business-info">
          <div class="business-info-h">
            <h3>{{item.businessName}}</h3>
            <div class="business-info-like">&#8226;</div>
          </div>
          <div class="business-info-star">
            <div class="business-info-star-left">
              <i class="fa fa-star"></i>
              <i class="fa fa-star"></i>
              <i class="fa fa-star"></i>
              <i class="fa fa-star"></i>
              <i class="fa fa-star"></i>
              <p>4.9 月售345单</p>
            </div>
            <div class="business-info-star-right">
              蜂鸟专送
            </div>
          </div>
          <div class="business-info-delivery">
            <p>&#165;{{item.starPrice}}起送 | &#165;{{item.deliveryPrice}}配送</p>
            <p>3.22km | 30分钟</p>
          </div>
          <div class="business-info-explain">
            <div>{{item.businessExplain}}</div>
          </div>
          <div class="business-info-promotion">
            <div class="business-info-promotion-left">
              <div class="business-info-promotion-left-incon">新</div>
              <p>饿了么新用户首单立减9元</p>
            </div>
            <div class="business-info-promotion-right">
              <p>2个活动</p>
              <i class="fa fa-caret-down"></i>
            </div>
          </div>
          <div class="business-info-promotion">
            <div class="business-info-promotion-left">
              <div class="business-info-promotion-left-incon" style="background-color: #F1884F;">特</div>
              <p>特价商品5元起</p>
            </div>
          </div>
        </div>
      </li>
    </ul>

    <!-- 底部菜单部分 -->
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '../components/Footer.vue';

export default {
  name: 'Index',
  data() {
    return {
      businessArr: [],
      location: '沈阳市规划大厦',
      searchKeywords: '',
      showHistory: false,
      historyList: []
    }
  },
  created() {
    // 从本地存储加载搜索历史
    let history = localStorage.getItem('searchHistory');
    if (history) {
      this.historyList = JSON.parse(history);
    }

    let savedLocation = localStorage.getItem('userLocation');
    if (savedLocation) {
      this.location = savedLocation;
    }

    this.$axios.post('BusinessController/listBusiness').then(response => {
      this.businessArr = response.data;
    }).catch(error => {
      console.error(error);
    });
  },
  mounted() {
    document.onscroll = () => {
      //获取滚动条位置 
      let s1 = document.documentElement.scrollTop;
      let s2 = document.body.scrollTop;
      let scroll = s1 == 0 ? s2 : s1;
      //获取视口宽度 
      let width = document.documentElement.clientWidth;

      //获取顶部固定块 
      let search = this.$refs.fixedBox;

      //判断滚动条超过视口宽度的12%时，搜索块变固定定位 
      if (search) {
        if (scroll > width * 0.12) {
          search.style.position = 'fixed';
          search.style.left = '0';
          search.style.top = '0';
        } else {
          search.style.position = 'static';
        }
      }
    }
  },
  destroyed() {
    //当切换到其他组件时，就不需要document滚动条事件，所以将此事件去掉 
    document.onscroll = null;
  },
  components: {
    Footer
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
    changeLocation() {
      let newLocation = prompt('请输入您的位置', this.location);
      if (newLocation && newLocation.trim()) {
        this.location = newLocation.trim();
        localStorage.setItem('userLocation', this.location);
      }
    },
    toBusinessList(orderTypeId) {
      this.$router.push({
        path: '/businessList',
        query: {
          orderTypeId: orderTypeId
        }
      });
    },
    toBusinessInfo(businessId) {
      this.$router.push({
        path: '/businessInfo',
        query: {
          businessId: businessId
        }
      });
    },
    toVipPrivileges() {
      this.$router.push('/vipPrivileges');
    },
    searchBusiness() {
      if (this.searchKeywords.trim() === '') {
        return;
      }
      
      // 保存搜索历史
      this.saveHistory(this.searchKeywords.trim());
      
      this.$router.push({
        path: '/businessList',
        query: {
          businessName: this.searchKeywords
        }
      });
    },
    saveHistory(keyword) {
      // 过滤重复并限制数量
      let index = this.historyList.indexOf(keyword);
      if (index !== -1) {
        this.historyList.splice(index, 1);
      }
      this.historyList.unshift(keyword);
      if (this.historyList.length > 10) {
        this.historyList.pop();
      }
      localStorage.setItem('searchHistory', JSON.stringify(this.historyList));
    },
    removeHistory(index) {
      this.historyList.splice(index, 1);
      localStorage.setItem('searchHistory', JSON.stringify(this.historyList));
    },
    clearHistory() {
      if (confirm('确定清空所有搜索历史吗？')) {
        this.historyList = [];
        localStorage.removeItem('searchHistory');
      }
    },
    searchByHistory(keyword) {
      this.searchKeywords = keyword;
      this.searchBusiness();
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

/****************** header ******************/
.wrapper header {
  width: 100%;
  height: 12vw;
  background-color: #0097FF;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 0 4vw;
}

.wrapper header .icon-location-box {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.wrapper header .icon-location-box .icon-location {
  color: #fff;
  font-size: 5vw;
  margin-right: 2vw;
}

.wrapper header .icon-location-box .location-text {
  font-size: 4.5vw;
  font-weight: 700;
  color: #fff;
  display: flex;
  align-items: center;
}

.wrapper header .icon-location-box .location-text i {
  margin-left: 1vw;
  font-size: 3vw;
}

/****************** search ******************/
.wrapper .search {
  width: 100%;
  height: 13vw;
}

.wrapper .search .search-fixed-top {
  width: 100%;
  height: 13vw;
  background-color: #0097FF;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.wrapper .search .search-fixed-top {
  width: 100%;
  height: 13vw;
  background-color: #0097FF;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative; /* 为历史面板定位 */
}

.wrapper .search .search-fixed-top .search-box {
  width: 92%;
  height: 9vw;
  background-color: #fff;
  border-radius: 1vw;
  display: flex;
  align-items: center;
  padding: 0 3vw;
  box-sizing: border-box;
}

.wrapper .search .search-fixed-top .search-box input {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  margin-left: 2vw;
  font-size: 3.5vw;
  color: #666;
}

.wrapper .search .search-fixed-top .search-box .fa-search {
  margin-right: 1vw;
}

/****************** 搜索历史面板 ******************/
.search-history {
  width: 100%;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
  border-radius: 0 0 2vw 2vw;
  padding: 3vw;
  box-sizing: border-box;
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 2000;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 3.5vw;
  color: #666;
  margin-bottom: 3vw;
}

.history-header i {
  cursor: pointer;
}

.history-list {
  list-style: none;
}

.history-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2.5vw 0;
  border-bottom: 1px solid #f5f5f5;
  font-size: 3.8vw;
  color: #333;
  cursor: pointer;
}

.history-text {
  flex: 1;
}

.history-list li i {
  color: #ccc;
  font-size: 4vw;
  padding: 1vw;
}

.history-footer {
  text-align: center;
  font-size: 3.5vw;
  color: #0097FF;
  margin-top: 3vw;
  cursor: pointer;
}

/****************** 点餐分类部分 ******************/
.wrapper .foodtype {
  width: 100%;
  height: 48vw;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-content: center;
}

.wrapper .foodtype li {
  width: 18vw;
  height: 20vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  user-select: none;
  cursor: pointer;
}

.wrapper .foodtype li img {
  width: 12vw;
  height: 10.3vw;
}

.wrapper .foodtype li p {
  font-size: 3.2vw;
  color: #666;
}

/****************** 横幅广告部分 ******************/
.wrapper .banner {
  width: 95%;
  margin: 0 auto;
  height: 29vw;
  background-image: url(../assets/index_banner.png);
  background-repeat: no-repeat;
  background-size: cover;
  box-sizing: border-box;
  padding: 2vw 6vw;
}

.wrapper .banner h3 {
  font-size: 4.2vw;
  margin-bottom: 1.2vw;
}

.wrapper .banner p {
  font-size: 3.4vw;
  color: #666;
  margin-bottom: 2.4vw;
}

.wrapper .banner a {
  font-size: 3vw;
  color: #C79060;
  font-weight: 700;
}

/****************** 超级会员部分 ******************/
.wrapper .supermember {
  width: 95%;
  margin: 0 auto;
  height: 11.5vw;
  background-color: #FEEDC1;
  margin-top: 1.3vw;
  border-radius: 2px;
  color: #644F1B;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.wrapper .supermember .left {
  display: flex;
  align-items: center;
  margin-left: 4vw;
  user-select: none;
}

.wrapper .supermember .left img {
  width: 6vw;
  height: 6vw;
  margin-right: 2vw;
}

.wrapper .supermember .left h3 {
  font-size: 4vw;
  margin-right: 2vw;
}

.wrapper .supermember .left p {
  font-size: 3vw;
}

.wrapper .supermember .right {
  font-size: 3vw;
  margin-right: 4vw;
  cursor: pointer;
}

/****************** 推荐商家部分 ******************/
.wrapper .recommend {
  width: 100%;
  height: 14vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

.wrapper .recommend .recommend-line {
  width: 6vw;
  height: 0.2vw;
  background-color: #888;
}

.wrapper .recommend p {
  font-size: 4vw;
  margin: 0 4vw;
}

/****************** 推荐方式部分 ******************/
.wrapper .recommendtype {
  width: 100%;
  height: 5vw;
  margin-bottom: 5vw;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.wrapper .recommendtype li {
  font-size: 3.5vw;
  color: #555;
}

/****************** 推荐商家列表部分 ******************/
.wrapper .business {
  width: 100%;
  margin-bottom: 14vw;
}

.wrapper .business li {
  width: 100%;
  box-sizing: border-box;
  padding: 2.5vw;
  user-select: none;
  border-bottom: solid 1px #DDD;
  display: flex;
  cursor: pointer;
  transition: background-color 0.2s;
}

.wrapper .business li:active {
  background-color: #f5f5f5;
}

.wrapper .business li img {
  width: 18vw;
  height: 18vw;
}

.wrapper .business li .business-info {
  width: 100%;
  box-sizing: border-box;
  padding-left: 3vw;
}

.wrapper .business li .business-info .business-info-h {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2vw;
}

.wrapper .business li .business-info .business-info-h h3 {
  font-size: 4vw;
  color: #333;
}

.wrapper .business li .business-info .business-info-h .business-info-like {
  width: 1.6vw;
  height: 3.4vw;
  background-color: #666;
  color: #fff;
  font-size: 4vw;
  margin-right: 4vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

.wrapper .business li .business-info .business-info-star {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2vw;
  font-size: 3.1vw;
}

.wrapper .business li .business-info .business-info-star .business-info-star-left {
  display: flex;
  align-items: center;
}

.wrapper .business li .business-info .business-info-star .business-info-star-left .fa-star {
  color: #FEC80E;
  margin-right: 0.5vw;
}

.wrapper .business li .business-info .business-info-star .business-info-star-left p {
  color: #666;
  margin-left: 1vw;
}

.wrapper .business li .business-info .business-info-star .business-info-star-right {
  background-color: #0097FF;
  color: #fff;
  font-size: 2.4vw;
  border-radius: 2px;
  padding: 0 0.6vw;
}

.wrapper .business li .business-info .business-info-delivery {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2vw;
  color: #666;
  font-size: 3.1vw;
}

.wrapper .business li .business-info .business-info-explain {
  display: flex;
  align-items: center;
  margin-bottom: 3vw;
}

.wrapper .business li .business-info .business-info-explain div {
  border: solid 1px #DDD;
  font-size: 2.8vw;
  color: #666;
  border-radius: 3px;
  padding: 0 0.1vw;
}

.wrapper .business li .business-info .business-info-promotion {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.8vw;
}

.wrapper .business li .business-info .business-info-promotion .business-info-promotion-left {
  display: flex;
  align-items: center;
}

.wrapper .business li .business-info .business-info-promotion .business-info-promotion-left .business-info-promotion-left-incon {
  width: 4vw;
  height: 4vw;
  background-color: #70BC46;
  border-radius: 3px;
  font-size: 3vw;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.wrapper .business li .business-info .business-info-promotion .business-info-promotion-left p {
  color: #666;
  font-size: 3vw;
  margin-left: 2vw;
}

.wrapper .business li .business-info .business-info-promotion .business-info-promotion-right {
  display: flex;
  align-items: center;
  font-size: 2.5vw;
  color: #999;
}

.wrapper .business li .business-info .business-info-promotion .business-info-promotion-right p {
  margin-right: 2vw;
}
</style>
