<template>
  <span>
    <div class="main_header_sql_tip_">
      请输入语句:
      <a href="#/">
        <i class="el-icon-back main_header_sql_back_" title="返回创建连接页面"></i>
      </a>
    </div>
    <el-input type="textarea"
              class="main_header_"
              v-model="sql"
    />
    <div class="main_header_sql_btn_">
      <el-button @click="clearSql">清空</el-button>
      <el-button type="primary" @click="execute">执行</el-button>
    </div>
  </span>
</template>

<script>
    import User from '@/user';
    import CookieUtil from '@/utils/CookieUtil';
    import InnerConfig from '@/config/innerConfig';
    import AjaxUtil from '@/utils/AjaxUtil';

    export default {
      name: "main-header",
      data(){
        return {
          sql: ''
        };
      },
      methods: {
        clearSql(){
          this.sql = '';
        },
        execute(){
          // 用户是否已经连接
          let connected = User.get('connected');
          if(connected !== 'yes'){
            this.$message.error('未成功连接');
            this.$router.push("/");
            return;
          }
          let that = this;
          AjaxUtil.get('sql/execute', this.getParam()).then((data) => {
            //console.log(data);
            if(data.status === 'success'){
              that.$emit('get-data', data.result);
            }else if(data.status === 'error'){
              that.$message.error('请求出错，错误原因：' + data.errorInfo);
            }else{
              that.$message.error('请求出错');
            }
          }, (err) => {
            that.$message.error('请求出错，错误原因：' + err.message);
          });
        },
        getParam(){
          let data = {};
          data.sql = this.sql;
          data.dbId = User.get('dbId');
          data.token = CookieUtil.get(InnerConfig.cookieName);
          return data;
        }
      }
    }
</script>

<style>
  .main_header_sql_tip_{
    color: blue;
    margin-bottom: 7px;
    font-family: '微软雅黑';
  }
  .main_header_sql_btn_{
    margin-top: 7px;
  }
  .main_header_sql_back_{
    float: right;
    cursor: pointer;
    margin: 5px 2px 0px 0px;
  }
  .main_header_ .el-textarea__inner{
    height: 240px;
    font-size: 22px;
    font-family: Arial;
  }
</style>
