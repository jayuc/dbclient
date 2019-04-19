<template>
  <span>
    <div class="header"></div>
    <div class="h30"></div>
    <div class="row_">
      <div class="col_">
        <el-button type="primary" v-on:click="openDialog('Mysql')">连接mysql</el-button>
      </div>
      <div class="col_">
        <el-button type="success" v-on:click="openDialog('Oracle')">连接oracle</el-button>
      </div>
      <div class="col_">
        <el-button type="info" v-on:click="openDialog('Redis')">连接redis</el-button>
      </div>
      <div class="col_" style="margin-left: 18px">
        <el-button type="warning" v-on:click="">连接postgresql</el-button>
      </div>
      <div class="col_" style="margin-left: 36px">
        <el-button type="danger" v-on:click="">连接mongodb</el-button>
      </div>
    </div>
    <Dialog ref="dialog"
            :dbData="dbData"
    />
  </span>
</template>

<script>
    import Dialog from './login/Dialog';

    // 数据库默认值
    const dbValues = {
      'Oracle': {
        port: 1522,
        showName: true,
        showUserName: true
      },
      'Mysql': {
        port: 3306,
        showName: true,
        showUserName: true
      },
      'Redis': {
        port: 6379,
        showName: false,
        showUserName: false
      }
    };

    export default {
      name: "Login",
      data(){
        return {
          dbData: {
            type: 'Oracle',
            showName: true,
            showUserName: true
          }
        }
      },
      methods: {
        openDialog(dbType){
          let data = dbValues[dbType];
          this.dbData.type = dbType;
          this.dbData.showName = data.showName;
          this.dbData.showUserName = data.showUserName;
          this.$refs.dialog.open(data);
        }
      },
      components: {
        Dialog
      }
    }
</script>

<style scoped>
 .header{
   height: 20px;
   background: black;
 }
  .el-col{
    text-align: center;
  }
  .row_{
    overflow: hidden;
  }
  .col_{
    width: 160px;
    float: left;
    text-align: center;
  }
</style>
