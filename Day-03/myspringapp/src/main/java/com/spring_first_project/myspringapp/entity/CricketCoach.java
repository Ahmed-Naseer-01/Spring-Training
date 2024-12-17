package com.spring_first_project.myspringapp.entity;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements Coach {
    public CricketCoach() {
        System.out.println("Cricket Coach Constructor called " + this.getClass().getName());
    }
    @Override
    public String getDailyWorkout() {
        return "Practical cricket workout";
    }
    @PostConstruct
    public void doMyStartupStuff() {
        System.out.println("Cricket Coach doMyStartupStuff called");
    }
    @PreDestroy
    public void myEndUpStuff() {
        System.out.println("Cricket Coach myEndUpStuff called");
}
}
