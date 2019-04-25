<template>
  <span>
    <div class="h10"></div>
    <el-container>
      <el-aside width="300px">
        <Sider />
      </el-aside>
      <el-container>
        <el-header :height="headerHeight + 'px'">
          <Header v-on:get-data="handlerData"
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
            headerHeight: 320,
            extractHeight: 100,
            bodyHeight: 300
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
          handlerData(data){
            this.initHeight();
            //console.log(data);
            let columns = this.convertHeader(data.headers);
            //console.log(columns);
            this.$refs.the_body.assignColumns(columns);
            this.$refs.the_body.assignTableData(data.rows);
          },
          convertHeader(arr){
            let result = [];
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
            return result;
          }
        }
    }
</script>

<style scoped>

</style>
