package ai.spring.demo_ai.functioncalling;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    private final RestClient restClient;
    private final WeatherConfigProperties weatherProps;

    public WeatherService(WeatherConfigProperties props) {
        this.weatherProps = props;
        System.out.println("Weather API URL: {}"+ weatherProps.apiUrl());
        System.out.println("Weather API Key: {}"+ weatherProps.apiKey());
        this.restClient = RestClient.create(weatherProps.apiUrl());
    }

    @Override
    public Response apply(WeatherService.Request weatherRequest) {
        System.out.println("Weather Request: {}"+weatherRequest);
//        Response response = restClient.get()
//                .uri("/current.json?key={key}&q={q}", weatherProps.apiKey(), weatherRequest.city())
//                .retrieve()
//                .body(Response.class);
//        System.out.println("Weather API Response: {}"+response);
//        return response;
        try {
            Response response = restClient.get()
                    .uri("/current.json?key={key}&q={q}", weatherProps.apiKey(), weatherRequest.city())
                    .retrieve()
                    .body(Response.class);
            return response;
        } catch (RestClientException e) {
            // Handle the case where the response is not JSON
            System.err.println("Error retrieving weather data: " + e.getMessage());
            // Return a default or error response
            return new Response(new Location("N/A", "N/A", "N/A", 0L, 0L),
                    new Current("N/A", new Condition("Error"), "N/A", "N/A"));
        }
    }

    // mapping the response of the Weather API to records.
    public record Request(String city) {}
    public record Response(Location location,Current current) {}
    public record Location(String name, String region, String country, Long lat, Long lon){}
    public record Current(String temp_f, Condition condition, String wind_mph, String humidity) {}
    public record Condition(String text){}

}
