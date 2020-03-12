<template>
  <span class="main_asider_">
    <div class="main_connect_item_tree_">
      <el-tree :data="connectTree"
               node-key="id"
               :default-expanded-keys="connectTerrDefaultExpanded"
               @node-click="nodeClick"
               ref="connectTree"
      />
    </div>
    <div class="main_asider_item_tree_">
      <div class="search_box">
        <el-input
            v-model="searchText"
            placeholder="搜索"
            size="mini"
            @change="searchTable"
        >
          <el-button slot="append"
                     icon="el-icon-search"
                     style="width: 20px;margin: -10px -27px;"
                     @click="searchTable"
                     title="回车键搜索"
          ></el-button>
        </el-input>
      </div>
      <div class="search_refresh">
        <el-button icon="el-icon-refresh"
                   size="mini"
                   plain
                   type="success"
                   style="padding: 5px 7px;"
                   title="刷新"
                   @click="loadTableTree"
                   :disabled="tableLoading"
        ></el-button>
      </div>
      <el-tree :data="tableTree"
               node-key="id"
               :default-expanded-keys="tableTerrDefaultExpanded"
               v-loading="tableLoading"
               @node-click="tableNodeClick"
               @node-expand="expendTableNode"
      />
    </div>
  </span>
</template>

<script>
    import $ from 'jquery';
    import Config from '@/config';
    import user from '@/user';
    import AjaxUtil from '@/utils/AjaxUtil';
    import StringUtil from '@/utils/StringUtil';
    import entity from './configs';
    import handler from './handler';
    import ResultUtil from '@/utils/ResultUtil';
    import loginHandler from '../login/handler';
    import ElButton from "../../../node_modules/element-ui/packages/button/src/button.vue";

    // table 查询sql
    const tableSql = entity.tableSql;

    export default {
      components: {ElButton},
      name: "main-sider",
      data(){
        // 连接
        let connects = Config.get('connects');
        if(!(connects instanceof Array)){
          connects = [];
        }
        // db id
        let dbId = user.get('dbId');
        let currentDb = dbId.substring(0, dbId.indexOf('_'));
        let tables = entity.tableNa[currentDb];
        let tableTreeArr = [];

        let that = this;
        AjaxUtil.get(entity.tableUrl[currentDb], {sql: tableSql[currentDb]}).then((data) => {
          ResultUtil.handle(data, () => {
            let arr = handler.produceTables(data, currentDb);
            for(let i=0; i<arr.length; i++){
              tableTreeArr.push(arr[i]);
            }

            // 修改table树高度
            setTimeout(() => {
              //console.log(handler.computeTableStyle());
              $('.main_asider_item_tree_').find('.el-tree-node__children').css(handler.computeTableStyle());
              that.tableLoading = false;
            }, 500);
          }, that);
        }, () => {
          that.tableLoading = false;
          that.$message.warning('查询所有表失败');
        });
        return {
          connectTree: [{
            id: 1,
            label: '连接',
            children: connects
          }],
          connectTerrDefaultExpanded: [1],
          tableTreeName: tables,
          tableTree: [{
            id: 1,
            label: tables,
            children: tableTreeArr
          }],
          allTreeList: tableTreeArr,
          tableTerrDefaultExpanded: [1],
          tableLoading: true,
          currentDb: currentDb,
          searchText: '',
          oldSerachText: ''
        }
      },
      methods: {
        searchTable(){
          if(this.oldSerachText === this.searchText){
            return;
          }
          let searchText = this.searchText;
          // console.log(searchText);
          if(StringUtil.isBlank(searchText)){
            // console.log(this.allTreeList);
            this.oldSerachText = searchText;
            this.addTableTreeArr(this.allTreeList);
            return;
          }
          let list = this.allTreeList;
          let arr = [];
          for(let i=0; i<list.length; i++){
            let item = list[i];
            if((item.label.toLowerCase()).indexOf(searchText.toLowerCase()) > -1){
              arr.push(item);
            }
          }
          // console.log(arr);
          this.oldSerachText = searchText;
          this.addTableTreeArr(arr);
        },
        addTableTreeArr(arr){
          this.tableTree = [{
            id: 1,
            label: this.tableTreeName,
            children: arr
          }];
        },
        nodeClick(data, node, node_div){
          if(node.level === 2){
            let nodeDiv = node_div.$refs.node;
            $(nodeDiv).siblings().find('.my_selected').remove();
            $(nodeDiv).siblings().find('.el-tree-node__content')
              .removeClass('el-tree-node__background_5c');
            let my_selected = $(nodeDiv).find('.my_selected');
            if(my_selected.length === 0){
              $(nodeDiv).children('.el-tree-node__content')
                .addClass('el-tree-node__background_5c')
                .append($('<div class="my_selected"><i class="el-icon-check"></i></div>'));
            }

            // 设置 db id
            let dbId = data.id;
            this.currentDb = handler.getDbTypeFromDbId(dbId);
            let preDbId = user.get('dbId');
            if(preDbId !== dbId){
              user.set('dbId', dbId);

              // 设置当前选中
              let _id = entity.getDatabase[this.currentDb](dbId).exclusionDbNameStr();
              user.set('connectIndex', Config.get('connectPos')[_id]);

              // console.log(this.currentDb);
              Config.set('currentDbType', this.currentDb);

              this.loadTableTree();

              // 触发选中事件
              this.$emit('select-node-click');
            }

          }
        },
        loadTableTree(){
          // 设置 table
          let that = this;
          that.tableLoading = true;
          AjaxUtil.get(entity.tableUrl[this.currentDb], {sql: tableSql[this.currentDb]}).then((data) => {
            //console.log(data);
            that.tableLoading = false;
            ResultUtil.handle(data, () => {
              let arr = handler.produceTables(data, that.currentDb);
              that.allTreeList = arr;
              that.tableTreeName = entity.tableNa[that.currentDb];
              that.searchText = '';
              that.oldSerachText = '';
              that.tableTree = [{
                id: 1,
                label: entity.tableNa[that.currentDb],
                children: arr
              }];
            }, that);
          }, () => {
            that.tableLoading = false;
            that.$message.warning('查询所有表失败');
          });
        },
        // 加载数据库中包含的所有表
        loadTables(node, resolve){
          if (node.level === 0) {
            return resolve([{ name: this.tables }]);
          }
          if (node.level === 1){
            let that = this;
            AjaxUtil.get('sql/execute', {sql: tableSql[this.currentDb]}).then((data) => {
              //console.log(data);
              ResultUtil.handle(data, () => {
                let d = handler.produceTables(data);
                resolve(d);
                // 修改table树高度
                setTimeout(() => {
                  $(that.$refs.tableTree.$el).find('.el-tree-node__children').css(handler.computeTableStyle());
                }, 500);
              }, that);
            });
          }
          if(node.level === 2){
            //console.log(node);
            let option = handler.produceTableOption(node);
            resolve(option);
          }
        },
        tableNodeClick(data, node){
          //console.log(data);
          //console.log(node.level);
          let level = node.level;
          let query;
          let tableInfo;
          if(entity.tableQuery[this.currentDb]){
            query = entity.tableQuery[this.currentDb][data.type];
            tableInfo = entity.tableQuery[this.currentDb]['tableInfo'];
          }
          let that = this;
          if(typeof query === 'function'){
            this.$emit('start-get-data', true);
            AjaxUtil.get('sql/execute', {sql: query(data.tableName)}).then((data) => {
              //console.log(data);
              that.$emit('start-get-data', false);
              that.$emit('get-data', data);
              ResultUtil.handle(data, null, that);
            }, (err) => {
              this.$emit('start-get-data', false);
              that.$message.error('请求出错，错误原因：' + err.message);
            });
          }
          // 查询表信息
          if(data.type){
            if (data.type === 'tableStructure' && typeof tableInfo === 'function'){
              AjaxUtil.get('sql/execute', {sql: tableInfo(data.tableName)}).then((data) => {
                //console.log(data);
                let result = data.result;
                if(result.headers instanceof Array
                  && result.headers.length > 0
                  && result.rows.length > 0){
                  that.$emit('query-table-info', result.rows[0][result.headers[0]]);
                }
              });
            }else{
              that.$emit('query-table-info', '');
            }
          }
        },
        selectConnectNode(dbId){
          //console.log(dbId);
          let arr = this.connectTree[0].children;
          let treeData = [{
            id: 1,
            label: this.connectTree[0].label,
            children: arr
          }];
          let _id = entity.getDatabase[this.currentDb](dbId).exclusionDbNameStr();
          let index = Config.get('connectPos')[_id];
          let temp = {
            id: dbId,
            label: loginHandler.dbIdHandlers[entity.dbTypes[this.currentDb]](dbId)
          };
          arr.splice(index, 1, temp);
          //console.log(treeData);
          this.connectTree = treeData;

          // 选择连接
          let that = this;
          setTimeout(function () {
            that.clickConnectNode();
          }, 500);

          // 加载table表
          this.loadTableTree();
        },
        clickConnectNode(){
          // 判断选中哪一个连接
          let index = user.get('connectIndex');
          let node = $(this.$refs.connectTree.$el)
            .find('.el-tree-node__children .el-tree-node__content:eq(' + index + ')');
          node.click();
          let list = $(this.$refs.connectTree.$el)
            .find('.el-tree-node__children .el-tree-node__content .el-tree-node__label');
          //console.log(list);
          for(let i=0; i<list.length; i++){
            let title = $(list[i]).text();
            $(list[i]).attr('title', title);
          }
        },
        expendTableNode(data, node, component){
          if(node.level === 1){
            let group = $(component.$el).find('.el-tree-node__children');
            //console.log(group);
            setTimeout(function () {
              $(group[0]).css(handler.computeTableStyle());
            }, 500);
          }
        }
      },
      mounted() {
        this.clickConnectNode();
      }
    }
</script>

<style>
  .main_asider_ .el-tree-node__label{
    color: blue;
  }
  .main_asider_ .el-tree-node__children .el-tree-node__label{
    font-size: 12px;
    color: #606266;
  }
  .main_asider_ .el-tree-node__content{
    height: 20px;
    position: relative;
  }
  .main_asider_ .el-tree-node__content .my_selected{
    width: 20px;
    height: 20px;
    color: #409EFF;
    font-size: 18px;
    position: absolute;
    top: 0;
    right: -3px;
  }
  .main_asider_ .main_connect_item_tree_ .el-tree-node__label{
    width: 248px;
    overflow: hidden;
  }
  .main_asider_ .el-tree-node__background_5c{
    background-color: #f5f7fa;
  }
  .main_asider_ .el-tree-node__expand-icon.is-leaf{
    padding: 4px 0;
  }
  .main_asider_item_tree_{
    margin-top: 5px;
    position: relative;
  }
  .main_asider_item_tree_ .search_box{
    position: absolute;
    top: -2px;
    left: 124px;
    width: 140px;
    z-index: 2;
  }
  .main_asider_item_tree_ .search_box .el-input--mini .el-input__inner{
    height: 24px;
    padding: 0px 5px;
    line-height: 24px;
    border: 1px solid #67C23A;
  }
  .main_asider_item_tree_ .search_box .el-input-group__append{
    padding: 0 13px;
  }
  .main_asider_item_tree_ .search_refresh{
    position: absolute;
    top: -2px;
    left: 270px;
    width: 10px;
    z-index: 2;
  }
</style>
