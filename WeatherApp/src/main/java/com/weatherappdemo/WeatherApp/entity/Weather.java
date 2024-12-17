package com.weatherappdemo.WeatherApp.entity;

public class Weather {
    private String temperature;
    private String humidity;
    private String city;

    public Weather(String temperature, String humidity, String city) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getCity() {
        return city;
    }

    public String getHumidity() {
        return humidity;
    }


    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
