package config;


import com.spring_first_project.myspringapp.entity.Coach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javabean.SwimCoach;

@Configuration
public class SportConfig {
    @Bean
    public Coach swimCoach()
    {
        return new SwimCoach();
    }
}
