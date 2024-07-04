package ai.spring.demo_ai;

import ai.spring.demo_ai.functioncalling.WeatherConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(WeatherConfigProperties.class)
@SpringBootApplication
public class DemoAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAiApplication.class, args);
    }
}
