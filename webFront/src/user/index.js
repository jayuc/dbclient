/**
 * Created by yujie on 2019/4/17 16:36
 */

import Config from '@/config';

const userInfo = Config.root.userInfo;

const get = (key) => {
  if(userInfo){
    return userInfo[key];
  }
};

const set = (key, value) => {
  if(userInfo){
    userInfo[key] = value;
  }
};

export default {
  get,
  set,
};
