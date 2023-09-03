package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchConfig {
    
    @Bean("pointsMap") 
    public Map<String, Integer> pointsMap() {
        Map<String, Integer> pointsMap = new HashMap<>();
        return pointsMap;
    } 
}
