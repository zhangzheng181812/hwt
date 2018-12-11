package com.zhx;

import com.zhx.admin.authclient.interceptor.LogoutInterceptor;
import com.zhx.admin.authclient.interceptor.OauthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableTransactionManagement
public class AdminApplication implements WebMvcConfigurer {

	@Autowired
	private OauthInterceptor oauthInterceptor;
	@Autowired
	private LogoutInterceptor logoutInterceptor;


	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
	/**
	 * 配置拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(oauthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/quill_editor/**")
                .excludePathPatterns("/element-ui/**");
        //退出
        registry.addInterceptor(logoutInterceptor)
                .addPathPatterns("/logout");

	}
}
