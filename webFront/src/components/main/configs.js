/**
 * Created by yujie on 2019/4/28 14:09
 */

const tableSql = {
  'mysql': 'show tables',
  'oracle': '',
  'redis': 'keys *',
};

const tableQuery = {
  'mysql': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {return 'desc ' + tableName},
  },
  'oracle': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {
      return 'select dbms_metadata.get_ddl(\'TABLE\',\'' + tableName.toUpperCase() + '\') from dual'
    },
  }
};

const tableDesc = '数据库所有表';
const tableNa = {
  'mysql': tableDesc,
  'oracle': tableDesc,
  'redis': 'redis中所有key'
};

const produceChildNode = (i, tableName) => {
  let arr = [];
  arr.push({id: 'q__' + i, label: '查询', type: 'query', tableName: tableName});
  arr.push({id: 'c__' + i, label: '表结构',type: 'tableStructure', tableName: tableName});
  return arr;
};
const produceChildNodeByRedis = (i, tableName) => {
  return null;
};
const tableChildrenNode = {
  'mysql': produceChildNode,
  'oracle': produceChildNode,
  'redis': produceChildNodeByRedis,
};

export default {
  tableSql,
  tableQuery,
  tableNa,
  tableChildrenNode
}
