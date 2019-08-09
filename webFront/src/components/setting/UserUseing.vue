<template>
  <span>
    <div style="margin: 20px 20px 0px 20px;border-bottom: 2px solid #409EFF;
                  padding-bottom: 7px;font-size: 16px;color: #67C23A;font-weight: normal;">
      用户统计：
      <a style="float: right;color: blue;cursor: pointer;margin: 4px 0px 0px 0px;"
         href="#/"
         title="返回"
      >
        <i class="el-icon-back"></i>
      </a>
    </div>
    <div style="width: 600px;padding: 20px">
      <el-table :data="tableData"
                style="width: 100%"
      >
        <el-table-column label="#"
                         prop="number"
                         width="80"
        />
        <el-table-column label="Token"
                         prop="token"
                         width="320"
        />
        <el-table-column label="连接个数"
                         prop="connectNumber"
                         width="120"
        />
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="primary"
                       plain
                       @click="showDetails(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog :visible.sync="visible"
               title="用户使用详情"
               width="400"
    >
      <el-table :data="detailData"
                border
      >
        <el-table-column label="数据库类型"
                         prop="type"
                         width="120"
                         align="center"
        />
        <el-table-column label="Ip地址"
                         prop="host"
                         width="182"
                         align="center"
        />
        <el-table-column label="端口"
                         prop="port"
                         width="100"
                         align="center"
        />
        <el-table-column label="数据库名称"
                         prop="name"
                         width="120"
                         align="center"
        />
        <el-table-column label="用户名"
                         prop="userName"
                         width="120"
                         align="center"
        />
      </el-table>
    </el-dialog>
  </span>
</template>

<script>
    import AjaxUtil from '@/utils/AjaxUtil';

    export default {
      name: "user-use-ing",
      data(){
        return {
          tableData: [],
          tableObj: {},
          visible: false,
          detailData: []
        }
      },
      methods: {
        showDetails(row){
          console.log(row);
          this.detailData = [];
          let data = this.tableObj[row.token];
          if(data){
            for(let index in data){
              this.detailData.push(data[index]);
            }
          }
          console.log(data);
          // 打开详情窗口
          this.openDetail();
        },
        openDetail(){
          this.visible = true;
        },
        init(){
          let that = this;
          AjaxUtil.get('newcon/getUserUseing').then((data) => {
            // console.log(data);
            that.tableData = [];
            if(data){
              let i = 1;
              for(let index in data){
                that.tableData.push({
                  number: i,
                  token: index,
                  connectNumber: data[index].length
                });
                i++;
              }
              that.tableObj = data;
            }
          });
        }
      },
      mounted() {
        this.init();
      }
    }
</script>

<style scoped>

</style>
