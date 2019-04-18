package com.mobvista.okr;

import com.mobvista.okr.config.ApplicationProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author guwei
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableAsync
@MapperScan("com.mobvista.okr.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
