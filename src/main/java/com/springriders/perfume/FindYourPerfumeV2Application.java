package com.springriders.perfume;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"com.springriders.perfume.mapper"})
@SpringBootApplication
public class FindYourPerfumeV2Application {

    public static void main(String[] args) {
        SpringApplication.run(FindYourPerfumeV2Application.class, args);
    }

}
