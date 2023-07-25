package src.quiz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

@Configuration
public class CustomConfig {
    @Bean("objectMapper")
    @ApplicationScope
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
