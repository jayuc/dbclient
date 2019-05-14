/**
 * Created by yujie on 2019/5/9 13:45
 */
import $ from 'jquery';
import el from "element-ui/src/locale/lang/el";

// 每个节点前长度
const pre = 20;

// 生成子元素
const createItem = (element, data, isInit) => {
  //console.log(element);
  if(isObject(data)){
    appendItemToElement(element, data, isInit);
  }else{
    $(element).text(data);
  }
};

// 判断是否可以转json
const canJson = (str) => {
  try {
    JSON.parse(str);
  }catch (e) {
    return false;
  }
  return true;
};

// 向元素添加子内容
// 已知obj是个对象
function appendItemToElement(element, obj, isInit) {
  let firstElement = '{';
  let lastElement = '}';
  //console.log(element);
  //console.log(element.getAttribute('pre'));
  //console.log(obj);
  let prePx = parseInt(element.getAttribute('pre'));
  if(obj instanceof Array){
    firstElement = '[';
    lastElement = ']';
  }
  let span = $('<span class="json-tree-item-wrapper"></span>');
  if($(element).hasClass('json-tree-item-value')){
    $(element).children('.json-tree-value-wrapper').hide();
    let sp = $('<span class="json-tree-value-span">' + firstElement + '</span>');
    $(element).append(sp);
    sp.addClass('brown');
  }else{
    span.append(getFirstDiv(firstElement, prePx, isInit));
  }
  for (let index in obj){
    let item = obj[index];
    span.append(getDiv(index, item, prePx + pre, isInit));
  }
  span.append(getFirstDiv(lastElement, prePx, isInit));
  $(element).append(span);
}

// 判断是否未对象或数组
function isObject(value) {
  if(value && typeof value === 'object'){
    return true;
  }
  return false;
}

function getTypeByValue(value) {
  //console.log(value);
  let type = typeof value;
  if(type !== 'undefined' && !value){
    return item('null', 'red6c');
  }
  if(value instanceof Array){
    return item('[...]', 'brown');
  }
  switch (type) {
    case "string":
      return item('"' + value + '"', 'green');
    case "number":
      return item(value, 'blue');
    case "undefined":
      return item('undefined', 'red6c');
    case "object":
      return item('{...}', 'brown');
    case "boolean":
      return item(value, 'red6c');
  }
  function item(value, className) {
    return {
      value,
      className
    }
  }
}

function getFirstDiv(value, prePx, isInit) {
  let div = $('<div></div>');
  let preSpan = $('<span></span>');
  let span = $('<span></span>');
  div.append(preSpan);
  preSpan.css({
    display: 'inline-block',
    width: prePx
  });
  div.addClass('brown');
  span.text(value);
  div.append(span);
  return div;
}

// 生成div
function getDiv(key, value, prePx, isInit) {
  let className = 'json-tree-item-value';
  if(isObject(value)){
    className += ' json-tree-selected';
  }
  let div = $('<div class="' + className + '"></div>');
  let keySpan = $('<span></span>');
  let valueSpan = $('<span class="json-tree-value-wrapper"></span>');
  let preSpan = $('<span></span>');
  let arrow = $('<i class="el-icon-caret-right"></i>');
  let da = getTypeByValue(value);
  keySpan.append('"' + key + '" : ');
  valueSpan.append(da.value);
  valueSpan.addClass(da.className);
  div.append(preSpan);
  preSpan.css({
    display: 'inline-block',
    width: prePx
  });
  if (isObject(value)){
    div.append(arrow);
    arrow.css({
      color: '#909399',
      cursor: 'pointer'
    });
    div.data('__data', value);
    div.attr('pre', prePx);
  }
  div.append(keySpan);
  div.append(valueSpan);
  //console.log(prePx);
  return div;
}

// 改变箭头的方向
const changeArrowDir = (str) => {
  let a = 'el-icon-caret-right';
  let b = 'el-icon-caret-bottom';
  if(str === a){
    return b;
  }
  if(str === b){
    return a;
  }
  return false;
};

const isItemClose = (str) => {
  let a = 'el-icon-caret-right';
  let b = 'el-icon-caret-bottom';
  if(str === a){
    return true;
  }else if(str === b){
    return false;
  }
  return null;
};

export default {
  createItem,
  canJson,
  changeArrowDir,
  isItemClose,
}
