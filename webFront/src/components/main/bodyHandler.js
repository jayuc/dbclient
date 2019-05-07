/**
 * Created by yujie on 2019/5/6 18:00
 */
import StringUtil from '@/utils/StringUtil';
import config from '@/config';

// 设定最大cell宽度
const maxCellWidth = 240;
// 中文单个字符所占px
const chineseSingleWidth = 12;
// 额外宽度
const extraWidth = 6;
// 单个字母宽度
const singleLetterWidth = 10;
// 内容单个字母宽度
const bodyLetterWidth = 8;

// 处理列表
const handleRows = (rows) => {
  if(rows instanceof Array){
    for(let i=0; i<rows.length; i++){
      rows[i]['__id__'] = i+1;
    }
  }
  return rows;
};

function hHeaders(arr, result, headerInfo) {
  // 第一列 序号列宽度
  const numberRowWidth = 30;
  // table 宽度
  const tableWidth = config.get('tableWidth') - numberRowWidth;
  let width = maxCellWidth;
  // 已占宽度
  let total = numberRowWidth;
  // 是否使用最大宽度
  let userMaxWidth = maxCellWidth*arr.length < tableWidth;
  // 是否不加一行
  let appendRow = false;
  if(arr.length > 0){
    for (let i=0; i<arr.length; i++){
      let item = arr[i];
      if(userMaxWidth){
        width = maxCellWidth;
        if(i === arr.length - 1){
          if((tableWidth - total) < maxCellWidth){
            width = undefined;
          }else{
            appendRow = true;
          }
        }
      }else{
        if(i === arr.length - 1){
          if((tableWidth - total) < maxCellWidth){
            width = undefined;
          }else{
            appendRow = true;
          }
        }
        width = item.length*singleLetterWidth + extraWidth;
      }
      result.push({
        prop: item,
        label: item,
        width: width,
        key: i
      });
      total += width;
      headerInfo[i + 1] = item;
    }
    if(appendRow){
      result.push({
        prop: '____',
        label: '',
        key: arr.length
      });
    }
  }
}
// 处理表头，主要时计算头部的宽度
const handleHeaders = (arr) => {

  // 判断是否时数组
  let result = [];
  // 表头信息
  let headerInfo = {};
  if(!(arr instanceof Array)){
    return result;
  }

  // 增加显示序列号字段
  if(arr.length > 0){
    result.push({
      prop: '__id__',
      label: '',
      key: '00',
      width: 30,
    });
  }

  // 处理
  hHeaders(arr, result, headerInfo);

  return {
    result,
    headerInfo,
  };

};

// 计算字符串所占px
const computeStrPx = (str) => {
  if(typeof str === 'string'){
    let singlePx = bodyLetterWidth;
    if(StringUtil.containChinese(str)){
      singlePx = chineseSingleWidth;
    }
    let width = str.length*singlePx + extraWidth;
    return width > maxCellWidth ? maxCellWidth : width;
  }
  return 0;
};

export default {
  handleRows,
  handleHeaders,
  computeStrPx
}
