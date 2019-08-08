<template>
  <el-dialog :title="computeTitle"
             :visible.sync="visible"
             width="400px"
             class="login_dialog_"
  >
    <el-form :model="formData"
             label-width="105px"
             :rules="rules"
             ref="myForm"
             @keyup.enter.native="connect"
    >
      <el-form-item label="Ip地址：" prop="host">
        <!--
        <el-input v-model="formData.host"></el-input>
        -->
        <el-autocomplete class="inline-input"
                         style="width: 255px"
                         v-model="formData.host"
                         :fetch-suggestions="queryTipByHost"
                         @select="selectTip"
        />
      </el-form-item>
      <el-form-item label="端口：" prop="port">

        <el-input v-model.number="formData.port"></el-input>
        <!--
        <el-autocomplete class="inline-input"
                         style="width: 255px"
                         v-model.number="formData.port"
                         :fetch-suggestions="queryTipByPort"
                         @select="selectTip"
        />
        -->
      </el-form-item>
      <el-form-item label="数据库名：" prop="name" v-if="$attrs.dbData.showName">
        <!--
        <el-input v-model="formData.name"></el-input>
        -->
        <el-autocomplete class="inline-input"
                         style="width: 255px"
                         v-model="formData.name"
                         :fetch-suggestions="queryTipByName"
                         @select="selectTip"
        />
      </el-form-item>
      <el-form-item label="用户名：" prop="userName" v-if="$attrs.dbData.showUserName">
        <!--
        <el-input v-model="formData.userName"></el-input>
        -->
        <el-autocomplete class="inline-input"
                         style="width: 255px"
                         v-model="formData.userName"
                         :fetch-suggestions="queryTipByUsername"
                         @select="selectTip"
        />
      </el-form-item>
      <el-form-item label="用户密码：" prop="password">
        <el-input v-model="formData.password"
                  :show-password="true"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="connect">连 接</el-button>
    </span>
  </el-dialog>
</template>

<script>
    import User from '@/user';
    import Config from '@/config';
    import handlers from './handler';
    import CookieUtil from '@/utils/CookieUtil';
    import InnerConfig from '@/config/innerConfig';
    import entity from './entity';
    import LoginData from '@/model/LoginData';
    import LoginDb from '@/model/LoginDb';
    import entryCacheCookie from '@/model/LoginCacheCookie';

    export default {
      name: "login-dialog",
      data(){
        return {
          visible: false,
          formData: {
            host: undefined,
            port: undefined,
            name: undefined,
            userName: undefined,
            password: undefined,
          },
          rules: {}
        }
      },
      computed: {
        computeTitle(){
          return this.$attrs.dbData.type + ' 连接：';
        }
      },
      methods: {
        queryTipByHost(str, callback){
          this.queryTip(str, callback, 'host');
        },
        queryTipByPort(str, callback){
          this.queryTip(str, callback, 'port');
        },
        queryTipByName(str, callback){
          this.queryTip(str, callback, 'name');
        },
        queryTipByUsername(str, callback){
          this.queryTip(str, callback, 'userName');
        },
        queryTip(str, callback, type){
          // console.log(str);
          let arr = [];
          let cache = entryCacheCookie.getLoginDataCache();
          let dbType = this.currentDbType();
          if(cache[dbType]){
            let d = this.formData;
            arr = cache[dbType].getColumn(type, new LoginData(d.host, d.port, d.name, d.userName, d.password), str);
          }
          // console.log(arr);
          callback(arr);
        },
        selectTip(item){
          // console.log(item);
          let cache = entryCacheCookie.getLoginDataCache();
          let dbType = this.currentDbType();
          if(cache[dbType]){
            let d = this.formData;
            let column = cache[dbType].getEmptyColumn(new LoginData(d.host, d.port, d.name, d.userName, d.password));
            // console.log(column);
            for(let index in this.formData){
              if(column[index] instanceof Array && column[index].length === 1
                    && column[index][0] != this.formData[index] && column[index][0]){
                this.formData[index] = column[index][0];
              }
            }
          }
        },
        cancel(){
          this.close();
        },
        currentDbType(){
          return this.$attrs.dbData.type;
        },
        connect(){
          //console.log('-------- start connect...');
          let that = this;
          let type = this.currentDbType();
          this.$refs.myForm.validate((valid) => {
            if(valid){   //验证成功
              let param = that.filterFormData();
              param.type = type;
              let preDbId = handlers.getDbId(param);
              let dbId = preDbId.substring(0, preDbId.length - 1);
              //console.log(dbId);
              let connectObj = Config.get('connectObj');
              let connectPos = Config.get('connectPos');
              if(typeof connectObj === 'object' && connectObj[dbId]){
                User.set('connectIndex', connectPos[dbId]);
                //跳转到主页面
                that.$router.push("/main");
                return ;
              }
              //console.log('-------执行');
              handlers.connect(that, param, (data, dbId) => {
                // 数据库编号
                if(dbId){
                  // 标识用户已经创建连接
                  User.set('connected', 'yes');
                  // password
                  let password = User.get('password');
                  let _id = handlers.getDatabase[type](dbId, that.havePassword()).exclusionDbNameStr();
                  if(typeof password === 'object'){
                    password[_id] = param.password;
                  }else{
                    password = {};
                    password[_id] = param.password;
                    User.set('password', password);
                  }
                  // 生产连接
                  that.produceConnects(dbId, type, _id);
                }
                // 用户标识
                let token = data.attributes.token;
                if(token){
                  CookieUtil.set(InnerConfig.cookieName, token, 1000);
                }
                // 连接成功后执行
                that.afterConnected(param);
                that.close();
                //跳转到主页面
                that.$router.push("/main");
              });
            }
          });
        },
        // 连接成功或执行
        afterConnected(param){
          //console.log(param);

          // 缓存数据
          let cache = entryCacheCookie.getLoginDataCache();
          let type = this.currentDbType();
          let loginData = new LoginData(param.host, param.port, param.name, param.userName, param.password);
          if(cache[type]){
            cache[type].addLoginData(loginData);
          }else{
            cache[type] = new LoginDb(loginData);
          }
          // console.log(cache);
          let cacheCookie = new entryCacheCookie.LoginCacheCookie(cache[type]);
          // console.log(cacheCookie);
          cacheCookie.cookie(type, loginData);
        },
        //过滤表单
        filterFormData(){
          let data = {};
          data.port = this.formData.port;
          data.password = this.formData.password;
          data.host = this.formData.host ? this.formData.host.trim() : this.formData.host;
          data.name = this.formData.name ? this.formData.name.trim() : this.formData.name;
          data.userName = this.formData.userName ? this.formData.userName.trim() : this.formData.userName;
          return data;
        },
        close(){
          this.resetFrom();
          this.visible = false;
        },
        open(data){
          this.visible = true;
          // 初始化表单数据
          this.formData.port = data.port;
          this.formData.name = data.name;
          this.formData.host = undefined;
          this.formData.userName = undefined;
          this.formData.password = undefined;
          this.rules = entity.getRules(data);
        },
        resetFrom(){  //重置表单
          this.$refs.myForm.resetFields();
        },
        // 处理连接
        produceConnects(dbId, type, _id){
          let connectArr = Config.get('connects');
          let connectObj = Config.get('connectObj');
          let connectPos = Config.get('connectPos');
          if(!(connectArr instanceof Array)){
            connectArr = [];
            connectObj = {};
            connectPos = {};
            Config.set('connects', connectArr);
            Config.set('connectObj', connectObj);
            Config.set('connectPos', connectPos);
          }
          if(!connectObj[dbId]){
            let con = {
              id: dbId,
              label: this.handleDbId(dbId, type)
            };
            connectPos[_id] = connectArr.length;
            connectArr.push(con);
            connectObj[dbId] = dbId;
          }
          // 设置当前连接
          User.set('connectIndex', connectPos[_id]);
        },
        // 处理dbId
        handleDbId(dbId, type){
          return handlers.dbIdHandlers[type](dbId);
        },
        // 是否有密码
        havePassword(){
          if(this.formData.password){
            return true;
          }
          return false;
        }
      }
    }
</script>

<style>
  .login_dialog_ .el-dialog__footer{
    padding-top: 0px;
  }
</style>
