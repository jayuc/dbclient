/**
 * Created by yujie on 2019/4/25 16:20
 */

import NavigatorUtil from '@/utils/NavigatorUtil';
import Config from '@/config';

//项目初始化创建任务
const init = () => {

  initNavigator();

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
