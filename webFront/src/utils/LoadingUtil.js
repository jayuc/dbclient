/**
 * 生成正在加载
 */
import StringUtil from './StringUtil';

const createLoading = (that, _text) => {
	if(that){
		let text = "正在加载";
		if(!StringUtil.isBlank(_text)){
			text = _text;
		}
		return that.$loading({
			lock: true,
			text: text,
			spinner: 'el-icon-loading',
			background: 'rgba(0, 0, 0, 0.7)'
		});
	}
	return null;
 };

 export default {
	 createLoading,
 }