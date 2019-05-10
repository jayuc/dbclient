<template>
    <el-dialog :visible.sync="visible"
               title="格式化数据："
               width="600px"
               custom-class="my_json_tree__i_"
               @opened="after_open"
               top="10vh"
    >
      <div id="my_json_tree__i_div_"
           ref="div"
           pre="0"
      >

      </div>
    </el-dialog>
</template>

<script>
    import $ from 'jquery';
    import handler from './handler';

    export default {
      name: "index",
      data(){
        return {
          visible: false,
          data: {},
          testData: {name: "90", age: 29, arr: [], obj: {}}
        }
      },
      watch: {
        data(){

        }
      },
      methods: {
        open(data){
          this.visible = true;
          this.data = data;
        },
        after_open(){
          let str = this.data;
          let div = this.$refs.div;
          //console.log(str);
          $(div).empty();
          // let obj = this.testData;
          // handler.createItem(div, obj, true);
          // return;
          if(handler.canJson(str)){
            let obj = JSON.parse(str);
            handler.createItem(div, obj, true);
          }else{
            console.log('不能json');
            $(div).text(str);
          }
        }
      },
      mounted() {
        let that = this;
        $('.my_json_tree__i_').on('click', '.json-tree-selected', function (e) {
          e.stopPropagation(); // 阻止事件冒泡
          let tagName = e.target.tagName;
          let data = $(this).data('__data');
          let init = $(this).attr('init');
          let arrowName = $(e.target).attr('class');
          let dir = handler.changeArrowDir(arrowName);
          if(dir){
            $(e.target).attr('class', dir);
            if(init === 'yes'){
              let the_wapper = $(this).children('.json-tree-item-wrapper');
              let the_span = $(this).children('.json-tree-value-span');
              let span_wapper = $(this).children('.json-tree-value-wrapper');
              if(handler.isItemClose(arrowName)){
                the_wapper.show();
                the_span.show();
                span_wapper.hide();
              }else{
                the_wapper.hide();
                the_span.hide();
                span_wapper.show();
              }
            }
          }
          //console.log(data);
          //console.log(arrowName);
          if('I' === tagName && init !== 'yes'){
            handler.createItem($(this)[0], data, false);
            $(this).attr('init', 'yes');
          }
        });
      }
    }
</script>

<style>
  .my_json_tree__i_ .json-tree-item-value{

  }
  .my_json_tree__i_ .el-dialog__body{
    padding: 10px 20px 20px 20px;
    max-height: 460px;
    min-height: 200px;
    overflow: auto;
  }
  .my_json_tree__i_ .el-dialog__header{
    padding: 20px 20px 0 20px;
  }
  .my_json_tree__i_ .el-dialog__title{
    font-size: 14px;
  }
</style>
