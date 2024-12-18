package com.weatherappdemo.WeatherApp.rest;

import com.weatherappdemo.WeatherApp.exception.WeatherErrorResponse;
import com.weatherappdemo.WeatherApp.exception.WeatherNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class WeatherRestExceptionHandler {
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
