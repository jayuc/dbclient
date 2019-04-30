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
      <el-button @click="clearSql" size="mini">清空</el-button>
      <el-button type="primary" @click="execute" size="mini">执行</el-button>
    </div>
  </span>
</template>

<script>
    import User from '@/user';
    import AjaxUtil from '@/utils/AjaxUtil';
    import ResultUtil from '@/utils/ResultUtil';

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
          if(this.sql.length === 0){
            this.$message.error('sql不能为空');
            return ;
          }
          // 用户是否已经连接
          let connected = User.get('connected');
          if(connected !== 'yes'){
            this.$message.error('未成功连接');
            this.$router.push("/");
            return;
          }
          this.$emit('start-get-data', true);
          let that = this;
          AjaxUtil.get('sql/execute', this.getParam()).then((data) => {
            console.log(data);
            that.$emit('start-get-data', false);
            that.$emit('get-data', data);
            ResultUtil.handle(data, null, that);
          }, (err) => {
            that.$emit('start-get-data', false);
            that.$message.error('请求出错，错误原因：' + err.message);
          });
        },
        getParam(){
          let data = {};
          data.sql = this.sql;
          return data;
        }
      }
    }
</script>

<style>
  .main_header_sql_tip_{
    color: blue;
    margin-bottom: 7px;
    font-size: 14px;
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
    height: 180px;
    font-size: 14px;
    font-family: Arial;
  }
</style>
