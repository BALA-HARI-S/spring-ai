package ai.spring.demo_ai.functioncalling;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "weather")
public record WeatherConfigProperties(String apiKey, String apiUrl) {
}
