/**
 * Created by yujie on 2019/4/28 14:34
 */

import Config from '@/config';
import entity from './configs';
import AjaxUtil from '@/utils/AjaxUtil';
import user from '@/user';
import $ from 'jquery';

// 计算所有表
const produceTables = (result, dbType) => {
  let arr  = [];
  if(result.result.headers instanceof Array){
    let field = result.result.headers[0];
    let rows = result.result.rows;
    for(let i=0; i<rows.length; i++){
      arr.push({
        id: 'f__' + i,
        label: entity.tableName[dbType](rows[i], field),
        type: entity.tableType[dbType](rows[i]),
        tableName: entity.tableChildName[dbType](rows[i], field),
        children: entity.tableChildrenNode[dbType](i, rows[i][field])
      });
    }
  }
  return arr;
};

// 计算table树高度
const computeTableStyle = () => {
  let navigatorHeight = Config.get('navigatorHeight');
  let connects = Config.get('connects');  //
  // 全局顶部header的高度
  let global_header_height = $('.__app-header__').height();
  let extra = 20 + 20 + 20 + 5 + global_header_height;  // 20是连接树 连接两个字 那一行高度；后面两个20是头部高度和树第一层高度；后面5是为了和右边table对齐
  return {
    overflow: 'auto',
    height: navigatorHeight - extra - 20*(connects.length)
  }
};

// 表操作项
const produceTableOption = (node) => {
  let tableName = node.data.name;
  let arr = [
    {name: '查询', type: 'query'},
    {name: '表结构', type: 'tableStructure'}
  ];
  arr.forEach((item) => {
    item.tableName = tableName;
    item.leaf = true;
  });
  return arr;
};

// 获取mysql oracle 等关系型数据库，可以通过sql查询的
function _getDataBases(dbType) {
  return new Promise((resolve, reject) => {
    AjaxUtil.get('sql/execute', {sql: entity.allDatabases[dbType]}).then((data) => {
      if(data.result.headers instanceof Array && data.result.headers.length > 0){
        let field = data.result.headers[0];
        let arr = [];
        let rows = data.result.rows;
        for(let i=0; i<rows.length; i++){
          arr.push({value: rows[i][field].toLowerCase(), label: rows[i][field].toLowerCase()});
        }
        resolve(arr);
      }
    }, (err) => {
      reject(err);
    });
  });
}
function getRedisDataBases() {
  return new Promise((resolve) => {
    let arr = [];
    for (let i=0; i<16; i++){
      arr.push({value: i, label: i});
    }
    resolve(arr);
  });
}
// 查询所有数据库
const showDataBases = {
  'mysql': _getDataBases,
  'oracle': _getDataBases,
  'redis': getRedisDataBases,
  'postgresql': _getDataBases,
  'mongodb': _getDataBases,
};

// 获取当前数据库类型
const getDbTypeFromDbId = (dbId) => {
  return dbId.substring(0, dbId.indexOf('_'));
};

// 获取当前数据库名
const getDbNameFromDbId = (dbId) => {
  let dbType = getDbTypeFromDbId(dbId);
  return entity.getDbNames[dbType](dbId);
};

// 获取当前连接参数
const getDbParamFromDbId = (dbId) => {
  let dbType = getDbTypeFromDbId(dbId);
  let temp = entity.getDbParam[dbType](dbId);
  let passwords = user.get('password');
  if(typeof passwords === 'object'){
    temp.password = passwords[entity.getDatabase[dbType](dbId).exclusionDbNameStr()];
  }
  return temp;
};

// 导入excel数据库类型
const importExcelDbType = ['mysql', 'oracle'];
const shouldImportExcel = () => {
  let currentDbType = Config.get("currentDbType");
  for(let i=0; i<importExcelDbType.length; i++){
    if(currentDbType.toLowerCase() === importExcelDbType[i].toLowerCase()){
      return true;
    }
  }
  return false;
};

export default {
  produceTables,
  computeTableStyle,
  produceTableOption,
  showDataBases,
  getDbTypeFromDbId,
  getDbNameFromDbId,
  getDbParamFromDbId,
  shouldImportExcel,
}
