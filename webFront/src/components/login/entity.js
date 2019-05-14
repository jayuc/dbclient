/**
 * Created by yujie on 2019/5/14 8:22
 */

// 数据库默认值
const dbValues = {
  'Oracle': {
    port: 1522,        // 默认端口
    name: undefined,   // 默认数据库名
    showName: true,
    showUserName: true,
    userNameRequired: true,   // 是否为必填项
    passwordRequired: true,   // 是否为必填项
  },
  'Mysql': {
    port: 3306,
    name: undefined,
    showName: true,
    showUserName: true,
    userNameRequired: true,   // 是否为必填项
    passwordRequired: true,   // 是否为必填项
  },
  'Redis': {
    port: 6379,
    name: 0,
    showName: true,
    showUserName: false,
    userNameRequired: true,   // 是否为必填项
    passwordRequired: true,   // 是否为必填项
  },
  'Postgresql': {
    port: 5432,
    name: undefined,
    showName: true,
    showUserName: true,
    userNameRequired: true,   // 是否为必填项
    passwordRequired: true,   // 是否为必填项
  },
  'Mongodb': {
    port: 27017,
    name: undefined,
    showName: true,
    showUserName: true,
    userNameRequired: false,   // 是否为必填项
    passwordRequired: false,   // 是否为必填项
  }
};

// 获取验证规则
const getRules = (options) => {
  return {
    host: [{required: true, message: '请输入Ip地址', trigger: 'blur'}],
    port: [{required: true, message: '请输入端口号', trigger: 'blur'},
      {type: 'number', message: '必须为数字', trigger: 'change'},
      {validator(rule, value, callback) {
          if(value > 65535){
            callback(new Error('端口号超过最大值'));
          }
          callback();
        }, trigger: 'change'}],
    name: [{required: true, message: '请输入数据库名', trigger: 'blur'}],
    userName: [{required: options.userNameRequired, message: '请输入用户名', trigger: 'blur'}],
    password: [{required: options.passwordRequired, message: '请输入数据库密码', trigger: 'blur'}],
  };
};

export default {
  dbValues,
  getRules,
}
