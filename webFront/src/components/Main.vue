<template>
  <span>
    <div class="h10"></div>
    <el-container>
      <el-aside width="300px">
        <Sider />
      </el-aside>
      <el-container>
        <el-header>
          <Header v-on:get-data="handlerData"
          />
        </el-header>
        <el-main>
          <Body />
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
            console.log(data);
          }
        }
    }
</script>

<style scoped>

</style>
