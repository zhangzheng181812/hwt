var Common = {
    //EasyUI用DataGrid用日期格式化
    TimeFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        /*json格式时间转js时间格式*/
        value = value.substr(1, value.length - 2);
        var obj = eval('(' + "{Date: new " + value + "}" + ')');
        var dateValue = obj["Date"];
        if (dateValue.getFullYear() < 1900) {
            return "";
        }
        var val = dateValue.format("yyyy-mm-dd HH:MM");//控制格式
        return val.substr(11, 5);
    }

};


// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


//格式化数字金额
function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}

//统一提交前确认
function Confirm(msg,obj) {
    $.messager.confirm("操作提示", msg, function (r) {
        if (r) {
            if ($("#"+obj).form('validate') == false) {
                return false;
            }
            $("#"+obj).submit();
        }
    });
}

function tishi(data,execute) {
    $('body').append(
        '<div class="all-alert">'+
        '<div class="all-alert-container">'+
        '<p class="all-alert-words">'+
        data+
        '</p>'+
        '<div class="all-alert-btn-c clear">'+
        '<div class="fl all-alert-btn-xq">'+
        '取消'+
        '</div>'+
        '<div class="fr all-alert-btn-qd">'+
        '确定'+
        "</div>"+
        '</div>'+
        '</div>'+
        '</div>'
    )
    $('body').css('overflow', 'hidden');
    $('.all-alert-btn-qd').on('click', function () {
        $('body').css('overflow', 'visible');
        $('.all-alert').remove();
        if(execute){
            execute();
        }
    });
    $('.all-alert-btn-xq').click(function () {
        $('body').css('overflow', 'visible');
        $('.all-alert').remove();
    })

}
function tishi2(data,execute) {
    $('body').append(
        '<div class="all-alert">'+
        '<div class="all-alert-container">'+
        '<p class="all-alert-words center">'+
        data+
        '</p>'+
        '<div class="all-alert-btn-c clear">'+
        '<div class="all-alert-btn-qd pos-center">'+
        '确定'+
        "</div>"+
        '</div>'+
        '</div>'+
        '</div>'
    )
    $('body').css('overflow', 'hidden');
    $('.all-alert-btn-qd').on('click', function () {
            $('body').css('overflow', 'visible');
            $('.all-alert').remove();
            if(execute){
                execute();
            }
        });
    $('.all-alert-btn-xq').click(function () {
        $('body').css('overflow', 'visible');
        $('.all-alert').remove();
        // if(after){
        //     after();
        // }
    })

}
function post(url,params){
    var form = $("<form method='post'></form>");
    var input;
    form.attr({"action":url});
    $.each(params,function(key,value){
        input = $("<input type='hidden'>");
        input.attr({"name":key});
        input.val(value);
        form.append(input);
    });
    $(document.body).append(form);
    form.submit();
}

//通过随机数强制刷新页面（解决微信安卓版reload失效问题）
var reload = function(){
    var hash = +(new Date());
    var new_search = (/wechat_hash/).test(location.search) ?
        // 如果之前有添加过指纹，就更新它
        location.search.replace(/wechat_hash=\d+(&?)/,'wechat_hash=' + hash + '$1') :
        location.search == "" ?
            // 如果 search 为空
        '?wechat_hash=' + hash :
            // 如果 search 不为空
        location.search + '&wechat_hash=' + hash;
    // 修改浏览器历史
    var current_title = document.title;
    var new_uri = location.origin + location.pathname + new_search;
    history.replaceState(null, current_title, new_uri);
    // 重新加载页面
    location.reload(true);
};

function warnMessage(txt) {
    // console.log('warn');
    var showDiv = document.createElement("div");
    showDiv.id

        = "showDiv";
    showDiv.innerHTML = '<div class="showTxt1"><div class="showdiv"><span>' + txt + '</span></div></div>';
    document.getElementsByTagName("body").item(0).appendChild(showDiv);
    var sTime = setTimeout(function () {
        document.body.removeChild(document.getElementById("showDiv"));
    }, 2000);
}