<template>
	<el-dialog :visible.sync="visible"
			   :title="title"
               width="600px"
			   custom-class="excel_dialog_progress_f"
	>
		<el-progress :percentage="percentage" :stroke-width="12"></el-progress>
		<div class="h10"></div>
		<div>共计 <span style="color: #409EFF;">{{ total }}</span> 条，成功 <span style="color: #67C23A;">{{ success }}</span> 条，失败 <span style="color: #E6A23C;">{{fail}}</span> 条<span v-show="finished">，耗时 <span v-show="showMin"><span style="color: #F56C6C;">{{ tookMin }}</span> 分 </span><span style="color: #F56C6C;">{{ tookSec }}</span> 秒</span>。
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
import NumberUtil from '@/utils/NumberUtil';

export default {
	name: 'excel-dialog-progress',
	data(){
		return {
			visible: false,
			total: 0,
			success: 0,
			fail: 0,
			percentage: 0,
			startTime: 0,
			tookMin: 0,
			showMin: false,
			tookSec: 0,
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
			if(per === 0 || per === 100){
				this.percentage = per;
			}else{
				if(NumberUtil.isInteger(per)){
					this.percentage = per;
				}else if(NumberUtil.isInteger(per*10)){
					this.percentage = parseFloat(per.toFixed(1));
				}else{
					this.percentage = parseFloat(per.toFixed(2));
				}
			}
		},
		checkFinished(){
			if((this.success + this.fail) >= this.total){
				this.finished = true;
				this.title = '导入完成：';
				this.handlerTook();
				return true;
			}
			return false;
		},
		handlerTook(){
			let now = new Date().getTime();
			let took = now - this.startTime;
			let oneMin = 1000*60;
			let min = took/oneMin;
			if(min > 1){
				this.tookMin = parseInt(min);
                this.showMin = true;
			}
			this.tookSec = (took%oneMin)/1000;
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
			// console.log(data);
			this.reset();
			this.visible = true;
			this.computeNumberAndPercentage(data);
			if(data){
				this.startTime = data.startTime;
			}
		},
		close(boolean){
			this.visible = false;
			if(boolean){
				this.$emit('close-excel-dialog');
			}
			this.reset();
		},
		reset(){
			this.total = 0;
			this.success = 0;
			this.fail = 0;
			this.percentage = 0;
			this.failClick = false;
			this.finished = false;
			this.title = '导入进度：';
			this.startTime = 0;
			this.tookMin = 0;
			this.tookSec = 0;
			this.showMin = false;
			this.errorDetails = {};
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
.excel_dialog_progress_f .el-progress__text{
	position: absolute;
	top: 0px;
	right: 0px;
}

</style>