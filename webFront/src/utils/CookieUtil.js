/**
 * 设置cookie
 * @param name cookie名
 * @param value cookie值
 * @param days  cookie存活时间
 */
function setCookie(name, value, days) {
  let d = new Date();
  d.setTime(d.getTime() + (days * 24 * 60 * 60 * 1000));
  let expires = "expires=" + d.toUTCString();
  document.cookie = name + "=" + value + "; " + expires+"; path=/";  //这个很重要代表在那个层级下可以访问cookie
}

/**
 * 获取cookie
 * @param name cookie名
 * @returns {string|null}
 */
function getCookie(name) {
  let cname = name + "=";
  let ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while(c.charAt(0) == ' ') c = c.substring(1);
    if(c.indexOf(cname) != -1) return c.substring(cname.length, c.length);
  }
  return null;
}

/**
 * 清除cookie
 * @param name
 */
function clearCookie(name) {
  setCookie(name, "", -1);
}

export default {
    setCookie,
    getCookie,
    clearCookie
}
