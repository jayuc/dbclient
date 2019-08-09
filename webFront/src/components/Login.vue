<template>
  <span>
    <div class="h30"></div>
    <div class="row_">
      <div class="col_">
        <el-button type="primary" v-on:click="openDialog('Mysql')">连接 Mysql</el-button>
      </div>
      <div class="col_">
        <el-button type="success" v-on:click="openDialog('Oracle')">连接 Oracle</el-button>
      </div>
      <div class="col_">
        <el-button type="info" v-on:click="openDialog('Redis')">连接 Redis</el-button>
      </div>
      <div class="col_" style="margin-left: 18px">
        <el-button type="warning" v-on:click="openDialog('Postgresql')">连接 Postgresql</el-button>
      </div>
      <div class="col_" style="margin-left: 36px">
        <el-button type="danger" v-on:click="openDialog('Mongodb')">连接 Mongodb</el-button>
      </div>
      <div class="col_">
        <el-button type="primary"
                   icon="el-icon-d-arrow-right"
                   circle
                   title="前进"
                   @click="gotoMain"
        />
      </div>
      <div class="col_" style="text-align: left;width: 40px;">
        <el-button type="primary"
                   icon="el-icon-edit"
                   circle
                   title="问题反馈"
                   @click="gotoFeedback"
        />
      </div>
      <div class="col_">
        <a href="#/userStatistic">
          <el-button type="primary"
                     icon="el-icon-tickets"
                     circle
                     title="用户统计"
          />
        </a>
      </div>
    </div>
    <Dialog ref="dialog"
            :dbData="dbData"
    />
  </span>
</template>

<script>
    import Dialog from './login/Dialog';
    import user from '@/user';
    import entity from './login/entity';

    // 数据库默认值
    const dbValues = entity.dbValues;

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
        },
        gotoMain(){
          let connected = user.get('connected');
          if(connected === 'yes'){
            //跳转到主页面
            this.$router.push("/main");
          }else{
            this.$message.warning('还未连接，请先连接');
          }
        },
        gotoFeedback(){
          this.$router.push("/feedback");
        }
      },
      components: {
        Dialog
      }
    }
</script>

<style scoped>
  .row_{
    overflow: hidden;
  }
  .col_{
    width: 160px;
    float: left;
    text-align: center;
  }
</style>
