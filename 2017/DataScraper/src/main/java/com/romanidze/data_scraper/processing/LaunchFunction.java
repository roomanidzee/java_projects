package com.romanidze.data_scraper.processing;

import com.romanidze.data_scraper.functions.FileWorkImplementation;
import com.romanidze.data_scraper.functions.ParseImplementation;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class LaunchFunction {
    
    private final Path FILEPATH = Paths.get("src/main/resources/app.properties");
    
    public void launch(){
        
        Connection con = null;
        
        Properties p = new Properties();        
        
        try {
            
            Reader isr = new InputStreamReader(new FileInputStream(this.FILEPATH.toFile()));
            
            p.load(isr);
            
            String requestURL = p.getProperty("app.link");
            
            System.out.println("Отправляю запрос на " + requestURL);
            
            con = Jsoup.connect(requestURL);
            
        } catch (IOException ex) {
            
            System.err.println("Ошибка ввода - вывода");
            
        }
        
        ParseImplementation parse = new ParseImplementation();
        
        List<String> titlesList = new ArrayList<>();
        List<String> viewsList = new ArrayList<>();
        
        try{
        
           titlesList.addAll(parse.getAllTitles(con.get()));
           viewsList.addAll(parse.getAllViews(con.get()));
        
        }catch(IOException e){
            
            System.err.println("Произошла ошибка при получении данных");
            
        }
        
        FileWorkImplementation fileWork = new FileWorkImplementation(titlesList, viewsList);
        
        fileWork.saveData();
              
    }
    
}
