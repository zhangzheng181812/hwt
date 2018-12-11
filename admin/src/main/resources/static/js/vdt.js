var vdt = {//自定义校验
    //邮箱校验
    email: function (value) {
        if (value == '' || value == null || value == undefined) {
            vue.$message({
                message: '邮箱不能为空！',
                type: 'warning'
            });
            return false;
        }
        let reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        if (!reg.test(value)) {
            vue.$message({
                message: '邮箱格式不正确！',
                type: 'warning'
            });
            return false;
        }
    },
    //非空校验
    notNull: function (name, value) {
        if (value == '' || value == null || value == undefined) {
            vue.$message({
                message: name + '不能为空！',
                type: 'warning'
            });
            return false;
        }
    },
    //非空校验
    notNull2: function (value) {
        if (value == '' || value == null || value == undefined) {
            return false;
        }
    },
    //手机号校验
    phone: function (value) {
        if (value == '' || value == null || value == undefined) {

            vue.$message({
                message: '手机不能为空！',
                type: 'warning'
            });
            return false;
        }
        let reg = /^1[0-9]{10}$/;
        if (!reg.test(value)) {
            vue.$message({
                message: '手机格式不正确！',
                type: 'warning'
            });
            return false;
        }
    },
    //数字校验
    numberFormat: function (name, value) {
        if (value == '' || value == null || value == undefined) {
            vue.$message({
                message: name + '不能为空！',
                type: 'warning'
            });
            return false;
        }
        var reg = /^[0-9]*$/;
        if (!reg.test(value)) {
            vue.$message({
                message: name + '必须为数字！',
                type: 'warning'
            });
            return false;
        }
    },
    //价格校验
    priceFormat: function (name, value) {
        if (value == '' || value == null || value == undefined) {
            vue.$message({
                message: name + '不能为空！',
                type: 'warning'
            });
            return false;
        }
        // 0.01-99999999.99
        var reg = /(?!^0+\.0{0,2}$)(^\d+$|^\d+\.\d{2}$)/;
        if (!reg.test(value)) {
            vue.$message({
                message: name + '格式不正确，保留两位小数且必须大于0.00！',
                type: 'warning'
            });
            return false;
        }
    },
    /**
     * 格式化数字1转为001
     * @param num
     * @param length
     * @returns {string|*}
     */
     formatNum : function(num,length){
        var numstr = num.toString();
        var l=numstr.length;
        if (numstr.length>=length) {return numstr;}
        for(var  i = 0 ;i<length - l;i++){
            numstr = "0" + numstr;
        }
        return numstr;
    },
    //正数校验
    positiveNumber: function (value) {
        if (value == '' || value == null || value == undefined) {
            return false;
        }
        var reg = /(?!^0+\.0{0,2}$)(^\d+$|^\d+\.\d{2}$)/;
        if(reg.test(value)){
            return true;
        }else {
            return false ;
        }
    }
}