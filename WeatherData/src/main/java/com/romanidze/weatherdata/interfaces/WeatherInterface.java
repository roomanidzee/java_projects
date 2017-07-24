package com.romanidze.weatherdata.interfaces;

import com.romanidze.weatherdata.entities.WeatherRequest;
import java.util.List;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface WeatherInterface {
    
    String getAPIKey();//получение api-ключа от сервиса OpenWeatherMap
    //получение прогнозов по запросу пользователя
    List<WeatherRequest> getListOfWeatherRequests(String city, String country, byte forecastDays);
    
}
