package com.spring_boot_final.metasumer;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) 
@ComponentScan(basePackages = {"com.spring_boot_final.metasumer"})
@MapperScan(basePackages = {"com.spring_boot_final.metasumer"})
@PropertySources({ //DB 연결 정보 및 각 API 키 값 : 프로퍼티 사용 (로컬 경로 / 서버 경로)
		@PropertySource(value = {"file:c:/springWorkspace/configure.properties",
												  "file:/usr/local/project/properties/configure.properties"},
								    ignoreResourceNotFound=true)
})
public class SpringBootProjectFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootProjectFinalApplication.class, args);
	}

}
