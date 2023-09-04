package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchConfig {

    /* 
     * Bean that is used as in memory storage for receipt IDs and their respective point value
     */
    @Bean("pointsMap") 
    public Map<String, Integer> pointsMap() {
        Map<String, Integer> pointsMap = new HashMap<>();
        return pointsMap;
    } 
}
