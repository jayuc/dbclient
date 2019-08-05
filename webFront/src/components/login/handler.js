/**
 * Created by yujie on 2019/4/28 9:33
 */

import AjaxUtil from '@/utils/AjaxUtil';
import User from '@/user';
import ResultUtil from '@/utils/ResultUtil';
import Database from '@/model/Database';
import StringUtil from '@/utils/StringUtil';

// 处理db id
const dbIdHandlers = {
  'Redis': produceRedisDbId,
  'Mysql': produceDbId,
  'Oracle': produceDbId,
  'Postgresql': produceDbId,
  'Mongodb': produceMongodbDbId,
};

// 创建连接
const connect = (that, param, fun, reject) => {
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
        setHavePassword(dbId, param);
      }
      if(typeof fun === 'function'){
        fun(data, dbId);
      }
    }, that, () => {
      if(typeof reject === 'function'){
        reject();
      }
    });
  }, (err) => {
    loading.close();  //关闭正在加载
    console.log(err);
    that.$message.error('连接出错，错误原因：' + err.message);
    if(typeof reject === 'function'){
      reject();
    }
  });
};

// 设置
function setHavePassword(dbId, param) {
  if(param.type === 'Mongodb'){
    let pass = User.get('password');
    if(typeof pass !== 'object'){
      pass = {};
      User.set('password', pass);
    }
    if(StringUtil.isBlank(param.password)){
      pass[dbId] = false;
    }else{
      pass[dbId] = true;
    }
  }
}

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
  let item = new Database(dbId, true);
  return joinStr(item.type, item.userName, item.host, item.port, item.name);
}

function produceRedisDbId(dbId) {
  let item = new Database(dbId);
  return joinStr(item.type, null, item.host, item.port, item.name);
}

function produceMongodbDbId(dbId) {
  if(haveMongodbPasswordByDbId(dbId)){
    return produceDbId(dbId);
  }else{
    return produceRedisDbId(dbId);
  }
}

function joinStr(_type, _user, _url, _port, _name) {
  let user = _user ? _user + '@' : '';
  let name = _name ? '/' + _name : '';
  return '[' + _type + ']' + user + _url + ':' + _port + name;
}

function getMysqlDatabase(dbId, havePassword) {
  return new Database(dbId, havePassword);
}
function getRedisDatabase(dbId) {
  return new Database(dbId);
}
// 获取database
const getDatabase = {
  'Mysql': getMysqlDatabase,
  'Oracle': getMysqlDatabase,
  'Redis': getRedisDatabase,
  'Postgresql': getMysqlDatabase,
  'Mongodb': getMysqlDatabase,
};

// 判断mongodb dbId里面是否有密码
const haveMongodbPasswordByDbId = (dbId) => {
  let password = User.get('password');
  return !!password[dbId];
};

export default {
  dbIdHandlers,
  connect,
  getDatabase,
  getDbId,
  haveMongodbPasswordByDbId,
}
