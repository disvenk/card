package com.resto.msgc.backend.card.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by bruce on 2017/12/7.
 */
@Configuration
public class WebAppConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor localInterceptor;

    public WebAppConfiguration() {
        super();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/assets/");
        super.addResourceHandlers(registry);
    }

//    @Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/jsp/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).
                addPathPatterns("/**").
                excludePathPatterns("/index").
                excludePathPatterns("/login").
                excludePathPatterns("/druid/*").
                excludePathPatterns("/swagger*").
                excludePathPatterns("/sendCode/1/*").
                excludePathPatterns("/assets/*").
                excludePathPatterns("/newPosApi/*").
                excludePathPatterns("/*.html").
                excludePathPatterns("/tongbu/**");
    }

}