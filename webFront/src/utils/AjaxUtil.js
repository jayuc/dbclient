
import $ from 'jquery';
import Config from '@/config';
import User from '@/user';
import CookieUtil from '@/utils/CookieUtil';
import InnerConfig from '@/config/innerConfig';

const restRoot = Config.restRoot;

// 综合方式请求
function ajax(_options, _data, _opt) {

  if (typeof _options == 'object'){   //不使用promise处理
    doAjax(_options);
  }else if(typeof _options == 'string'){ //使用promise处理
    return doAjaxByPromise(_options, _data, _opt);
  }

}

//get方式
function get(_options, _data, _opt) {
  return typeAjax(_options, _data, _opt, 'get');
}

//post方式
function post(_options, _data, _opt) {
  return typeAjax(_options, _data, _opt, 'post');
}

//根据类型请求
function typeAjax(_options, _data, _opt, type) {
  if(typeof _options == 'object'){
    let options = {};
    $.extend(options, _options);
    options.type = type;
    doAjax(options);
  }else if(typeof _options == 'string'){
    let options = {};
    $.extend(options, _opt);
    options.type = type;
    return doAjaxByPromise(_options, _data, options);
  }
}

/**
 * 使用promise方式处理
 * @param _url rest接口
 * @param _data ajax参数
 * @param _options 方法选项
 * @returns {Promise<any>}
 */
function doAjaxByPromise(_url, _data, _options) {
  let options = {
    url: _url,
    data: {},
    type: 'get'
  };
  if(typeof _data == 'object'){
    options.data = _data;
  }
  handleOptions(options, _options);
  return new Promise((resolve, reject) => {
    $.ajax({
      url: handleUrl(options),
      type: options.type,
      data: options.data,
      timeout: 1000000,
      success: function (data) {
        resolve(data);
      },
      error: function (errmsg, status) {
        reject(errmsg);
        forceRefresh(status);
      }
    });
  });
}

/**
 * 使用原生ajax方式
 * @param _options
 */
function doAjax(_options) {
  if(!_options.url){
    throw new Error("url不能为空");
  }
  let options = {
    url: '',
    data: {},
    type: 'get',
    success: function (data) {
    },
    error: function (data) {
    }
  };
  handleOptions(options, _options);
  $.ajax({
    url: handleUrl(options),
    type: options.type,
    data: options.data,
    timeout: 1000000,
    success: options.success,
    error: (err, status) => {
      options.error(err);
      forceRefresh(status);
    }
  });
}

//处理options
function handleOptions(options, _options){
  $.extend(options, _options);
  options.data.dbId = User.get("dbId");
  options.data.token = CookieUtil.get(InnerConfig.cookieName);
}

//处理url
function handleUrl(options) {
  return restRoot + options.url;
}

// 在服务挂机时，强制刷新页面
function forceRefresh(status) {
  if(status === 'error'){
    window.location.reload();
  }
}

export default {
  ajax,
  post,
  get,
}
