package com.spring_boot_final.metasumer;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 프로젝트 외부 경로 이미지 맵핑 설정 
		// 맵핑 이름 : project_images
		registry.addResourceHandler("/project_images/**")

		//.addResourceLocations("file:///usr/local/project/product_images");
		.addResourceLocations("file:///D:/springWorkspace/metasumer_images/");
		registry.addResourceHandler("/ocr_image/**")
		//.addResourceLocations("file:///usr/local/project/product_images/upload");
		.addResourceLocations("file:///D:/springWorkspace/metasumer_images/upload/");
		registry.addResourceHandler("/stt_audio/**")
		//.addResourceLocations("file:///usr/local/project/upload/");
		.addResourceLocations("file:///D:/springWorkspace/upload/");
		registry.addResourceHandler("/metasumer_images/**")
		//.addResourceLocations("file:///usr/local/project/metasumer_images/");
		.addResourceLocations("file:///D:/springWorkspace/metasumer_images/");
	}
}
