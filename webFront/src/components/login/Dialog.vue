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
    >
      <el-form-item label="Ip地址：" prop="host">
        <el-input v-model="formData.host"></el-input>
      </el-form-item>
      <el-form-item label="端口：" prop="port">
        <el-input v-model.number="formData.port"></el-input>
      </el-form-item>
      <el-form-item label="数据库名：" prop="name" v-if="$attrs.dbData.showName">
        <el-input v-model="formData.name"></el-input>
      </el-form-item>
      <el-form-item label="用户名：" prop="userName" v-if="$attrs.dbData.showUserName">
        <el-input v-model="formData.userName"></el-input>
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
          rules: {
            host: [{required: true, message: '请输入Ip地址', trigger: 'blur'}],
            port: [{required: true, message: '请输入端口号', trigger: 'blur'},
              {type: 'number', message: '必须为数字', trigger: 'change'},
              {validator(rule, value, callback) {
                  if(value > 65535){
                    callback(new Error('端口号超过最大值'));
                  }
                  callback();
                }, trigger: 'change'}],
            name: [{required: true, message: '请输入数据库名', trigger: 'blur'}],
            userName: [{required: true, message: '请输入用户名', trigger: 'blur'}],
            password: [{required: true, message: '请输入数据库密码', trigger: 'blur'}],
          }
        }
      },
      computed: {
        computeTitle(){
          return this.$attrs.dbData.type + ' 连接：';
        }
      },
      methods: {
        cancel(){
          this.close();
        },
        connect(){
          let that = this;
          let type = this.$attrs.dbData.type;
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
                  let _id = handlers.getDatabase[type](dbId).exclusionDbNameStr();
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
                that.close();
                //跳转到主页面
                that.$router.push("/main");
              });
            }
          });
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
          this.formData.port = data.port;
          this.formData.name = data.name;
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
        }
      }
    }
</script>

<style>
  .login_dialog_ .el-dialog__footer{
    padding-top: 0px;
  }
</style>
