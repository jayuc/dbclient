<template>
  <span>
    <div class="main_header_sql_tip_">
      请输入语句:
      <span class="red" style="margin-left: 100px;font-size: 12px">提示：</span>
      <span style="font-size: 12px">执行{{executeTipText}}</span>
      <a href="#/">
        <i class="el-icon-back main_header_sql_back_" title="返回创建连接页面"></i>
      </a>
      <a @click="doSetting">
        <i class="el-icon-setting main_header_sql_setting_" title="设置"></i>
      </a>
    </div>
    <el-input type="textarea"
              class="main_header_"
              v-model="sql"
              ref="textarea"
              :draggable="false"
              @keyup.119.native="execute"
    />
    <div class="main_header_sql_btn_">
      <el-button @click="clearSql"
                 size="mini"
                 :disabled="buttonDisabled"
      >
        清空
      </el-button>
      <el-button type="primary"
                 @click="execute"
                 size="mini"
                 :title="executeTipText"
                 :disabled="buttonDisabled"
      >
        执行
      </el-button>
      <a class="main_header_sql_btn_a_">切换数据库：</a>
      <el-select v-model="databaseName"
                 size="mini"
                 style="width: 156px"
                 @change="selectChange"
                 :disabled="buttonDisabled"
      >
        <el-option
          v-for="item in databaseOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button size="mini"
                 type="primary"
                 style="margin-left: 10px"
                 plain
                 @click="openExcelDialog"
                 v-show="importExcelShow"
      >
        导入Excel文件
      </el-button>
      <div class="main_header_sql_btn_inner_btn_">
        <el-button icon="el-icon-arrow-down"
                   size="mini"
                   @click="toDown"
                   plain
                   type="info"
                   :class="downClass"
                   title="向下"
                   :disabled="buttonDisabled"
        />
        <el-button icon="el-icon-arrow-up"
                   size="mini"
                   type="success"
                   @click="toUp"
                   plain
                   :class="upClass"
                   title="向上"
                   :disabled="buttonDisabled"
        />
        <el-button icon="el-icon-minus"
                   size="mini"
                   type="primary"
                   plain
                   @click="toInitHeight"
                   :class="initClass"
                   title="恢复"
                   :disabled="buttonDisabled"
        />
      </div>
    </div>
    <ExcelDialog ref="excel_dialog" />
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
    import ExcelDialog from './importData/excelDialog';

    export default {
      name: "main-header",
      data(){
        return {
          sql: '',
          initClass: 'hide',
          upClass: 'inline_block_show',
          downClass: 'inline_block_show',
          databaseName: '',
          databaseOptions: [],
          executeTipText: '快捷键: F8',
          buttonDisabled: false,    // 按钮是否处于禁用状态
          importExcelShow: false,    // 导入excel是否显示
        }
      },
      components: {
        ExcelDialog
      },
      methods: {
        openExcelDialog(){
          this.$refs.excel_dialog.open();
        },
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
          // 禁用按钮
          this.buttonDisable();
          AjaxUtil.get('sql/execute', this.getParam()).then((data) => {
            console.log(data);
            that.$emit('start-get-data', false);
            that.$emit('get-data', data);
            ResultUtil.handle(data, null, that);
            // 解除按钮禁用
            that.buttonEnable();
          }, (err) => {
            that.$emit('start-get-data', false);
            // 解除按钮禁用
            that.buttonEnable();
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
          this.showImportExcel();
        },
        selectChange(dbName){
          //console.log(dbName);
          let dbId = User.get('dbId');
          let param = handler.getDbParamFromDbId(dbId);
          let that = this;
          param.name = dbName;
          //console.log(param);
          // 进行按钮禁用
          this.buttonDisable();
          loginHandler.connect(this, param, (data, dbId) => {
            //console.log(data);
            //console.log(dbId);
            that.$emit('select-database', dbId);
            // 解除按钮禁用
            that.buttonEnable();
          }, () => {
            // 解除按钮禁用
            that.buttonEnable();
          });
        },
        showImportExcel(){
          this.importExcelShow = handler.shouldImportExcel();
        },
        doSetting(){
          this.$emit('setting');
        },
        buttonDisable(){
          this.buttonDisabled = true;
        },
        buttonEnable(){
          this.buttonDisabled = false;
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
    color: #409EFF;
  }
  .main_header_sql_setting_{
    float: right;
    cursor: pointer;
    margin: 5px 8px 0px 0px;
    color: #67C23A;
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
