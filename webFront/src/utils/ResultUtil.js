/**
 * 处理请求返回结果的util
 * Created by yujie on 2019/4/30 9:08
 */

const handle = (data, successFun, that, failFun) => {
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
    if(data.attributes.go_home_page === 'yes'){
      // 跳转到首页
      that.$router.push("/");
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
