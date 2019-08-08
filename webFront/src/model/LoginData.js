/**
 * Created by yujie on 2019/8/6 11:22
 */

import Md5Util from '../utils/Md5Util';
import CodeUtil from '../utils/CodeUtil';

// 加工字符串
const str = (s) => {
  if(s){
    return s + '';
  }
  return '';
};

function LoginData(host, port, name, userName, password) {
  this.host = CodeUtil.encode(host);
  this.port = CodeUtil.encode(port);
  this.name = CodeUtil.encode(name);
  this.userName = CodeUtil.encode(userName);
  this.password = CodeUtil.encode(password);
  this.id = Md5Util.encode(str(host) + str(port) + str(name) + str(userName));
}

LoginData.prototype = {
  generateId: function () {
    // return Md5Util.encode(this.host + this.port + this.name + this.userName);
    return this.id;
  }
};

export default LoginData;
