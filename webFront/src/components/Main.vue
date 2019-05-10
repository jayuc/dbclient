<template>
  <span class="my_main_">
    <div :style="{height: headerTopHeight + 'px'}"></div>
    <el-container>
      <el-aside :width="asideWidth + 'px'">
        <Sider v-on:get-data="handlerData"
               v-on:start-get-data="startGetData"
               v-on:select-node-click="handleSiderSelectNode"
               ref="the_sider"
               v-on:query-table-info="queryTableInfo"
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
                  v-on:setting="doSetting"
          />
        </el-header>
        <el-main>
          <Body ref="the_body"
                :bodyHeight = "tbodyHeight"
                v-on:get-json-str="showJsonDialog"
          />
        </el-main>
      </el-container>
    </el-container>
    <Dialog ref="the_dialog"
    />
    <JsonDialog ref="the_json_dialog"
    />
  </span>
</template>

<script>
    import Sider from './main/sider';
    import Header from './main/header';
    import Body from './main/body';
    import Config from '@/config';
    import Dialog from './setting/Dialog';
    import bodyHandler from './main/bodyHandler';
    import JsonDialog from './main/json-show-dialog';
    import $ from 'jquery';

    export default {
        name: "Main",
        data(){
          return {
            headerHeight: 240,
            headerTopHeight: 10,
            extractHeight: 20 + 24,  // 20表示下面el-main的padding;24是table body 上面文字高度
            bodyHeight: 300,
            asideWidth: 300
          }
        },
        components: {
          Sider,
          Header,
          Body,
          Dialog,
          JsonDialog
        },
        computed: {
          tbodyHeight(){
            return this.computeBodyHeight();
          }
        },
        methods: {
          initHeight(){
            this.bodyHeight = this.computeBodyHeight();
          },
          computeBodyHeight(){
            // 全局顶部header的高度
            let global_header_height = $('.__app-header__').height();
            //console.log(global_header_height);
            return Config.get('navigatorHeight') - this.headerHeight - this.extractHeight
                          - this.headerTopHeight - global_header_height;
          },
          startGetData(status){
            this.$refs.the_body.setLoading(status);
          },
          handlerData(data){
            this.initHeight();
            //console.log(data);
            let columns = bodyHandler.handleHeaders(data.result.headers);
            //console.log(columns);
            this.$refs.the_body.assignColumns(columns.result);
            this.$refs.the_body.assignTableData(bodyHandler.handleRows(data.result.rows));
            //console.log(columns.headerInfo);
            this.$refs.the_body
              .assignTookTotal(this.handleTook(data.attributes.took), data.result.total, columns.headerInfo);
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
          },
          queryTableInfo(info){
            this.$refs.the_body.setTableInfo(info);
          },
          doSetting(){
            this.$refs.the_dialog.open();
          },
          showJsonDialog(text){
            this.$refs.the_json_dialog.open(text);
          }
        },
        mounted() {
          let bodyWidth = Config.get('bodyWidth');
          // 设置table实际所占宽度, 其中 10 是el-main的padding
          Config.set('tableWidth', bodyWidth - this.asideWidth - 2*10);
        }
    }
</script>

<style>
  .my_main_ .el-header{
    padding: 0 10px;
  }
  .my_main_ .el-main{
    padding: 10px 10px 0 10px;
  }
</style>
