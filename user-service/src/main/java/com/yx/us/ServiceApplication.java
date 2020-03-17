package com.yx.us;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ImportResource(locations = {"classpath:provider.xml"})
@MapperScan("com.yx.us.mapper")
public class ServiceApplication {

    public static void main(String[] args) {
        //表示非web项目
//        new SpringApplicationBuilder(ServiceApplication.class).web(WebApplicationType.NONE).run();
        SpringApplication.run(ServiceApplication.class);
    }
}
