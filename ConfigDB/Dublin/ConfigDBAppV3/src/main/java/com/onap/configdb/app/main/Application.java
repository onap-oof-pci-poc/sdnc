package com.onap.configdb.app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

//param

@ComponentScan("com.onap.*")
@EntityScan(basePackages = {"com.onap.configdb.db.domain","com.onap.configdb.application.main"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
