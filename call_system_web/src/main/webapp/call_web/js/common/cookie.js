
/*//读取cookie
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

    if (arr = document.cookie.match(reg))

        return decodeURI(arr[2]);
    else
        return null;
}*/
///获取cookie值
function getCookie(name){

    if (document.cookie.length > 0)
    {
        begin = document.cookie.indexOf(name+"=");
        if (begin != -1)
        {
            begin += name.length+1;//cookie值的初始位置
            end = document.cookie.indexOf(";", begin);//结束位置
            if (end == -1) end = document.cookie.length;//没有;则end为字符串结束位置
            return decodeURI(document.cookie.substring(begin, end)); }
    }

    return null; //不存在返回null

}
//删除cookies
function delCookie(name){
    var myDate=new Date();
    myDate.setTime(-1000);//设置时间
    document.cookie=name+"=''; expires="+myDate.toGMTString();
}

//判断是否已登录
var customerServerName = '';
var customerId = '';
if (getCookie('customerServerId') && getCookie('customerServerName')) {
    customerServerName = getCookie('customerServerName');
    customerId = getCookie('customerServerId');
} else {
    window.location.href = '/call_web/login.html';
}