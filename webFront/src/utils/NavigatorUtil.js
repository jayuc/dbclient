/**
 * Created by yujie on 2019/4/25 16:14
 */

// 高度
const height = () => {
  return window.screen.availHeight;
};

// 宽度
const width = () => {
  return document.body.clientWidth;
};

export default {
  height,
  width,
}
