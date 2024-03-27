package fr.hoenheimsports.instagramservice;

import fr.hoenheimsports.instagramservice.config.FirebaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(FirebaseProperties.class)
public class InstagramServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstagramServiceApplication.class, args);
    }

}
