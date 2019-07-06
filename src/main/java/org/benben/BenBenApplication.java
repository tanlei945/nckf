package org.benben;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@ServletComponentScan
public class BenBenApplication {

    public static void main(String[] args) {
    	System.setProperty("spring.devtools.restart.enabled", "false");
    	SpringApplication.run(BenBenApplication.class, args);
    }
}