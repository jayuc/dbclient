
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

const intLike = ['int'];
const dateLike = ['date', 'time'];
const yearLike = ['year'];
const numberLike = ['number', 'float', 'double', 'decimal'];
// 是否为指定类型
const canType = (str, type) => {
	let s = str.toLowerCase();
	let arr = [];
	if(type == 'int'){
		arr = intLike;
	}else if(type == 'date'){
		arr = dateLike;
	}else if(type == 'number'){
		arr = numberLike;
	}else if(type == 'year'){
		arr = yearLike;
	}
	for(let i=0; i<arr.length; i++){
		if(s.indexOf(arr[i]) != -1){
			return true;
		}
	}
	return false;
};

const dbColumn = {
	'mysql': {
		type: 'Type',
		name: 'Field'
	},
	'oracle': {
		type: 'DATA_TYPE',
		name: 'COLUMN_NAME'
	}
};
const handlerFields = (rows, dbType) => {
	let result = {};
	if(rows instanceof Array){
		for(let i=0; i<rows.length; i++){
			let type = rows[i][dbColumn[dbType]['type']];
			let name = rows[i][dbColumn[dbType]['name']].toLowerCase();
			if(canType(type, 'int')){
				result[name] = 'int';
				continue;
			}
			if(canType(type, 'date')){
				result[name] = 'date';
				continue;
			}
			if(canType(type, 'number')){
				result[name] = 'number';
				continue;
			}
			if(canType(type, 'year')){
				result[name] = 'year';
				continue;
			}
			result[name] = 'string';
		}
	}
	// console.log(result);
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
				callback(handlerFields(data.result.rows, currentDbType));
			}else if(data.result.total == 0){
				callback(false);
			}
		}, () => {
			callback(false);
		});
	}
};

// 生成 sql语句
const createSql = (fieldArr, tableName) => {
	if(!tableName || tableName.length == 0){
		return "";
	}
	let sl = "insert into " + tableName + " (";
	let sr = ") values (";
	// console.log(fieldArr);
	if(fieldArr instanceof Array && fieldArr.length > 0){
		for(let i=0; i<fieldArr.length; i++){
			let item = fieldArr[i];
			let value = item.value.toUpperCase();
			if(item && value && value.length > 0){
				if(i == fieldArr.length - 1){
					sl += value;
					sr += "?)";
				}else{
					sl += value + " ,";
					sr += "? ,";
				}
				
			}
		}
		return sl + sr;
	}
	return "";
};

const foreachFields = (fields, index) => {
	if(fields.length > 0 && fields[0].index > index){
		return 0;
	}
	for(let i=0; i<(fields.length-1); i++){
		let pre = fields[i];
		let next = fields[i+1];
		// console.log(i);
		// console.log(pre);
		// console.log(next);
		if(next.index > index && pre.index < index){
			return i+1;
		}
	}
	return -1;
};

// 生成字段约束规则
const createFieldRules = (arr, fields) => {
	let result = {};
	for(let i=0; i<arr.length; i++){
		let item = arr[i];
		result[item.index] = fields[item.value];
	}
	return result;
};

// 提交导入任务
const submitTask = (param, resolve, reject) => {
	AjaxUtil.post('batch/insert', param).then((data) => {
		if(typeof resolve === 'function'){
			resolve(data);
		}
	}, (e) => {
		if(typeof reject === 'function'){
			reject(e);
		}
	});
};

export default {
	contain,
	containFeild,
	queryTableContruct,
	submitTask,
	createSql,
	createFieldRules,
	foreachFields,
}