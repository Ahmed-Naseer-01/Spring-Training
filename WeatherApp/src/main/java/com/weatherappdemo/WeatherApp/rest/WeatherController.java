package com.weatherappdemo.WeatherApp.rest;

import com.weatherappdemo.WeatherApp.entity.Weather;
import com.weatherappdemo.WeatherApp.exception.WeatherErrorResponse;
import com.weatherappdemo.WeatherApp.exception.WeatherNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        throw new WeatherNotFoundException("Weather not found for city : " + city);
    }
    // here we'll Exception handler
    //     this is the type of response body                    Exception type to handle
    @ExceptionHandler
    public ResponseEntity<WeatherErrorResponse> handleException(WeatherNotFoundException ex) {

        // create a weatherErrorResponse
        WeatherErrorResponse errorResponse = new WeatherErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND) ;
    }
    @ExceptionHandler
    public ResponseEntity<WeatherErrorResponse> handleException(Exception ex) {

        // create a weatherErrorResponse
        WeatherErrorResponse errorResponse = new WeatherErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());


        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST) ;
    }

}
