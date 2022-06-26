package com.teethcare.config.googlemap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teethcare.model.dto.APIKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Getter
@Setter
public class GoogleMapConfig {
    @Value("${googlemap.key}")
    private String keyJson;

    @Bean
    public String getAPIKey() throws IOException {
        InputStream inputStream = new ClassPathResource(keyJson).getInputStream();
        APIKeyDTO key = new ObjectMapper().readValue(inputStream, APIKeyDTO.class);
        return key.getValue();
    }
}
