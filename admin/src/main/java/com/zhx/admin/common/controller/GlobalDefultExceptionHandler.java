package com.zhx.admin.common.controller;

import com.zhx.admin.common.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SimonHu
 * @Description:
 * @Created on 2018/6/25 10:15
 */
@ControllerAdvice
public class GlobalDefultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalDefultExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult defultExcepitonHandler(HttpServletRequest request, Exception e) {
        logger.error("---全局错误信息---：",e);
        return CommonResult.result("500","系统运行异常！");
    }
}
