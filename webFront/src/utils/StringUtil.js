/**
 * Created by yujie on 2019/5/7 9:34
 */

// 判断字符串是否包含大写字母
const containCapitalLetter = (str) => {
  let reg = /[A-Z]/;
  return reg.test(str);
};

// 判断包含多少个大写字母
const containCapitalLetterNum = (str) => {
  let reg = /[A-Z]/g;
  return reg.exec(str);
};

// 判断是否包含中文字符
const containChinese = (str) => {
  let reg = /[\u4e00-\u9fa5]/;
  return reg.test(str);
};

// 判断字符串是否为空
const isBlank = (str) => {
  if(typeof str === 'string' && str.length > 0){
    return false;
  }
  return true;
};

export default {
  containCapitalLetter,
  containCapitalLetterNum,
  containChinese,
  isBlank,
}
