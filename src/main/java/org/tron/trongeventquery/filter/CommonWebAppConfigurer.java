package org.tron.trongeventquery.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebAppConfigurer  implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        InterceptorRegistration r1 = registry.addInterceptor(new CommonInterceptor());

        // addPathPatterns 添加拦截
        r1.addPathPatterns("/*");

        // excludePathPatterns 排除拦截
//        r1.excludePathPatterns("/login");

    }
}
