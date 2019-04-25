<template>
  <div class="main_body_table_">
    <div class="main_body_table_tip_">
      执行结果：
    </div>
    <el-table :data="tableData"
              :height="$attrs.bodyHeight + 'px'"
    >
      <el-table-column v-for="item in columns"
                       :prop="item.prop"
                       :label="item.label"
                       :width="item.width"
                       :key="item.key"
      />
    </el-table>
  </div>
</template>

<script>
    export default {
      name: "main-body",
      data(){
        return {
          columns: [],
          tableData: []
        }
      },
      methods: {
        assignColumns(data){
          this.columns = data;
        },
        assignTableData(data){
          this.tableData = data;
        }
      }
    }
</script>

<style>
  .main_body_table_tip_{
    color: blue;
    margin-bottom: 7px;
    font-family: '微软雅黑';
  }
  .main_body_table_ .el-table--border{
    border-top: none;
  }
  .main_body_table_ .el-table::before{
    height: 0;
  }
  .main_body_table_ .el-table__header th{
    border-top: 1px solid #ebeef5;
  }
  .main_body_table_ .el-table__header th:first-child,.main_body_table_ .el-table__row td:first-child{
    border-left: 1px solid #ebeef5;
  }
  .main_body_table_ .el-table__row td,.main_body_table_ .el-table__header th{
    border-right: 1px solid #ebeef5;
  }
</style>
