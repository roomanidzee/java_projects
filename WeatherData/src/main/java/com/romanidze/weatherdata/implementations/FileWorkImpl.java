package com.romanidze.weatherdata.implementations;

import com.romanidze.weatherdata.entities.WeatherRequest;
import com.romanidze.weatherdata.interfaces.FileWorkInterface;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class FileWorkImpl implements FileWorkInterface{

    @Override
    public void saveToXML(List<WeatherRequest> weatherRequests) {
        
        Path filePath = Paths.get("src/main/resources/xmls", weatherRequests.get(0).getCity() + "_results.xml");
        
        if(!Files.exists(filePath)){
            
            try {
                
                Files.createFile(filePath);
                
            } catch (IOException ex) {
                
                System.err.println("Произошла ошибка во время создания файла");
                
            }
            
        }
        
        try{
            
            Document document = DocumentBuilderFactory.newInstance()
                                                      .newDocumentBuilder()
                                                      .newDocument();        
            
            Element results = document.createElement("results");
            document.appendChild(results);
            
            Element country = document.createElement("country");
            country.setTextContent(weatherRequests.get(0).getCountry());
            results.appendChild(country);
            
            for(int i = 0; i < weatherRequests.size(); i++){
                
                Element requestNumber = document.createElement("request_number");
                requestNumber.setAttribute("id", Integer.toString(weatherRequests.get(i).getForecastDays()));
                results.appendChild(requestNumber);
                
                Element minTemperature = document.createElement("minTemperature");
                minTemperature.setTextContent(Float.toString(weatherRequests.get(i).getMinTemperature()) + "°C");
                requestNumber.appendChild(minTemperature);
                
                Element maxTemperature = document.createElement("maxTemperature");
                maxTemperature.setTextContent(Float.toString(weatherRequests.get(i).getMaxTemperature()) + "°C");
                requestNumber.appendChild(maxTemperature);
                
                Element dateTime = document.createElement("dateTime");
                dateTime.setTextContent(weatherRequests.get(i).getDateTime());
                requestNumber.appendChild(dateTime);
                
            }
            
            Transformer transformer = TransformerFactory.newInstance()
                                                        .newTransformer();
            
            DOMSource source = new DOMSource(document);
            
            StreamResult result = new StreamResult(new File(filePath.toString()));
            
            transformer.transform(source, result);
            
            System.out.println("Документ сохранен!");
            
        }catch(ParserConfigurationException | TransformerException e){
            
            System.err.println("Произошла ошибка во время создания файла");
            
        }        
            
    }
    
}
