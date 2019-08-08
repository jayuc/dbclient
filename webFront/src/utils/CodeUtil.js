/**
 * 加密、解密
 * Created by yujie on 2019/8/8 10:43
 */

import base64ObsureCode from '../model/Base64ObsureCode';

const CodeUtil = base64ObsureCode;

// 加密
const encode = CodeUtil.encode;
// 解密
const decode = CodeUtil.decode;

export default CodeUtil;
