
import NumberUtil from '@/utils/NumberUtil';
import StringUtil from '@/utils/StringUtil';

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
	if(!realFeilds[feild]){
		return '字段值与数据库不一致';
	}
	for(let i=0; i<arr.length; i++){
		if(feild == arr[i].value){
			return '字段已设置';
		}
	}
	return 'true';
};

export default {
	contain,
	containFeild
}