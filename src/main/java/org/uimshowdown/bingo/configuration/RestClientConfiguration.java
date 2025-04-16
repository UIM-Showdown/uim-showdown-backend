package org.uimshowdown.bingo.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
 
    @Bean
    @Qualifier("templeOsrsClient")
    public RestClient templeOsrsClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://templeosrs.com")
                .build();
    }

}
