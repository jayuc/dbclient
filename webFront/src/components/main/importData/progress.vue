<template>
	<el-dialog :visible.sync="visible"
			   :title="title"
               width="600px"
			   custom-class="excel_dialog_progress_f"
	>
		<el-progress :percentage="percentage" :stroke-width="12"></el-progress>
		<div class="h10"></div>
		<div>共计 <span style="color: #409EFF;">{{ total }}</span> 条，成功 <span style="color: #67C23A;">{{ success }}</span> 条，失败 <span style="color: #E6A23C;">{{fail}}</span> 条。
			<el-link type="primary" 
					 v-show="failClick" 
					 @click="showFailDetail"
					 style="float: right;vertical-align: 1px;"
					 >
					 	查看失败
			</el-link>
		</div>
		<span slot="footer" class="dialog-footer" v-show="finished">
			<el-button size="small" type="primary" @click="close(true)">关 闭</el-button>
		</span>
	</el-dialog>
</template>
<script>


export default {
	name: 'excel-dialog-progress',
	data(){
		return {
			visible: false,
			total: 0,
			success: 0,
			fail: 0,
			percentage: 0,
			errorDetails: {},
			failClick: false,
			finished: false, // 是否完成
			title: '导入进度：',
		}
	},
	methods: {
		computeNumber(data){
			if(data){
				this.total = data.total;
				this.success = data.success;
				this.fail = data.fail;
			}
		},
		computePercentage(){
			let per = ((this.success + this.fail)/this.total)*100;
			this.percentage = parseInt(per);
		},
		checkFinished(){
			if((this.success + this.fail) >= this.total){
				this.finished = true;
				this.title = '导入结果：';
				return true;
			}
			return false;
		},
		computeNumberAndPercentage(data){
			this.computeNumber(data);
			this.computePercentage();
			if(this.checkFinished()){  // 完成
				if(this.fail > 0){
					this.failClick = true;
				}
				this.errorDetails = data;
				return true;
			}
			return false;
		},
		showFailDetail(){
			this.$emit('show-fail-detail', this.errorDetails);
		},
		open(data){
			this.visible = true;
			this.computeNumberAndPercentage(data);
		},
		close(boolean){
			this.visible = false;
			if(boolean){
				this.$emit('close-excel-dialog');
			}
		}
	}
}
</script>
<style>

.excel_dialog_progress_f .el-dialog__body{
	padding: 5px 20px 10px 20px;
}
.excel_dialog_progress_f .el-dialog__footer{
	padding: 0px 20px 16px;
}

</style>