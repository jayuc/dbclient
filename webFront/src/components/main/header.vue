<template>
  <span>
    <div class="main_header_sql_tip_">
      请输入语句:
      <a href="#/">
        <i class="el-icon-back main_header_sql_back_" title="返回创建连接页面"></i>
      </a>
    </div>
    <el-input type="textarea"
              class="main_header_"
              v-model="sql"
              ref="textarea"
              :draggable="false"
    />
    <div class="main_header_sql_btn_">
      <el-button @click="clearSql" size="mini">清空</el-button>
      <el-button type="primary" @click="execute" size="mini">执行</el-button>
      <a class="main_header_sql_btn_a_">切换数据库：</a>
      <el-select v-model="databaseName"
                 size="mini"
                 style="width: 156px"
                 @change="selectChange"
      >
        <el-option
          v-for="item in databaseOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <div class="main_header_sql_btn_inner_btn_">
        <el-button icon="el-icon-arrow-down"
                   size="mini"
                   @click="toDown"
                   plain
                   type="info"
                   :class="downClass"
                   title="向下"
        />
        <el-button icon="el-icon-arrow-up"
                   size="mini"
                   type="success"
                   @click="toUp"
                   plain
                   :class="upClass"
                   title="向上"
        />
        <el-button icon="el-icon-minus"
                   size="mini"
                   type="primary"
                   plain
                   @click="toInitHeight"
                   :class="initClass"
                   title="恢复"
        />
      </div>
    </div>
  </span>
</template>

<script>
    import User from '@/user';
    import AjaxUtil from '@/utils/AjaxUtil';
    import ResultUtil from '@/utils/ResultUtil';
    import $ from 'jquery';
    import TextareaUtil from '@/utils/TextareaUtil';
    import handler from './handler';
    import loginHandler from '../login/handler';

    export default {
      name: "main-header",
      data(){
        return {
          sql: '',
          initClass: 'hide',
          upClass: 'inline_block_show',
          downClass: 'inline_block_show',
          databaseName: '',
          databaseOptions: []
        }
      },
      methods: {
        clearSql(){
          this.sql = '';
          let data = {
            result: {
              headers: [],
              rows: [],
              total: 0
            },
            attributes: {
              took: 0
            }
          };
          this.$emit('get-data', data);
        },
        execute(){
          if(this.sql.length === 0){
            this.$message.error('sql不能为空');
            return ;
          }
          // 用户是否已经连接
          let connected = User.get('connected');
          if(connected !== 'yes'){
            this.$message.error('未成功连接');
            this.$router.push("/");
            return;
          }
          this.$emit('start-get-data', true);
          let that = this;
          AjaxUtil.get('sql/execute', this.getParam()).then((data) => {
            console.log(data);
            that.$emit('start-get-data', false);
            that.$emit('get-data', data);
            ResultUtil.handle(data, null, that);
          }, (err) => {
            that.$emit('start-get-data', false);
            that.$message.error('请求出错，错误原因：' + err.message);
          });
        },
        getSqlSelection(){
          return TextareaUtil.getSelection(this.$refs.textarea.$el.children[0]);
        },
        getParam(){
          let data = {};
          let sql = this.sql;
          let selectSql = this.getSqlSelection();
          if(selectSql.length > 0){   // 当有鼠标选择的内容时，则使用鼠标选中的内容
            sql = selectSql;
          }
          data.sql = sql;
          return data;
        },
        modifyTextareaHeight(_height){
          $('.main_header_ .el-textarea__inner').css({
            height: _height
          });
        },
        toDown(){
          this.$emit('to-down');
          this.modifyTextareaHeight(440);
          this.downClass = 'hide';
          this.initClass = 'inline_block_show';
          this.upClass = 'inline_block_show';
        },
        toUp(){
          this.$emit('to-up');
          this.modifyTextareaHeight(33);
          this.upClass = 'hide';
          this.downClass = 'inline_block_show';
          this.initClass = 'inline_block_show';
        },
        toInitHeight(){
          this.$emit('to-init-height');
          this.modifyTextareaHeight(180);
          this.upClass = 'inline_block_show';
          this.downClass = 'inline_block_show';
          this.initClass = 'hide';
        },
        showDataBase(){
          let dbId = User.get('dbId');
          let dbType = handler.getDbTypeFromDbId(dbId);
          handler.showDataBases[dbType](dbType).then((list) => {
            //console.log(list);
            this.databaseOptions = list;
            this.databaseName = handler.getDbNameFromDbId(dbId);
          }, (err) => {
            console.error(err);
          });
        },
        selectChange(dbName){
          //console.log(dbName);
          let dbId = User.get('dbId');
          let param = handler.getDbParamFromDbId(dbId);
          let that = this;
          param.name = dbName;
          //console.log(param);
          loginHandler.connect(this, param, (data, dbId) => {
            //console.log(data);
            //console.log(dbId);
            that.$emit('select-database', dbId);
          });
        }
      },
      mounted() {
        this.showDataBase();
      }
    }
</script>

<style>
  .main_header_sql_tip_{
    color: blue;
    margin-bottom: 7px;
    font-size: 14px;
    font-family: '微软雅黑';
  }
  .main_header_sql_btn_{
    margin-top: 7px;
    position: relative;
  }
  .main_header_sql_btn_inner_btn_{
    position: absolute;
    top: 0;
    right: 0;
  }
  .main_header_sql_back_{
    float: right;
    cursor: pointer;
    margin: 5px 2px 0px 0px;
  }
  .main_header_ .el-textarea__inner{
    height: 180px;
    font-size: 14px;
    font-family: Arial;
  }
  .main_header_sql_btn_a_{
    margin-left: 10px;
    color: blue;
    vertical-align: -2px;
  }
</style>
