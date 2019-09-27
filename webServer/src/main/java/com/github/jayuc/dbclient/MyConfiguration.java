package com.github.jayuc.dbclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置对跨域请求的支持
 * @author yujie
 *
 */

@SuppressWarnings("deprecation")
@Configuration
public class MyConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	ApplicationContext ac;

	@Override  
    public void addCorsMappings(CorsRegistry registry) { 
		// 是否时开发模式 ，还是生产模式
		String active = ac.getEnvironment().getActiveProfiles()[0];
		if("dev".equals(active)) {
			registry.addMapping("/**")  
            .allowCredentials(true)  
            .allowedOrigins("*")  
            .maxAge(3600)
            .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
		}
    } 
	
}
