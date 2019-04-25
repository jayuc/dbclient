<template>
  <div>
    <el-table :data="tableData"
              border
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

<style scoped>

</style>
