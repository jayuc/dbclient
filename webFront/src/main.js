// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './main.css';
import User from '@/user';

//使用 饿了么 ui
Vue.use(ElementUI);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  created() {
    let connected = User.get('connected');
    // 如果未创建连接，则跳转到连接页面，刷新时也跳转到连接页面
    if(connected !== 'yes'){
      this.$router.push("/");
    }
  }
});
