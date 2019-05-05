/**
 * Created by yujie on 2019/4/28 14:09
 */

const tableSql = {
  'mysql': 'show tables',
  'oracle': 'select table_name from user_tables',
  'redis': 'getRedisAllKeys',
};

const tableQuery = {
  'mysql': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {return 'desc ' + tableName},
  },
  'oracle': {
    'query': (tableName) => {return 'select * from ' + tableName},
    'tableStructure': (tableName) => {
      //return 'select dbms_metadata.get_ddl(\'TABLE\',\'' + tableName.toUpperCase() + '\') from dual'
      /**
       * 怎么查询表结构需要好好考虑一下
       */
      return null;
    },
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
  let url = dbId.substring(0, dbId.lastIndexOf('_'));
  return getRedisDbNameFromDbId(url);
}

function getRedisDbNameFromDbId(dbId) {
  return dbId.substring(dbId.lastIndexOf('_') + 1, dbId.length);
}

// 获取数据库类型
const getDbNames = {
  'mysql': getMysqlDbNameFromDbId,
  'oracle': getMysqlDbNameFromDbId,
  'redis': getRedisDbNameFromDbId,
};

function getRedisDbParamFromDbId(dbId) {
  let name = getRedisDbNameFromDbId(dbId);
  let url = dbId.substring(dbId.indexOf('_') + 1, dbId.lastIndexOf('_'));
  let port = parseInt(getRedisDbNameFromDbId(url));
  let _host = url.substring(0, url.lastIndexOf('_'));
  let host = _host.replace(/_/g, '.');
  return {
    host,
    port,
    name,
  }
}

function getMysqlDbParamFromDbId(dbId) {
  let userName = getRedisDbNameFromDbId(dbId);
  let url = dbId.substring(0, dbId.lastIndexOf('_'));
  let temp = getRedisDbParamFromDbId(url);
  temp.userName = userName;
  return temp;
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
}
