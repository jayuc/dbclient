/**
 * Created by yujie on 2019/4/25 16:20
 */

import NavigatorUtil from '@/utils/NavigatorUtil';
import Config from '@/config';
import entryLoginCookie from '@/model/LoginCacheCookie';

//项目初始化创建任务
const init = () => {

  // 初始化浏览器数据
  initNavigator();

  // 从cookie中提取登陆数据到内存中
  entryLoginCookie.loadLoginDataFromCookie();

};

//缓存浏览器数据
const initNavigator = () => {
  let height = NavigatorUtil.height();
  Config.set('navigatorHeight', height);

  // 初始化宽度
  Config.set('bodyWidth', NavigatorUtil.width());

};

export default {
  init
}
