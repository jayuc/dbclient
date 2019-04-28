/**
 * Created by yujie on 2019/4/28 9:33
 */

// 处理db id
const dbIdHandlers = {
  'Redis': produceRedisDbId,
  'Mysql': produceDbId,
  'Oracle': produceDbId,
};

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
    let port = str.substring(str.lastIndexOf('_'), str.length);
    return joinStr(item.type, null, url, port, null);
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
}
