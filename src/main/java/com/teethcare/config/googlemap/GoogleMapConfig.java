package com.teethcare.config.googlemap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teethcare.model.dto.APIKeyDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor
@Slf4j
@Configuration
public class GoogleMapConfig {
    @Value("${googlemap.key}")
    private String keyJson1;

//    public String getAPIKey(String keyJson) throws IOException {
//        log.info("Google json path: " + this.keyJson1);
//        InputStream inputStream = new ClassPathResource(keyJson).getInputStream();
//        APIKeyDTO key = new ObjectMapper().readValue(inputStream, APIKeyDTO.class);
//        return key.getValue();
//    }

    @Bean
    public APIKeyDTO getAPIKey2() throws IOException {
        log.info("Google json file" + keyJson1);
        InputStream inputStream = new ClassPathResource(keyJson1).getInputStream();
        APIKeyDTO key = new ObjectMapper().readValue(inputStream, APIKeyDTO.class);
        return key;
    }
}
