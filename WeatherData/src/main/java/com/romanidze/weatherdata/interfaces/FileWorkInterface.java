package com.romanidze.weatherdata.interfaces;

import com.romanidze.weatherdata.entities.WeatherRequest;
import java.util.List;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface FileWorkInterface {
    
    void saveToXML(List<WeatherRequest> weatherRequests);//метод для сохранения данных в xml-файл
    
}
