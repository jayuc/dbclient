<template>
	<el-dialog :visible.sync="visible"
			   title="Excel导入配置："
               width="800px"
			   class="excel_data_dialog_"
	>
		<el-form :model="formData"
             label-width="92px"
			 size="mini"
             @keyup.enter.native="execute"
    	>
			<el-form-item label="表 名：" prop="tableName">
				<el-input v-model="formData.tableName" 
						  style="width: 140px"
						  @change="checkTableName"
				>
				</el-input>
				<i class="el-icon-success success" v-show="tableSuccessIcon"></i>
				<i class="el-icon-error error" v-show="tableFailIcon"></i>
			</el-form-item>
			<el-form-item label="列 => 字段：">
				<el-tag :key="field.id"
						v-for="field in formData.tableFields"
						closable
						size="small"
						type="primary"
						style="font-size: 14px;margin-right: 10px;margin-top: 2px;"
						:disable-transitions="false"
						@close="deleteFeild(field)"
				>
					{{field.id}} => {{field.value}}
				</el-tag>
				<el-button type="primary"
						   plain
						   @click="showFieldEdit"
						   v-show="!fieldEditBoxShow"
						   :disabled="addFeildButtonDisabled"
				>
					添加
				</el-button>
			</el-form-item>
			<el-form-item label="" v-show="fieldEditBoxShow">
				<span>编辑 列 => 字段 ：</span>
				<el-input 
					style="width: 60px" 
					v-model="initRow"
					@blur="checkRow"
				>
				</el-input> =>
				<el-input style="width: 160px"
						  v-model="initFeildValue" 
				>
				</el-input>
				<el-button type="primary"
						   plain
						   style="margin-left: 10px;"
						   @click="addFeild"
				>
					添加
				</el-button>
				<el-button type="success"
						   plain
						   @click="hideFieldEdit"
				>
					完成
				</el-button>
			</el-form-item>
			<el-form-item label="上传excel：" prop="tableName">
				<el-upload 	:action="uploadUrl"
							:on-preview="handlePreview"
							:on-remove="handleRemove"
							:before-remove="beforeRemove"
							:on-success="afterFileSuccess"
							:limit="1"
							:on-exceed="handleExceed"
							:file-list="fileList">
							<el-button size="mini" 
									   type="success"
									   plain
							>
								点击上传
							</el-button>
				</el-upload>
			</el-form-item>
		</el-form>
		<span slot="footer" class="dialog-footer">
			<el-button size="small" @click="close">取 消</el-button>
			<el-button size="small" type="primary" @click="execute">导 入</el-button>
		</span>
	</el-dialog>
</template>
<script>

	import handler from './handler.js';
	import Config from '@/config';

	export default {
		name: 'excel-dialog',
		data(){
			return {
				visible: false, // 是否显示
				initRow: 1,   // 初始化列的值
				initFeildValue: '',
				uploadUrl: Config.get('restRoot') + 'upload/file/one',  // 图片上传地址
				realFeilds: {},  // 真实字段值
				formData: {
					tableName: undefined,
					tableFields: [],
					filePath: '',
				},
				tableSuccessIcon: false,  // 表名 成功标识
				tableFailIcon: false,  // 表名 失败标识
				fieldEditBoxShow: false,  // 列与字段对应关系 是否显示
				addFeildButtonDisabled: true,
				fileList: []
			}
		},
		methods: {
			execute(){


			},
			addFeild(){
				let arr = this.formData.tableFields;
				let field = this.initFeildValue;
				let realFeilds = this.realFeilds;
				let ret = handler.containFeild(field, arr, realFeilds);
				if(ret !== 'true'){
					this.$message.error(ret);
					return;
				}
				if(this.checkRow()){
					let item = {id: this.initRow, value: field};
					this.formData.tableFields.push(item);
					this.initRow++;
				}
			},
			checkRow(){
				let arr = this.formData.tableFields;
				let row = this.initRow;
				let ret = handler.contain(row, arr);
				if(ret !== 'true'){
					this.$message.error(ret + '，列号将重置');
					this.initRow = undefined;
					return false;
				}
				return true;
			},
			checkTableName(value){
				// console.log(value);
				let that = this;
				handler.queryTableContruct(value, (construct) => {
					// console.log(construct);
					if(construct){
						that.realFeilds = construct;
						that.enableAddField(false);
						that.enableTableIncon(true);
					}else{
						that.enableAddField(true);
						that.formData.tableFields = [];
						that.hideFieldEdit();
						that.enableTableIncon(false);
						that.$message.error('表名不正确，数据库中无此表');
					}
				})
			},
			enableTableIncon(boolean){
				if(boolean){
					this.tableSuccessIcon = true;
					this.tableFailIcon = false;
				}else{
					this.tableSuccessIcon = false;
					this.tableFailIcon = true;
				}
			},
			enableAddField(boolean){
				this.addFeildButtonDisabled = boolean;
			},
			deleteFeild(field){
				this.formData.tableFields.splice(this.formData.tableFields.indexOf(field), 1);
			},
			showFieldEdit(){
				this.fieldEditBoxShow = true;
			},
			hideFieldEdit(){
				this.fieldEditBoxShow = false;
			},
			open(){
				this.visible = true;
				this.resetForm();
			},
			close(){
				this.visible = false;
				this.resetForm();
			},
			afterFileSuccess(file){
				// console.log(file);
				if(file.status == 1){
					this.formData.filePath = file.path;
				}else{
					this.$message.error('上传文件出错：' + file.errorInfo);
				}
			},
			handleRemove(file, fileList) {
				console.log(file, fileList);
			},
			handlePreview(file) {
				console.log(file);
			},
			handleExceed(files, fileList) {
				this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
			},
			beforeRemove(file, fileList) {
				return this.$confirm(`确定移除 ${ file.name }？`);
			},
			// 表单重置
			resetForm(){
				this.formData.tableName = undefined;
				this.formData.tableFields = [];
				this.initFeildValue = '';
				this.initRow = 1;
				this.realFeilds = {};
				this.fileList = [];
			}
		}
	}

</script>
<style>
	.excel_data_dialog_ .success{
		font-size: 18px;
		color: #67C23A;
		vertical-align: -3px;
		margin: 0px 0px 0px 8px;
	}
	.excel_data_dialog_ .error{
		font-size: 18px;
		color: #F56C6C;
		vertical-align: -3px;
		margin: 0px 0px 0px 8px;
	}
</style>