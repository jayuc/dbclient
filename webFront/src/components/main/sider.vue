<template>
  <span class="main_asider_">
    <div>
      <el-tree :data="connectTree"
               node-key="id"
               :default-expanded-keys="connectTerrDefaultExpanded"
               @node-click="nodeClick"
      />
    </div>
  </span>
</template>

<script>
    import $ from 'jquery';

    export default {
      name: "main-sider",
      data(){
        return {
          connectTree: [{
            id: 1,
            label: '连接',
            children: [
              {id: 11, label: '第一个连接'},
              {id: 12, label: '第二个连接'}
            ]
          }],
          connectTerrDefaultExpanded: [1]
        }
      },
      methods: {
        nodeClick(data, node, node_div){
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

        }
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
</style>
