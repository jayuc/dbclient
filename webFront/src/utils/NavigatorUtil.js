/**
 * Created by yujie on 2019/4/25 16:14
 */

// 浏览器可见高度
const height = () => {
  return document.body.clientHeight;
};

// 浏览器可见宽度
const width = () => {
  return document.body.clientWidth;
};

export default {
  height,
  width,
}
