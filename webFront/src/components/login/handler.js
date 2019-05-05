/**
 * Created by yujie on 2019/4/28 9:33
 */

import AjaxUtil from '@/utils/AjaxUtil';
import User from '@/user';
import CookieUtil from '@/utils/CookieUtil';
import InnerConfig from '@/config/innerConfig';
import ResultUtil from '@/utils/ResultUtil';

// 处理db id
const dbIdHandlers = {
  'Redis': produceRedisDbId,
  'Mysql': produceDbId,
  'Oracle': produceDbId,
};

// 创建连接
const connect = (that, dbType, param) => {
  // 正在加载
  const loading = that.$loading({
    lock: true,
    text: 'Loading',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.5)'
  });
  AjaxUtil.post('newcon/create', param).then((data) => {
    loading.close();  //关闭正在加载
    console.log(data);
    ResultUtil.handle(data, () => {
      that.$message.success('连接成功');
      // 数据库编号
      let dbId = data.attributes.dbId;
      if(dbId){
        User.set('dbId', dbId);
        // 标识用户已经创建连接
        User.set('connected', 'yes');
        // 生产连接
        that.produceConnects(dbId, dbType);
      }
      // 用户标识
      let token = data.attributes.token;
      if(token){
        CookieUtil.set(InnerConfig.cookieName, token, 1000);
      }
      that.close();
      //跳转到主页面
      that.$router.push("/main");
    }, that);
  }, (err) => {
    loading.close();  //关闭正在加载
    console.log(err);
    that.$message.error('连接出错，错误原因：' + err.message);
  });
};

// 获取db id 判断是否已经连接
function getDbId(item) {
  return getStrOutNull(item.type.toLowerCase()) + getStrOutNull(item.host.replace(/\./g, '_')) + getStrOutNull(item.port)
    + getStrOutNull(item.name) + getStrOutNull(item.userName);
}

function getStrOutNull(str) {
  if(str === 0){
    return '0_';
  }
  return str ? str + '_' : '';
}

function produceDbId(dbId) {
  let item = getTypeByDbId(dbId);
  if(item){
    let str = item.str;
    let username = str.substring(str.lastIndexOf('_'), str.length);
    let url = str.substring(0, str.lastIndexOf('_'));
    let urlstr = url.substring(0, url.lastIndexOf('_'));
    let dbname = url.substring(url.lastIndexOf('_'), url.length);
    let ur = urlstr.substring(1, urlstr.lastIndexOf('_'));
    let port = urlstr.substring(urlstr.lastIndexOf('_'), urlstr.length);
    return joinStr(item.type, username, ur, port, dbname);
  }
  return null;
}

function produceRedisDbId(dbId) {
  let item = getTypeByDbId(dbId);
  if(item){
    let str = item.str;
    let url = str.substring(1, str.lastIndexOf('_'));
    let dbname = str.substring(str.lastIndexOf('_'), str.length);
    let ur = url.substring(0, url.lastIndexOf('_'));
    let port = url.substring(url.lastIndexOf('_'), url.length);
    return joinStr(item.type, null, ur, port, dbname);
  }
  return null;
}

function getTypeByDbId(dbId) {
  if(typeof dbId === 'string'){
    let startNum = dbId.indexOf('_');
    let type = dbId.substring(0, startNum);
    let str = dbId.substring(startNum, dbId.length);
    return {
      type,
      str
    }
  }
  return null;
}

function joinStr(_type, _user, _url, _port, _name) {
  let user = _user ? _user + '@' : '';
  let name = _name ? '/' + _name : '';
  return '[' + _type + ']' + user.replace(/_/g, '') + _url.replace(/_/g, '.') + ':'
    + _port.replace(/_/g, '') + name.replace(/_/g, '');
}

export default {
  dbIdHandlers,
  connect,
  getDbId,
}
