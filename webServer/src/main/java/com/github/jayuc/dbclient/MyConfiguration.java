package com.github.jayuc.dbclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置对跨域请求的支持
 * @author yujie
 *
 */

@Configuration
public class MyConfiguration extends WebMvcConfigurationSupport {
	
	@Autowired
	ApplicationContext ac;

	@Override  
    public void addCorsMappings(CorsRegistry registry) { 
		// 是否时开发模式 ，还是生产模式
		String active = ac.getEnvironment().getActiveProfiles()[0];
		if("dev".equals(active)) {
			registry.addMapping("/**")
					.allowedMethods("*")
					.allowedOrigins("*")
					.allowedHeaders("*");
			super.addCorsMappings(registry);
		}
    } 
	
}
