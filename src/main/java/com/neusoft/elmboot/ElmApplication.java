package com.neusoft.elmboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neusoft.elmboot.mapper")
public class ElmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElmApplication.class, args);
	}

}
