package com.ctc.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@MapperScan("com.ctc.sd.dao")
public class NetworkSupportApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NetworkSupportApplication.class, args);

	}
}
