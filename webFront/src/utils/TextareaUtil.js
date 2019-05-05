/**
 * Created by yujie on 2019/5/5 10:09
 */

/**
 * 获取textarea鼠标选中部分
 * @param textarea 对象
 */
const getSelection = (textarea) => {
  let userSelection;

  if (typeof textarea.selectionStart === 'number' && typeof textarea.selectionEnd === 'number') {
    // 非IE浏览器
    // 获取选区的开始位置
    let startPos = textarea.selectionStart,
      // 获取选区的结束位置
      endPos = textarea.selectionEnd;

    userSelection = textarea.value.substring(startPos, endPos);

  } else if (document.selection) {
    // IE浏览器
    userSelection = document.selection.createRange().text;
  }

  return userSelection;
};

export default {
  getSelection,
}
