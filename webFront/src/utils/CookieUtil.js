
import StringUtil from './StringUtil';

/**
 * 设置cookie
 * @param name cookie名
 * @param value cookie值
 * @param days  cookie存活时间
 */
function set(name, value, days) {
  let d = new Date();
  d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000));
  let expires = "expires=" + d.toUTCString();
  document.cookie = name + "=" + value + "; " + expires+"; path=/";  //这个很重要代表在那个层级下可以访问cookie
}

/**
 * 获取cookie
 * @param name cookie名
 * @returns {string|null}
 */
function get(name) {
  let cname = name + "=";
  let ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while(c.charAt(0) == ' ') c = c.substring(1);
    if(c.indexOf(cname) != -1) return c.substring(cname.length, c.length);
  }
  return null;
}

/**
 * 获取cookie （模糊匹配）
 * @param name
 * @returns {Array}
 */
function getLike(name) {
  let suf = "=";
  let result = [];
  if(StringUtil.isBlank(name)){
    console.warn('匹配字符串不能为空');
    return result;
  }
  if(name.indexOf(suf) != -1){
    console.error("不支持带'='的字符串");
    return result;
  }
  let ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while(c.charAt(0) == ' ') c = c.substring(1);
    let firstEqu = c.indexOf(suf);
    let ind = c.indexOf(name);
    if(ind != -1 && ind < firstEqu){
      result.push(item(c.substring(0, firstEqu), c.substring(firstEqu+1, c.length)));
    }
  }
  return result;
}

function item(key, value) {
  return {key: key, value: value};
}

/**
 * 清除cookie
 * @param name
 */
function clear(name) {
  set(name, "", -1);
}

export default {
    set,
    get,
    getLike,
    clear
}
