package com.spring_first_project.myspringapp.entity;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class HockeyCoach implements Coach {
    public HockeyCoach() {
        System.out.println("HockeyCoach constructor called" + this.getClass().getName());
    }
    @Override
    public String getDailyWorkout() {
        return "Practice for hockey coach";
    }
}
