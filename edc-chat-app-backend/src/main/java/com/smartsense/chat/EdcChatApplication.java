package com.smartsense.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = { "com.smartsense", "com.smartsensesolutions" })
@ConfigurationPropertiesScan
@EnableFeignClients
@EnableAsync
public class EdcChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdcChatApplication.class, args);
    }

}
