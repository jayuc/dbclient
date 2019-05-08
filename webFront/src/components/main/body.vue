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
          if(columnIndex > 0){
            let text = row.row[this.headerInfo[columnIndex]];
            let px = bodyHandler.computeStrPx(text);
            let columnWidth = row.column.width;
            if(columnWidth && columnWidth < px){
              this.columns[columnIndex].width = px;
            }
          }
        }
      },
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
  }
</style>
