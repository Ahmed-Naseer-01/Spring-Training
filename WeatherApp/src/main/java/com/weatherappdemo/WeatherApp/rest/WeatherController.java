package com.weatherappdemo.WeatherApp.rest;

import com.weatherappdemo.WeatherApp.entity.Weather;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/myapi")
public class WeatherController {
   private List<Weather> weathers;


    @PostConstruct
    public void loadWeathers() {
        weathers = new ArrayList<>();
        weathers.add(new Weather("90", "89", "lahore"));
        weathers.add(new Weather("55", "83", "Gujranwala"));
        weathers.add(new Weather("22", "89", "isb"));
        weathers.add(new Weather("94", "89", "fsd"));
    }

    // list of weathers
    @GetMapping("/weathers")
    public List<Weather> getWeathers() {
        return weathers;
    }


    // get weather of specific city
    @GetMapping("/weathers/{city}")
    public Weather getWeather(@PathVariable String city) {
        for (Weather weather : weathers) {
            if (weather.getCity().equals(city)) {
                return weather;
            }
        }
    return null;
    }
}
