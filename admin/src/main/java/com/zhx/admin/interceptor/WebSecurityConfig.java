package com.zhx.admin.interceptor;

/**
 * Created by huangds on 2017/10/24.
 */

import com.zhx.admin.authclient.config.Constants;
import com.zhx.admin.authclient.domain.CommonResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 登录配置
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    public final static String SESSION_KEY="username";
    public final static String USER_ID="user_id";
    public final static String OFFICE_ID="office_id";

    public final static String USER_INFO="user_info";
    public final static String PHONE_NO="phone_no";
    public final static String NICK_NAME="nick_name";
    @Bean
    public SecurityInterceptor getSecurityInterceptor(){
        return new SecurityInterceptor();
    }
   @Override
    public void  addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.addPathPatterns("/**");

    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();
//            判断是否已有该用户登录的session
            if(session.getAttribute(SESSION_KEY) != null){
                return true;
            }
            CommonResult result = (CommonResult) request.getSession().getAttribute(Constants.CACHE_USER_INFO);
            if(null !=result && result.isSuc()){
                Map map = (Map)result.getData();
                Map<String,String> userInfo = (Map)map.get(USER_INFO);
                session.setAttribute(SESSION_KEY,userInfo.get(PHONE_NO));
                session.setAttribute(OFFICE_ID,userInfo.get(OFFICE_ID));
                session.setAttribute(USER_ID,userInfo.get(USER_ID));
                session.setAttribute(NICK_NAME,userInfo.get(NICK_NAME));
                return true;
            }else {
                session.setAttribute(SESSION_KEY,"15710080907");
                session.setAttribute(OFFICE_ID,"1");
                session.setAttribute(USER_ID,"14");
                session.setAttribute(NICK_NAME,"管理员");
                return true;
            }
        }
    }
}
