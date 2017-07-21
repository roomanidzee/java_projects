package com.romanidze.weatherdata.implementations;

import com.romanidze.weatherdata.entities.WeatherRequest;
import com.romanidze.weatherdata.entities.WeatherRequestBuilder;
import com.romanidze.weatherdata.interfaces.WeatherInterface;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.DailyForecast.Forecast;
import net.aksingh.owmjapis.OpenWeatherMap;
import net.aksingh.owmjapis.OpenWeatherMap.Units;
import org.json.JSONException;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class WeatherImpl implements WeatherInterface{

    @Override
    public String getAPIKey() {
        
        FileInputStream fis = null;
        Properties prop = new Properties();
        
        Path filePath = null;
        
        try{
            
            filePath = Paths.get("src/main/resources/app.properties").toRealPath();
            fis = new FileInputStream(filePath.toFile());
            
            prop.load(fis);           
            
        }catch(IOException e){
            
            System.err.println("Произошла ошибка при получении API-ключа. Проверьте"
                    + " наличие файла с ним");
            
        }
        
        return prop.getProperty("api.key");
        
    }

    @Override
    public List<WeatherRequest> getListOfWeatherRequests(String city, String country, byte forecastDays) {
        
        List<WeatherRequest> resultList = new ArrayList<>();
        
        StringBuilder userRequest = new StringBuilder();
        
        userRequest.append(city)
                   .append(",")
                   .append(country.toUpperCase());
        
        String owmAPIKey = getAPIKey();
        
        Units units = Units.METRIC;
        
        OpenWeatherMap owm = new OpenWeatherMap(units, owmAPIKey);
        
        try{
            
            DailyForecast forecast = owm.dailyForecastByCityName(userRequest.toString(), forecastDays);
            
            int numForecasts = forecast.getForecastCount();
            
            for(int i = 0; i < numForecasts; i++){
                
                Forecast dayForecast = forecast.getForecastInstance(i);
                Forecast.Temperature temperature = dayForecast.getTemperatureInstance();
                WeatherRequestBuilder wrb = new WeatherRequestBuilder();
                
                WeatherRequest wr;
                
                wrb.setCity(city);
                wrb.setCountry(country);
                wrb.setMinTemperature(temperature.getMinimumTemperature());
                wrb.setMaxTemperature(temperature.getMaximumTemperature());
                wrb.setDateTime(dayForecast.getDateTime().toString());
                wrb.setForecastDays((byte) i);
                
                wr = wrb.build();
                
                resultList.add(wr);
            }
            
        }catch(IOException | JSONException e){
            
            System.err.println("Произошла ошибка получения данных. "
                    + "Пожалуйста, проверьте свой код.");
            
        }
        
        return resultList;
        
    }
    
}
