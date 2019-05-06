<template>
  <span class="my_main_">
    <div class="h10"></div>
    <el-container>
      <el-aside :width="asideWidth + 'px'">
        <Sider v-on:get-data="handlerData"
               v-on:start-get-data="startGetData"
               v-on:select-node-click="handleSiderSelectNode"
               ref="the_sider"
        />
      </el-aside>
      <el-container>
        <el-header :height="headerHeight + 'px'">
          <Header v-on:get-data="handlerData"
                  v-on:start-get-data="startGetData"
                  v-on:to-up="toUp"
                  v-on:to-down="toDown"
                  v-on:to-init-height="toInitHeight"
                  ref="the_header"
                  v-on:select-database="selectDatabase"
          />
        </el-header>
        <el-main>
          <Body ref="the_body"
                :bodyHeight = "bodyHeight"
          />
        </el-main>
      </el-container>
    </el-container>
  </span>
</template>

<script>
    import Sider from './main/sider';
    import Header from './main/header';
    import Body from './main/body';
    import Config from '@/config';

    export default {
        name: "Main",
        data(){
          return {
            headerHeight: 240,
            extractHeight: 138,
            bodyHeight: 300,
            asideWidth: 300
          }
        },
        components: {
          Sider,
          Header,
          Body
        },
        methods: {
          initHeight(){
            this.bodyHeight = Config.get('navigatorHeight') - this.headerHeight - this.extractHeight;
          },
          startGetData(status){
            this.$refs.the_body.setLoading(status);
          },
          handlerData(data){
            this.initHeight();
            //console.log(data);
            let columns = this.convertHeader(data.result.headers);
            //console.log(columns);
            this.$refs.the_body.assignColumns(columns);
            this.$refs.the_body.assignTableData(data.result.rows);
            this.$refs.the_body.assignTookTotal(this.handleTook(data.attributes.took), data.result.total);
          },
          convertHeader(arr){
            let result = [];
            if(!(arr instanceof Array)){
              return result;
            }
            if(arr.length > 0){
              let arrLen = arr.length - 1;
              for (let i=0; i<arrLen; i++){
                let item = arr[i];
                let temp = {};
                temp.prop = item;
                temp.label = item.toUpperCase();
                temp.width = 180;
                temp.key = i;
                result.push(temp);
              }
              let lastTemp = {};
              let lastData = arr[arrLen];
              lastTemp.prop = lastData;
              lastTemp.label = lastData.toUpperCase();
              lastTemp.key = arrLen;
              result.push(lastTemp);
            }
            return result;
          },
          handleTook(took){
            return took/1000;
          },
          toUp(){
            this.headerHeight = 94;
            this.initHeight();
          },
          toDown(){
            this.headerHeight = 500;
            this.initHeight();
          },
          toInitHeight(){
            this.headerHeight = 240;
            this.initHeight();
          },
          handleSiderSelectNode(){
            this.$refs.the_header.showDataBase();
          },
          selectDatabase(dbId){
            this.$refs.the_sider.selectConnectNode(dbId);
          }
        }
    }
</script>

<style>
  .my_main_ .el-header{
    padding: 0 10px;
  }
  .my_main_ .el-main{
    padding: 10px;
  }
</style>
