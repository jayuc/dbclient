/**
 * 构造方法
 * @param dbId 数据库id
 * @param isFull 是否包含用户名
 * @constructor
 */
function Database(dbId, isFull) {
  let arr = dbId.split('_');
  let len = 6;
  if(isFull){
    len = 7;
  }
  if(arr.length > len){
    this.type = arr[0];
    this.host = arr[1] + '.' + arr[2] + '.' + arr[3] + '.' + arr[4];
    this.port = parseInt(arr[5]);
    let lastValue = arr[arr.length - 1];
    let arrLen = arr.length;
    if(isFull){
      this.userName = lastValue;
      arrLen = arr.length - 1;
    }else{
      this.userName = null;
    }
    let _dbName = '';
    for (let i=6; i<arrLen; i++){
      _dbName += arr[i] + '_';
    }
    this.name = _dbName.substring(0, _dbName.length - 1);
  }
}

Database.prototype = {
  // 排除了数据库名字的字符串
  exclusionDbNameStr: function(){
    return this.type + '_' + this.host + '_' + this.port + '_' + (this.userName ? this.userName : '');
  }
};

export default Database;
