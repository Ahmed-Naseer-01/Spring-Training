package javabean;

import com.spring_first_project.myspringapp.entity.Coach;

public class SwimCoach implements Coach {

    public SwimCoach() {
        System.out.println("SwimCoach constructor called");
    }

    @Override
    public String getDailyWorkout() {
        return "swim up";
    }
}
