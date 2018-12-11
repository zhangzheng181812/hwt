package com.zhx.admin.common;

/**
 * 服务端service通用返回对象，这样就不用再new HashMap了
 * 默认code=000 成功，其他失败
 * Created by admin on 2017/12/22.
 */
public class CommonResult<T>{
    private String code;
    private String msg;
    private T data;

    public CommonResult() {

    }

    public static CommonResult success(){
        return new CommonResult("000","成功");
    }

    public static CommonResult success(Object data){
        return new CommonResult("000","成功",data);
    }

    public static CommonResult result(String code, String msg){
        return new CommonResult(code,msg);
    }

    public static <T>CommonResult result(String code, String msg, T data ){
        return new CommonResult(code,msg,data);
    }


    public CommonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuc(){
        return "000".equals(this.code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
