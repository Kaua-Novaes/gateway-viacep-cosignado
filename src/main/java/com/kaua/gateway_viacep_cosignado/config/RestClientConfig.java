package com.kaua.gateway_viacep_cosignado.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient viaCepRestClient(
            RestClient.Builder builder,
            @Value("${integrations.viacep.base-url}") String baseUrl
    ) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return builder
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }
}
