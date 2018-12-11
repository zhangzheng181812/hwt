/**
 * Created by admin on 2018/4/26.
 */
/*金额仅能输入数字和小数点*/
var precapital;
var precapital1;
var precapital2;
document.querySelector('.capital').addEventListener('focus', function() {
    if (this.value == '0.00') {
        this.value = '';
    } else {
        this.value = this.value.replace(/\.00/, '').replace(/(\.\d)0/,'$1');
    }
    precapital = this.value;
});
document.querySelector('.capital').addEventListener('keyup', function() {
    this.value = this.value.replace(/^[\.]/, '').replace(/[^\d.]/g, '');
    if (this.value.split(".").length - 1 > 1) {
        this.value = precapital;
    }
    precapital = this.value;
});
document.querySelector('.capital').addEventListener('blur', function() {
    this.value = this.value.replace(/[\.]$/, '');
    this.value = this.value.replace(/(.*)\.([\d]{2})(\d*)/g,'$1.$2');
    this.value = Number(this.value).toFixed(2);
    if (this.value == '0.00') {
        this.value = '';
    }
});
document.querySelector('.capital1').addEventListener('focus', function() {
    if (this.value == '0.00') {
        this.value = '';
    } else {
        this.value = this.value.replace(/\.00/, '').replace(/(\.\d)0/,'$1');
    }
    precapital1 = this.value;
});
document.querySelector('.capital1').addEventListener('keyup', function() {
    this.value = this.value.replace(/^[\.]/, '').replace(/[^\d.]/g, '');
    if (this.value.split(".").length - 1 > 1) {
        this.value = precapital1;
    }
    precapital1 = this.value;
});
document.querySelector('.capital1').addEventListener('blur', function() {
    this.value = this.value.replace(/[\.]$/, '');
    this.value = this.value.replace(/(.*)\.([\d]{2})(\d*)/g,'$1.$2');
    this.value = Number(this.value).toFixed(2);
    if (this.value == '0.00') {
        this.value = '';
    }
});document.querySelector('.capital2').addEventListener('focus', function() {
    if (this.value == '0.00') {
        this.value = '';
    } else {
        this.value = this.value.replace(/\.00/, '').replace(/(\.\d)0/,'$1');
    }
    precapital2 = this.value;
});
document.querySelector('.capital2').addEventListener('keyup', function() {
    this.value = this.value.replace(/^[\.]/, '').replace(/[^\d.]/g, '');
    if (this.value.split(".").length - 1 > 1) {
        this.value = precapital2;
    }
    precapital2 = this.value;
});
document.querySelector('.capital2').addEventListener('blur', function() {
    this.value = this.value.replace(/[\.]$/, '');
    this.value = this.value.replace(/(.*)\.([\d]{2})(\d*)/g,'$1.$2');
    this.value = Number(this.value).toFixed(2);
    if (this.value == '0.00') {
        this.value = '';
    }
});