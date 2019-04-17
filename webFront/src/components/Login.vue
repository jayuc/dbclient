<template>
    <el-button type="primary" v-on:click="doClick">点击</el-button>
</template>

<script>
    import AjaxUtil from '@/utils/AjaxUtil';
    import User from '@/user';
    import CookieUtil from '@/utils/CookieUtil';
    import InnerConfig from '@/config/innerConfig';

    export default {
      name: "Login",
      methods: {
        doClick: function (e) {
          //console.log("click");
          //console.log(e.target);
          //console.log(AjaxUtil.ajax);
          let param = {type: 'Oracle', host: '192.168.10.228', port: 1522, name: 'orclc',
            userName: 'itms3', password: 'itms3'};
          AjaxUtil.ajax({
            url: 'http://127.0.0.1:8004/newcon/create',
            type: 'post',
            data: param,
            success: function (data) {
              console.log('data:');
              console.log(data);
              let dbId = data.attributes.dbId;
              if(dbId){
                User.set('dbId', dbId);
              }
              let token = data.attributes.token;
              if(token){
                CookieUtil.set(InnerConfig.cookieName, token, 1000);
              }
            },
            error: function (data) {
              console.log('error info: ');
              console.log(data);
            }
          });
        }
      }
    }
</script>

<style scoped>

</style>
