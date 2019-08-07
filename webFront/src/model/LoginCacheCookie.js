/**
 * 登陆数据，cache与cookie之间交互类
 * Created by yujie on 2019/8/7 14:04
 */

import innerConfig from '../config/innerConfig';
import CookieUtil from '../utils/CookieUtil';
import LoginDb from './LoginDb';
import LoginData from './LoginData';
import Config from '../config';

// 定义一个cookie前缀
const pre = innerConfig.loginCacheName;

function LoginCacheCookie(cache) {
  if(!cache instanceof LoginDb){
    throw new Error('对象创建失败');
  }
  this.cache = cache;
}

LoginCacheCookie.prototype = {
  cookie: function (type) {
    let cookieName = cookieKey(type);
    let cook = CookieUtil.get(cookieName);
    if(cook){
      CookieUtil.clear(cookieName);
    }
    this.cookieCache(cookieName);
  },
  cookieCache: function (cookieName) {
    CookieUtil.set(cookieName, JSON.stringify(this.cache), 1000);
  }
};

function cookieKey(type) {
  return pre + type;
}


// 获取登陆数据cache
const getLoginDataCache = () => {
  if(!Config.get(pre)){
    Config.set(pre, {});
  }
  return Config.get(pre);
};

// 从cookie中加载所有的缓存登陆数据
const loadLoginDataFromCookie = () => {
  let data = getLoginDataCache();
  let cookieData = CookieUtil.getLike(pre);
  // console.log(cookieData);
  if(cookieData instanceof Array){
    for(let i=0; i<cookieData.length; i++){
      let item = cookieData[i];
      let type = item.key.replace(pre, '');
      // console.log(type);
      data[type] = parseLoginDbFromStr(item.value);
    }
  }
};

function parseLoginDbFromStr(str) {
  let result = null;
  let obj = JSON.parse(str);
  if(obj && obj.db){
    let data = obj.db;
    result = new LoginDb();
    for(let index in data){
      let d = data[index];
      result.addLoginData(new LoginData(d.host, d.port, d.name, d.userName));
    }
  }
  return result;
}

export default {
  LoginCacheCookie,
  getLoginDataCache,
  loadLoginDataFromCookie,
};
