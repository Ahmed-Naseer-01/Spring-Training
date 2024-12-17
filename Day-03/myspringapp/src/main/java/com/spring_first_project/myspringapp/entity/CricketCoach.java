package com.spring_first_project.myspringapp.entity;

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
}
