
import NumberUtil from '@/utils/NumberUtil';
import StringUtil from '@/utils/StringUtil';
import entity from '../configs';
import Config from '@/config';
import AjaxUtil from '@/utils/AjaxUtil';

const contain = (row, arr) => {
	if(!row){
		return '列号不能为空';
	}
	if(!NumberUtil.isNumber(row)){
		return '列号必须为数字';
	}
	if(row <= 0){
		return '列号必须为正整数';
	}
	for(let i=0; i<arr.length; i++){
		if(row == arr[i].id){
			return '列号已经存在';
		}
	}
	return 'true';
};

const containFeild = (feild, arr, realFeilds) => {
	if(StringUtil.isBlank(feild)){
		return '字段不能为空';
	}
	if(!realFeilds[feild.toLowerCase()]){
		return '字段值与数据库不一致';
	}
	for(let i=0; i<arr.length; i++){
		if(feild == arr[i].value){
			return '字段已设置';
		}
	}
	return 'true';
};

const intLike = ['int', 'number', 'float', 'double', 'decimal'];
const dateLike = ['date', 'timestamp'];
// 是否为指定类型
const canType = (str, type) => {
	let s = str.toLowerCase();
	let arr = [];
	if(type == 'int'){
		arr = intLike;
	}else if(type == 'date'){
		arr = dateLike;
	}
	for(let i=0; i<arr.length; i++){
		if(s.indexOf(arr[i]) != -1){
			return true;
		}
	}
	return false;
};

const handlerFields = (rows) => {
	let result = {};
	if(rows instanceof Array){
		for(let i=0; i<rows.length; i++){
			let type = rows[i]['Type'];
			let name = rows[i]['Field'].toLowerCase();
			if(canType(type, 'int')){
				result[name] = 'int';
				continue;
			}
			if(canType(type, 'date')){
				result[name] = 'date';
				continue;
			}
			result[name] = 'string';
		}
	}
	return result;
};
// 查询表结构
const queryTableContruct = (tableName, callback) => {
	if(typeof callback !== 'function'){
		return;
	}
	// console.log(tableName);
	let currentDbType = Config.get('currentDbType').toLowerCase();
	let sql = entity.tableQuery[currentDbType]['tableStructure'];
	if(typeof sql === 'function'){
		AjaxUtil.get(entity.tableUrl[currentDbType], {sql: sql(tableName)}).then((data) => {
			if(data.status == 'error'){
				callback(false);
			}else if(data.result.total > 0){
				// console.log(data.result.rows);
				callback(handlerFields(data.result.rows));
			}
		}, () => {
			callback(false);
		});
	}
};

export default {
	contain,
	containFeild,
	queryTableContruct,
}