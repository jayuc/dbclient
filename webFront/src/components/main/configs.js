/**
 * Created by yujie on 2019/4/28 14:09
 */

import Database from '@/model/Database';
import user from '@/user';

const tableSql = {
  'mysql': 'show tables',
  'oracle': 'select table_name from user_tables',
  'redis': 'getRedisAllKeys',
};

const tableQuery = {
  'mysql': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {return 'show full columns from ' + tableName},
    'tableInfo': (tableName) => {
      let database = new Database(user.get('dbId'), true).name;
      return 'select table_comment from information_schema.TABLES ' +
        'where TABLE_SCHEMA=\'' + database + '\' and TABLE_NAME= \'' + tableName + '\''
    }
  },
  'oracle': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {
      //return 'select dbms_metadata.get_ddl(\'TABLE\',\'' + tableName.toUpperCase() + '\') from dual'
      /**
       * 怎么查询表结构需要好好考虑一下
       */
      return 'select t_c.*,t_e.comments from ' +
              '(select col.COLUMN_NAME,col.DATA_TYPE || \'(\' || col.DATA_LENGTH || \')\' DATA_TYPE,col.DATA_DEFAULT,col.NULLABLE ' +
              'from user_tab_columns col where col.TABLE_NAME = \'' + tableName + '\') t_c, ' +
              '(select coe.COMMENTS,coe.COLUMN_NAME from user_col_comments coe where coe.TABLE_NAME = \'' + tableName +'\') t_e ' +
              'where t_c.column_name = t_e.column_name(+) and rownum < 1000';
    },
    'tableInfo': (tableName) => {
      return 'select comments from user_tab_comments ' +
        'where table_name=\'' + tableName + '\'';
    }
  },
  'redis': {
    'string': (key) => {return 'get ' + key},
    'hash': (key) => {return 'hgetAll ' + key},
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
  return undefined;
};
const tableChildrenNode = {
  'mysql': produceChildNode,
  'oracle': produceChildNode,
  'redis': produceChildNodeByRedis,
};

const sqlurl = 'sql/execute';
const tableUrl = {
  'mysql': sqlurl,
  'oracle': sqlurl,
  'redis': 'redis/execute'
};

const _tn = (da, field) => {
  return da[field];
};
const _tn_redis = (da, field) => {
  let _type = da.type ? da.type : '';
  let type = _type;
  if(_type.length > 0){
    type = '[' + _type.substring(0, 1).toUpperCase() + _type.substring(1, _type.length) + ']';
  }
  return type + da[field];
};
const tableName = {
  'mysql': _tn,
  'oracle': _tn,
  'redis': _tn_redis
};

const _tt = (da) => {
  return undefined;
};
const _tt_redis = (da) => {
  return da['type'];
};
const tableType = {
  'mysql': _tt,
  'oracle': _tt,
  'redis': _tt_redis
};

const _tcn = (da, field) => {
  return undefined;
};
const _tcn_redis = (da, field) => {
  return da[field];
};
const tableChildName = {
  'mysql': _tcn,
  'oracle': _tcn,
  'redis': _tcn_redis
};

// 查询所有数据库语句
const allDatabases = {
  'mysql': 'show databases',
  'oracle': 'select name from v$database'
};

function getMysqlDbNameFromDbId(dbId) {
  let database = new Database(dbId, true);
  return database.name;
}

function getRedisDbNameFromDbId(dbId) {
  let database = new Database(dbId);
  return database.name;
}

// 获取数据库类型
const getDbNames = {
  'mysql': getMysqlDbNameFromDbId,
  'oracle': getMysqlDbNameFromDbId,
  'redis': getRedisDbNameFromDbId,
};

function getRedisDbParamFromDbId(dbId) {
  let database = new Database(dbId);
  return {
    host: database.host,
    port: database.port,
    type: dbTypes[database.type]
  }
}

function getMysqlDbParamFromDbId(dbId) {
  let database = new Database(dbId, true);
  return {
    host: database.host,
    port: database.port,
    userName: database.userName,
    type: dbTypes[database.type]
  }
}

// 获取当前连接参数
const getDbParam = {
  'mysql': getMysqlDbParamFromDbId,
  'oracle': getMysqlDbParamFromDbId,
  'redis': getRedisDbParamFromDbId,
};

//
const dbTypes = {
  'mysql': 'Mysql',
  'oracle': 'Oracle',
  'redis': 'Redis',
};

function getMysqlDatabase(dbId) {
  return new Database(dbId, true);
}
function getRedisDatabase(dbId) {
  return new Database(dbId);
}
// 获取database
const getDatabase = {
  'mysql': getMysqlDatabase,
  'oracle': getMysqlDatabase,
  'redis': getRedisDatabase,
};

export default {
  tableSql,
  tableQuery,
  tableNa,
  tableChildrenNode,
  tableUrl,
  tableName,
  tableType,
  tableChildName,
  allDatabases,
  getDbNames,
  getDbParam,
  dbTypes,
  getDatabase,
}
