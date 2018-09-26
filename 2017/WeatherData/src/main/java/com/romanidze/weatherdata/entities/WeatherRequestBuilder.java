package com.romanidze.weatherdata.entities;

import java.util.Objects;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class WeatherRequestBuilder {
    
    private String city;//Пояснения аналогичны тем, что в WeatherRequest
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
        
        return new WeatherRequest(this.getCity(), this.getCountry(), this.getMinTemperature(), this.getMaxTemperature(), this.getDateTime(), this.getForecastDays());
        
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        
        hash = 97 * hash + Objects.hashCode(this.getCity());
        hash = 97 * hash + Objects.hashCode(this.getCountry());
        hash = 97 * hash + Float.floatToIntBits(this.getMinTemperature());
        hash = 97 * hash + Float.floatToIntBits(this.getMaxTemperature());
        hash = 97 * hash + Objects.hashCode(this.getDateTime());
        hash = 97 * hash + this.getForecastDays();
        
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
        
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
    public String toString(){
        
       return "WeatherRequestBuilder{city = " + this.getCity() + ", country = " + this.getCountry() 
               + ", minTemperature = " + this.getMinTemperature() + "°C, maxTemperature = " + this.getMaxTemperature()
               + "°C, dateTime = " + this.getDateTime() + ", forecasrDays = " + this.getForecastDays() + "}";
        
    }
    
}
