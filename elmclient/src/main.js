import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'font-awesome/css/font-awesome.min.css'
import axios from 'axios'
import qs from 'qs'
import common from './common.js'

Vue.config.productionTip = false

// 设置axios的基础URL
// 自动获取当前访问的主机名（IP或域名），确保内网环境下请求不失效
const hostname = window.location.hostname;
axios.defaults.baseURL = 'http://' + hostname + ':8080/'; 
// 将axios挂载到Vue原型上
Vue.prototype.$axios = axios;
Vue.prototype.$qs = qs;

// 将common中的方法直接挂载到Vue原型上，方便组件中使用 this.$getSessionStorage 等调用
Vue.prototype.$getCurDate = common.getCurDate;
Vue.prototype.$getSessionStorage = common.getSessionStorage;
Vue.prototype.$setSessionStorage = common.setSessionStorage;
Vue.prototype.$removeSessionStorage = common.removeSessionStorage;
Vue.prototype.$getLocalStorage = common.getLocalStorage;
Vue.prototype.$setLocalStorage = common.setLocalStorage;
Vue.prototype.$removeLocalStorage = common.removeLocalStorage;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
