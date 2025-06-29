package astr.api.configuration;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${nasa.api.key}")
    private String apiKey;

    @Value("${nasa.base-url}")
    private String baseUrl;

    @Bean
    public WebClient webClient(){

        //Timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        //Filtro para adicionar a api key automaticamente
        ExchangeFilterFunction apiKeyFilter = ExchangeFilterFunction.ofRequestProcessor(request -> {
            URI updatedUri = UriComponentsBuilder.fromUri(request.url())
                    .queryParam("api_key", apiKey) // <-- Garanta que é "api_key", não "nasa.api.key"
                    .build(true)
                    .toUri();

            ClientRequest updatedRequest = ClientRequest.from(request).url(updatedUri).build();

            return Mono.just(updatedRequest);
        });

        //Contruindo o WebClient
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type","application/json")
                .defaultHeader("Accept", "application/json")
                .filter(apiKeyFilter)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(512 * 1024))
                .build();


    }
}
