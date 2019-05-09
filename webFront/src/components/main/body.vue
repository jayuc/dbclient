<template>
  <div class="main_body_table_">
    <div class="main_body_table_tip_">
      <span class="ti">
        执行结果：
      </span>
      <span :class="tookClass">
        &nbsp;&nbsp;&nbsp;
        用时：<span style="color: #67C23A;">{{took}}</span> 秒
      </span>
      <span :class="totalClass">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        总数：<span style="color: #409EFF">{{total}}</span>
      </span>
      <span :class="infoClass">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        表信息：<span style="color: #F56C6C">{{tableInfo}}</span>
      </span>
    </div>
    <el-table :data="tableData"
              :height="$attrs.bodyHeight + 'px'"
              v-loading="loading"
              element-loading-text="拼命加载中"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              :cell-style="columnFormat"
              :cell-class-name="convertCellClass"
    >
      <el-table-column v-for="item in columns"
                       :prop="item.prop"
                       :label="item.label"
                       :width="item.width"
                       :key="item.key"
                       :show-overflow-tooltip="true"
                       align="center"
      />
    </el-table>
  </div>
</template>

<script>

    import bodyHandler from "./bodyHandler";
    import StringUtil from '@/utils/StringUtil';
    import $ from 'jquery';

    export default {
      name: "main-body",
      data(){
        return {
          columns: [],
          tableData: [],
          took: 0,
          total: 0,
          tableInfo: '',
          tookClass: 'hide',
          totalClass: 'hide',
          infoClass: 'hide',
          loading: false,
          headerInfo: {},
        }
      },
      methods: {
        assignColumns(data){
          this.columns = data;
        },
        assignTableData(data){
          this.tableData = data;
        },
        assignTookTotal(took, total, headerInfo){
          this.took = took;
          this.total = total;
          this.tookClass = 'inline_show';
          this.totalClass = 'inline_show';
          this.headerInfo = headerInfo;
        },
        setLoading(status){
          this.loading = status;
        },
        setTableInfo(info){
          let className = 'hide';
          if(!StringUtil.isBlank(info)){
            className = 'inline_show';
            this.tableInfo = info;
          }
          this.infoClass = className;
        },
        columnFormat(row){
          //console.log(row);
          let columnIndex = row.columnIndex;
          if(columnIndex > 0){  // 去掉第一个，第一个是序号
            let text = row.row[this.headerInfo[columnIndex]];
            let px = bodyHandler.computeStrPx(text);
            let columnWidth = this.columns[columnIndex].width;
            if(columnWidth){
              if(columnWidth < px){
                this.columns[columnIndex].width = px;
              }
            }
            // 判断是不是最后一个并且后面还有一个最小列
            if(columnIndex === this.headerInfo['size'] && this.columns[columnIndex + 1]){
              if(bodyHandler.isMaxStrPx(text)){
                this.columns[columnIndex].width = undefined;
                this.columns[columnIndex + 1].width = 1;
              }
            }else{
              if(!columnWidth){
                if(80 < px){   // 表格的最小宽度是80
                  this.columns[columnIndex].width = px;
                }
              }
            }
          }
        },
        convertCellClass(cell){
          //console.log(cell);
          let columnIndex = cell.columnIndex;
          let showAfterClass = '_show__after_';
          if(columnIndex > 0) {  // 去掉第一个，第一个是序号
            let text = cell.row[this.headerInfo[columnIndex]];
            let columnWidth = this.columns[columnIndex].width;
            let maxWidth = bodyHandler.computeMaxStrPx(text);
            // 测试bug
            // if (columnIndex === 5 && cell.rowIndex === 5){
            //   console.log(text);
            //   console.log(text.length);
            //   console.log(columnWidth);
            //   console.log(maxWidth);
            // }
            if(columnWidth < maxWidth){
              return showAfterClass;
            }
            // 判断是不是最后一个并且后面还有一个最小列
            if(columnIndex === this.headerInfo['size'] && this.columns[columnIndex + 1]){
              if(bodyHandler.isMaxStrPx(text)){
                return showAfterClass;
              }
            }
          }
        }
      },
      mounted() {
        let that = this;
        $('.main_body_table_').on('click', '._show__after_', function () {
          let text = $(this).children().text();
          that.$emit('get-json-str', text);
        });
      }
    }
</script>

<style>
  .main_body_table_tip_{
    margin-bottom: 5px;
    font-family: '微软雅黑';
  }
  .main_body_table_tip_ .ti{
    color: blue;
    font-size: 14px;
  }
  .main_body_table_ .el-table{
    font-size: 12px;
  }
  .main_body_table_ .el-table--border{
    border-top: none;
  }
  .main_body_table_ .el-table::before{
    height: 0;
  }
  .main_body_table_ .el-table__header th{
    border-top: 1px solid #ebeef5;
    background-color: #F2F6FC;
  }
  .main_body_table_ .el-table__header th:first-child,.main_body_table_ .el-table__row td:first-child{
    border-left: 1px solid #ebeef5;
  }
  .main_body_table_ .el-table__row td,.main_body_table_ .el-table__header th{
    border-right: 1px solid #ebeef5;
    padding: 2px 0;
  }
  .main_body_table_ .el-table__row td > .cell,.main_body_table_ .el-table__header th > .cell{
    padding: 0 2px;
    min-width: 4px;
    position: relative;
  }
  .main_body_table_ .el-table__row td > .cell::after{
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 16px;
    height: 24px;
    cursor: pointer;
    display: none;
  }
  .main_body_table_ .el-table__row td._show__after_ > .cell::after{
    display: block;
  }
</style>
