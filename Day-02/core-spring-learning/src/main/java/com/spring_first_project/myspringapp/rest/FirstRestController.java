package com.spring_first_project.myspringapp.rest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstRestController {

//    injecting property in the rest controller ( this property is define in the application.property file)
    @Value("${coach.name}")
    private String coachName;
    @Value("${team.name}")
    private String teamName;
//    inject custom properties
    @GetMapping("/teamInfo")
    public String teaminfo()
    {
        return "Team name : " + teamName + ", Coach name : " + coachName;
    }
    @GetMapping("/")
    public String message() { return "hello from Rest Controller"; }
    @GetMapping("/page-01")
    public String message1() { return "hello from page 01"; }
}
