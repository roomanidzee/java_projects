package com.romanidze.weatherdata.entities;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class WeatherRequestBuilder {
    
    private String city;
    private String country;
    private float minTemperature;
    private float maxTemperature;
    private String dateTime;
    private byte forecastDays;

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @param minTemperature the minTemperature to set
     */
    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * @param maxTemperature the maxTemperature to set
     */
    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @param forecastDays the forecastDays to set
     */
    public void setForecastDays(byte forecastDays) {
        this.forecastDays = forecastDays;
    }
    
    public WeatherRequest build(){
        
        return new WeatherRequest(this.city, this.country, this.minTemperature,
                                  this.maxTemperature, this.dateTime, this.forecastDays);
        
    }
    
}
