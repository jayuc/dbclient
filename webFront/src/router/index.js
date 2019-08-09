import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main';
import Login from '@/components/Login';
import IssuePublish from '@/components/feedback';
import UserUseing from '@/components/setting/UserUseing';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/main/',
      name: 'Main',
      component: Main
    },
    {
      path: '/feedback/',
      name: 'feedback',
      component: IssuePublish
    },
    {
      path: '/userStatistic/',
      name: 'userStatistic',
      component: UserUseing
    }
  ]
})
