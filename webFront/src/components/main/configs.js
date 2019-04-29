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

export default {
  tableSql,
  tableQuery,
  tableNa,
}
