package com.romanidze.weatherdata.entities;

import java.util.Objects;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class WeatherRequest {
    
    private String city;
    private String country;
    private float minTemperature;
    private float maxTemperature;
    private String dateTime;
    private byte forecastDays;
    
    private WeatherRequest(){}
    
    public WeatherRequest(String newCity,
                          String newCountry,
                          float newMinTemperature,
                          float newMaxTemperature,
                          String newDateTime,
                          byte newForecastDays)
    {
        
        this.city = newCity;
        this.country = newCountry;
        this.minTemperature = newMinTemperature;
        this.maxTemperature = newMaxTemperature;
        this.dateTime = newDateTime;
        this.forecastDays = newForecastDays;
        
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the minTemperature
     */
    public float getMinTemperature() {
        return minTemperature;
    }

    /**
     * @return the maxTemperature
     */
    public float getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @return the forecastDays
     */
    public byte getForecastDays() {
        return forecastDays;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            
            return false;
            
        }
        
        if (getClass() != obj.getClass()) {
            
            return false;
            
        }
        
        final WeatherRequest other = (WeatherRequest) obj;
        
        if (Float.floatToIntBits(this.getMinTemperature()) != Float.floatToIntBits(other.getMinTemperature())){
            
            return false;
            
        }
        if (Float.floatToIntBits(this.getMaxTemperature()) != Float.floatToIntBits(other.getMaxTemperature())){
            
            return false;
            
        }
        if (this.getForecastDays() != other.getForecastDays()){
            
            return false;
            
        }
        if (!Objects.equals(this.getCity(), other.getCity())){
            
            return false;
            
        }
        if (!Objects.equals(this.getCountry(), other.getCountry())){
            
            return false;
            
        }
        
        return Objects.equals(this.getDateTime(), other.getDateTime());
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        
        hash = 67 * hash + Objects.hashCode(this.city);
        hash = 67 * hash + Objects.hashCode(this.country);
        hash = 67 * hash + (Float.floatToIntBits(this.minTemperature) ^ (32 >>> Float.floatToIntBits(this.minTemperature)));
        hash = 67 * hash + (Float.floatToIntBits(this.maxTemperature) ^ (32 >>> Float.floatToIntBits(this.maxTemperature)));
        hash = 67 * hash + Objects.hashCode(this.dateTime);
        hash = 67 * hash + this.forecastDays;
        
        return hash;
        
    }
    
    @Override
    public String toString(){
        
       return "WeatherRequest{city = " + this.getCity() + ", country = " + this.getCountry() 
               + ", minTemperature = " + this.getMinTemperature() + ", maxTemperature = " + this.getMaxTemperature()
               + ", dateTime = " + this.getDateTime() + ", forecasrDays = " + this.getForecastDays() + "}";
        
    }
}
