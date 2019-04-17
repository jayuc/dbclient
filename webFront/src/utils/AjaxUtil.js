
import $ from 'jquery';
import Config from '@/config';
import User from '@/user';
import CookieUtil from '@/utils/CookieUtil';
import InnerConfig from '@/config/innerConfig';

const restRoot = Config.restRoot;

function ajax(_options) {
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
  $.extend(options, _options);
  options.data.dbId = User.get("dbId");
  options.data.token = CookieUtil.get(InnerConfig.cookieName);
  console.log('param: ');
  console.log(options.data);
  $.ajax({
    url: restRoot + options.url,
    type: options.type,
    data: options.data,
    timeout: 1000000,
    success: options.success,
    error: options.error
  });
}

export default {
  ajax,
}
