<template>
  <span class="main_asider_">
    <div>
      <el-tree :data="connectTree"
               node-key="id"
               :default-expanded-keys="connectTerrDefaultExpanded"
               @node-click="nodeClick"
               ref="connectTree"
      />
    </div>
    <div class="main_asider_item_tree_">
      <el-tree :data="tableTree"
               node-key="id"
               :default-expanded-keys="tableTerrDefaultExpanded"
               :v-loading="tableLoading"
               @node-click="tableNodeClick"
      />
    </div>
  </span>
</template>

<script>
    import $ from 'jquery';
    import Config from '@/config';
    import user from '@/user';
    import AjaxUtil from '@/utils/AjaxUtil';
    import entity from './configs';
    import handler from './handler';

    // table 查询sql
    const tableSql = entity.tableSql;

    export default {
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
        AjaxUtil.get('sql/execute', {sql: tableSql[currentDb]}).then((data) => {
          let arr = handler.produceTables(data, currentDb);
          for(let i=0; i<arr.length; i++){
            tableTreeArr.push(arr[i]);
          }

          // 修改table树高度
          setTimeout(() => {
            $('.main_asider_item_tree_').find('.el-tree-node__children').css(handler.computeTableStyle());
            that.tableLoading = false;
          }, 500);
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
          tableTree: [{
            id: 1,
            label: tables,
            children: tableTreeArr
          }],
          tableTerrDefaultExpanded: [1],
          tableLoading: true,
          currentDb: currentDb,
        }
      },
      methods: {
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
            this.currentDb = dbId.substring(0, dbId.indexOf('_'));
            let preDbId = user.get('dbId');
            if(preDbId !== dbId){
              user.set('dbId', dbId);

              // 设置 table
              let that = this;
              that.tableLoading = true;
              AjaxUtil.get('sql/execute', {sql: tableSql[this.currentDb]}).then((data) => {
                //console.log(data);
                that.tableLoading = false;
                let arr = handler.produceTables(data, that.currentDb);
                that.tableTree = [{
                  id: 1,
                  label: entity.tableNa[that.currentDb],
                  children: arr
                }];
              }, () => {
                that.tableLoading = false;
                that.$message.warning('查询所有表失败');
              });
            }

          }
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
              let d = handler.produceTables(data);
              resolve(d);
              // 修改table树高度
              setTimeout(() => {
                $(that.$refs.tableTree.$el).find('.el-tree-node__children').css(handler.computeTableStyle());
              }, 500);
            });
          }
          if(node.level === 2){
            //console.log(node);
            let option = handler.produceTableOption(node);
            resolve(option);
          }
        },
        tableNodeClick(data, node){
          //console.log(node);
          if(node.level === 3){
            let query = entity.tableQuery[this.currentDb] ? entity.tableQuery[this.currentDb][data.type] : null;
            let that = this;
            if(typeof query === 'function'){
              this.$emit('start-get-data', true);
              AjaxUtil.get('sql/execute', {sql: query(data.tableName)}).then((data) => {
                //console.log(data);
                this.$emit('start-get-data', false);
                that.$emit('get-data', data);
                if(data.status === 'success'){
                  that.$message.success('sql语句已经成功执行');
                }else if(data.status === 'error'){
                  that.$message.error('请求出错，错误原因：' + data.errorInfo);
                }else{
                  that.$message.error('请求出错');
                }
              }, (err) => {
                this.$emit('start-get-data', false);
                that.$message.error('请求出错，错误原因：' + err.message);
              });
            }
          }
        }
      },
      mounted() {
        // 判断选中哪一个连接
        let index = user.get('connectIndex');
        $(this.$refs.connectTree.$el)
          .find('.el-tree-node__children .el-tree-node__content:eq(' + index + ')').click();

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
    right: 0;
  }
  .main_asider_ .el-tree-node__background_5c{
    background-color: #f5f7fa;
  }
  .main_asider_ .el-tree-node__expand-icon.is-leaf{
    padding: 4px 0;
  }
  .main_asider_item_tree_{
    margin-top: 10px;
  }
</style>
