/**
 * 处理请求返回结果的util
 * Created by yujie on 2019/4/30 9:08
 */

import AjaxUtil from '@/utils/AjaxUtil';

const handle = (data, successFun, that, failFun, refresh) => {
  if(data.status === 'success'){
    that.$message.success('sql语句已经成功执行');
    if(typeof successFun === 'function'){
      successFun(data, that);
    }
  }else if(data.status === 'error'){
    let errorInfo = data.errorInfo;
    if (errorInfo && errorInfo.length > 0){
      errorInfo = '，错误原因：' + errorInfo;
    } else {
      errorInfo = '';
    }
    that.$message.error('请求出错' + errorInfo);
    if(typeof failFun === 'function'){
      failFun(data, that);
    }
    let needForceRefresh = true;
    if(refresh === false){   // 如果不需要强制刷新
      needForceRefresh = false;
    }
    if(data.attributes.go_home_page === 'yes' && needForceRefresh){
      // 强制刷新页面
      AjaxUtil.forceRefresh('error');
    }
  }else{
    that.$message.error('请求出错');
    if(typeof failFun === 'function'){
      failFun(data, that);
    }
  }
};

export default {
  handle,
}
