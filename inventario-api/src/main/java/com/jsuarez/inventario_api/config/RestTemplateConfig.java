package com.jsuarez.inventario_api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        log.debug("Configurando RestTemplate para comunicación entre microservicios");
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // Configurar timeouts
        factory.setConnectTimeout(5000); // 5 segundos para conectar
        factory.setReadTimeout(10000);   // 10 segundos para leer respuesta
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        log.info("RestTemplate configurado con timeouts - Connect: 5s, Read: 10s");
        
        return restTemplate;
    }
}