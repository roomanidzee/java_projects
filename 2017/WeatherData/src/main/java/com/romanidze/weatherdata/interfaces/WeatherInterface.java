package com.romanidze.weatherdata.interfaces;

import com.romanidze.weatherdata.entities.WeatherRequest;
import java.util.List;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface WeatherInterface {
    
    String getAPIKey();
    List<WeatherRequest> getListOfWeatherRequests(String city, String country, byte forecastDays);
    
}
