<template>
  <ul class="footer">
    <li @click="toIndex">
      <i class="fa fa-home"></i>
      <p>首页</p>
    </li>
    <li @click="toOrderList">
      <i class="fa fa-file-text-o"></i>
      <p>订单</p>
    </li>
    <li @click="toMine">
      <i class="fa fa-user-o"></i>
      <p>我的</p>
    </li>
  </ul>
</template>

<script>
export default {
  name: 'Footer',
  methods: {
    toIndex() {
      this.$router.push({ path: '/index' });
    },
    toOrderList() {
      this.$router.push({ path: '/orderList' });
    },
    toMine() {
      this.$router.push({ path: '/mine' });
    }
  }
}
</script>

<style scoped>
.footer {
  width: 100%;
  height: 14vw;
  border-top: solid 1px #DDD;
  background-color: #fff;
  position: fixed;
  left: 0;
  bottom: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.footer li {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #999;
  cursor: pointer;
}

.footer li i {
  font-size: 4.5vw;
}

.footer li p {
  font-size: 2.8vw;
  margin: 1.2vw 0 0 0;
}
</style>
