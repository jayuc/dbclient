/**
 * Created by yujie on 2019/8/6 11:28
 */

import LoginData from './LoginData';

function LoginDb(loginData) {
  this.db = {};
  this.addLoginData(loginData);
}

LoginDb.prototype = {
  /**
   * 获取该input里面可能获取的数据
   * @param type 获取哪一个类型的数据
   * @param loginData 已知参数
   * @returns {Array}
   */
  getColumn: function(type, loginData, tipText){
    let column = [];
    let tm = {};
    if(!loginData instanceof LoginData){
      return column;
    }
    // console.log(loginData);
    for (let index in this.db){
      // console.log(compareLoginData(this.db[index], loginData, type));
      if(compareLoginData(this.db[index], loginData, type)
        && (!tipText || this.db[index][type].indexOf(tipText) > -1)
        && this.db[index][type] != tipText){
        if(!tm[this.db[index][type]]){
          column.push(item(this.db[index][type]));
          tm[this.db[index][type]] = '1';
        }
      }
    }
    return column;
  },
  /**
   * 获取未设置属性的可能值
   * @param loginData
   */
  getEmptyColumn: function(loginData){
    let column = new LoginData([], [], [], []);
    if(!loginData instanceof LoginData){
      return column;
    }
    for(let index in this.db){
      let temp = compareAndGetEmptyColumn(this.db[index], loginData);
      if(typeof temp === 'object'){
        for(let t in temp){
          if(column[t] instanceof Array){
            column[t].push(temp[t]);
          }
        }
      }
    }
    return column;
  },
  addLoginData: function (loginData) {
    if(loginData instanceof LoginData){
      let id = loginData.generateId();
      if(!this.db[id]){
        this.db[id] = loginData;
      }
    }
  }
};

// 判断后一个loginData 是否和前一个相等；当后一个值为空时，也表示相等
// type 为排除这个属性
function compareLoginData(l1, l2, type) {
  for(let index in l2){
    if(index != type && l2[index] && l2[index] != l1[index]){
      return false;
    }
  }
  return true;
}

function compareAndGetEmptyColumn(l1, l2) {
  let obj = {};
  for(let index in l2){
    if(l2[index]){
      if(l2[index] != l1[index]){
        return false;
      }
    }else if(l1[index]){
      obj[index] = l1[index];
    }
  }
  return obj;
}

function item(value) {
  return {value: value};
}

export default LoginDb;
