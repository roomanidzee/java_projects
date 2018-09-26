package com.romanidze.weatherdata.processing;

import com.romanidze.weatherdata.entities.WeatherRequest;
import com.romanidze.weatherdata.implementations.FileWorkImpl;
import com.romanidze.weatherdata.implementations.WeatherImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class LaunchFunction {
    
    public static void launch(){
        
        FileWorkImpl fw = new FileWorkImpl();
        WeatherImpl wi = new WeatherImpl();
        
        List<WeatherRequest> dataList = new ArrayList<>();
        
        System.out.println("Введите, пожалуйста, название города, страны,"
                + " и количество дней для прогноза: ");
        System.out.println();
        
        Scanner sc1 = new Scanner(System.in);
        Scanner sc2 = new Scanner(System.in);
        Scanner sc3 = new Scanner(System.in);
        
        String city = sc1.nextLine();
        String country = sc2.nextLine();
        byte days = sc3.nextByte();
        
        dataList.addAll(wi.getListOfWeatherRequests(city, country, days));
        
        for(int i = 0; i < dataList.size(); i++){
            
            System.out.println(dataList.get(i));
            
        }
        
        fw.saveToXML(dataList);
        
    }
    
}
