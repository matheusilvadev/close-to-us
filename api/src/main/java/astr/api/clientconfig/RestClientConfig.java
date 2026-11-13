package astr.api.clientconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient nasaRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.nasa.gov") // Já define a URL base aqui
                .build();
    }
}
