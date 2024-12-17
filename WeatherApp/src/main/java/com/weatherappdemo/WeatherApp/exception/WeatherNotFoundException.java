package com.weatherappdemo.WeatherApp.exception;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException() {
    }

    public WeatherNotFoundException(String message) {
        super(message);
    }

    public WeatherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherNotFoundException(Throwable cause) {
        super(cause);
    }
}
