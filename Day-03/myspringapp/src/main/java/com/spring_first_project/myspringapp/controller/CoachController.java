package com.spring_first_project.myspringapp.controller;
import com.spring_first_project.myspringapp.entity.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class CoachController {

     private final Coach newcoach;
    @Autowired
     public CoachController(@Qualifier("swimCoach")Coach theNewcoach) {
        this.newcoach = theNewcoach;
        System.out.println("coachController called " + this.getClass().getName());
    }
    @GetMapping("/dailyworkout")
    public String getDailyWorkout() { return newcoach.getDailyWorkout(); }
}
