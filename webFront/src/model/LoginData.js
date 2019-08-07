/**
 * Created by yujie on 2019/8/6 11:22
 */

import Md5Util from '../utils/Md5Util';

function LoginData(host, port, name, userName) {
  this.host = host;
  this.port = port;
  this.name = name;
  this.userName = userName;
}

LoginData.prototype = {
  generateId: function () {
    return Md5Util.encode(this.host + this.port + this.name + this.userName);
  }
};

export default LoginData;
