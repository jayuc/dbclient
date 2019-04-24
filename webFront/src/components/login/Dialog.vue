<template>
  <el-dialog :title="$attrs.dbData.type + ' 连接：'"
             :visible.sync="visible"
             width="450px"
             class="login_dialog_"
  >
    <el-form :model="formData"
             label-width="115px"
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
        <el-input v-model="formData.password" :show-password="true"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="connect">连 接</el-button>
    </span>
  </el-dialog>
</template>

<script>
    import AjaxUtil from '@/utils/AjaxUtil';
    import User from '@/user';
    import CookieUtil from '@/utils/CookieUtil';
    import InnerConfig from '@/config/innerConfig';

    export default {
      name: "Dialog",
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
      methods: {
        cancel(){
          this.close();
        },
        connect(){
          let that = this;
          let type = this.$attrs.dbData.type;
          this.$refs.myForm.validate((valid) => {
            if(valid){   //验证成功
              // 正在加载
              const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.5)'
              });
              let param = this.filterFormData();
              param.type = type;
              AjaxUtil.post('newcon/create', param).then((data) => {
                loading.close();  //关闭正在加载
                console.log(data);
                if(data.status === 'success'){  //请求成功
                  that.$message.success('连接成功');
                  // 数据库编号
                  let dbId = data.attributes.dbId;
                  if(dbId){
                    User.set('dbId', dbId);
                    // 标识用户已经创建连接
                    User.set('connected', 'yes');
                  }
                  // 用户标识
                  let token = data.attributes.token;
                  if(token){
                    CookieUtil.set(InnerConfig.cookieName, token, 1000);
                  }
                  that.close();
                  //跳转到主页面
                  that.$router.push("/main");
                }else if(data.status === 'error'){ // 请求失败
                  that.$message.error('连接出错，错误原因：' + data.errorInfo);
                }else{
                  that.$message.error('连接出错');
                }
              }, (err) => {
                loading.close();  //关闭正在加载
                console.log(err);
                that.$message.error('连接出错，错误原因：' + err.message);
              });
            }
          });
        },
        //过滤表单
        filterFormData(){
          let data = {};
          data.port = this.formData.port;
          data.password = this.formData.password;
          data.host = this.formData.host.trim();
          data.name = this.formData.name.trim();
          data.userName = this.formData.userName.trim();
          return data;
        },
        close(){
          this.resetFrom();
          this.visible = false;
        },
        open(data){
          this.visible = true;
          this.formData.port = data.port;
        },
        resetFrom(){  //重置表单
          this.$refs.myForm.resetFields();
        }
      }
    }
</script>

<style>
  .login_dialog_ .el-dialog__footer{
    padding-top: 0px;
  }
</style>
