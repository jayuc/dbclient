/**
 * Created by yujie on 2019/8/6 14:48
 */

import md5 from 'js-md5';

// 加密
function encode(str) {
  return md5(str);
}

// 解密
function decode() {

}

export default {
  encode,
  decode
}
