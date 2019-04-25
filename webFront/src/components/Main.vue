<template>
  <span>
    <div class="h10"></div>
    <el-container>
      <el-aside width="300px">
        <Sider />
      </el-aside>
      <el-container>
        <el-header height="320px">
          <Header v-on:get-data="handlerData"
          />
        </el-header>
        <el-main>
          <Body ref="the_body" />
        </el-main>
      </el-container>
    </el-container>
  </span>
</template>

<script>
    import Sider from './main/sider';
    import Header from './main/header';
    import Body from './main/body';

    export default {
        name: "Main",
        components: {
          Sider,
          Header,
          Body
        },
        methods: {
          handlerData(data){
            //console.log(data);
            let columns = this.convertHeader(data.headers);
            //console.log(columns);
            this.$refs.the_body.assignColumns(columns);
            this.$refs.the_body.assignTableData(data.rows);
          },
          convertHeader(arr){
            let result = [];
            for (let i=0; i<arr.length; i++){
              let item = arr[i];
              let temp = {};
              temp.prop = item;
              temp.label = item.toUpperCase();
              temp.width = 180;
              temp.key = i;
              result.push(temp);
            }
            return result;
          }
        }
    }
</script>

<style scoped>

</style>
