<template>
	<el-dialog :visible.sync="visible"
			   title="错误明细："
               width="800px"
	>
		<el-table :data="errorList"
				  style="width: 100%"
				  border
				  height="500"
				  size="mini"
		>
			<el-table-column
				prop="row"
				label="行号"
				align="center"
				width="60">
			</el-table-column>
			<el-table-column
				prop="errorInfo"
				label="错误原因"
				align="center"
				width="">
			</el-table-column>
		</el-table>
		<div class="h10"></div>
	</el-dialog>
</template>
<script>


// 生成表格
const processList = (rows, errors) => {
	let arr = [];
	if(rows instanceof Array && errors instanceof Array){
		for(let i=0; i<errors.length; i++){
			arr.push({
				row: rows[i],
				errorInfo: errors[i]
			});
		}
	}
	return arr;
};
export default {
	name: 'excel-dialog-progress-error-detail',
	data(){
		return {
			visible: false,
			errorList: []
		}
	},
	methods: {
		open(data){
			this.visible = true;
			if(data){
				// console.log(data);
				this.errorList = processList(data.failRows, data.errorList);
			}
		},
		close(){
			this.visible = false;
		}
	}
}
</script>