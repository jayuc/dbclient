/**
 * Created by yujie on 2019/4/28 14:34
 */

import Config from '@/config';
import entity from './configs';

// 计算所有表
const produceTables = (result, dbType) => {
  let field = result.result.headers[0];
  let rows = result.result.rows;
  let arr  = [];
  for(let i=0; i<rows.length; i++){
    arr.push({
      id: 'f__' + i,
      label: rows[i][field],
      children: entity.tableChildrenNode[dbType](i, rows[i][field])
    });
  }
  return arr;
};

// 计算table树高度
const computeTableStyle = () => {
  let navigatorHeight = Config.get('navigatorHeight');
  let connects = Config.get('connects');
  let extra = 150;
  return {
    overflow: 'auto',
    maxHeight: navigatorHeight - extra - 20*(connects.length)
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

export default {
  produceTables,
  computeTableStyle,
  produceTableOption
}
