package org.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 관리자 페이지 기본 경로
        registry.addViewController("/admin").setViewName("redirect:/admin/main");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스를 최우선으로 처리 (Security보다 먼저)
        registry.addResourceHandler("/assets/**", "/*.js", "/*.css", "/*.ico", "/*.png", "/*.jpg", "/*.svg")
                .addResourceLocations("classpath:/static/assets/", "classpath:/static/")
                .setCachePeriod(86400);  // 1일 캐시
                
        // API 경로가 아닌 모든 요청을 React 앱으로 포워딩 (SPA 지원)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        
                        // API, admin, actuator 경로는 제외
                        if (resourcePath.startsWith("api/") || 
                            resourcePath.startsWith("open-api/") || 
                            resourcePath.startsWith("store-api/") || 
                            resourcePath.startsWith("store-open-api/") ||
                            resourcePath.startsWith("admin/") ||
                            resourcePath.startsWith("actuator/") ||
                            resourcePath.startsWith("sse/")) {
                            return null;
                        }
                        
                        // 요청된 리소스가 존재하면 반환
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }
                        
                        // 그렇지 않으면 index.html 반환 (SPA 라우팅)
                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}
