package com.romanidze.gitrepoclone.implementations;

import com.romanidze.gitrepoclone.interfaces.GithubAPIInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class GithubAPIImpl implements GithubAPIInterface{

    private final String USER_AGENT = "Mozilla/5.0";
    
    @Override
    public String buildRequestURL(String username) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("https://api.github.com/users/")
          .append(username)
          .append("/repos");
        
        return sb.toString();
        
    }

    @Override
    public String makeGETRequest(String requestURL) {
        
        URL obj = null;
        HttpURLConnection con = null;
        
        try{
            
            obj = new URL(requestURL);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", this.USER_AGENT);
            int responseCode = con.getResponseCode();
            
            System.out.println("Отправляю GET - запрос на " + requestURL);
            System.out.println("Код ответа: " + responseCode);
            System.out.println();
            
        }catch(IOException e){
            
            System.err.println("Полученный URL не существует. Пожалуйста, "
                    + "проверьте его");
            
        } 
        
        String line = "";
        StringBuilder response = new StringBuilder();
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            
            line = br.readLine();
            
            while(line != null){
                
                response.append(line);
                line = br.readLine();
                
            }
            
        }catch(IOException e){
            
            System.err.println("Произошла ошибка при получении данных");
            
        }
        
        //API Github возвращает json, который начинается с [,
        //соответственно, такой json не обрабатывается, и поэтому
        //необходимо его редактировать
        
        StringBuilder result = new StringBuilder();
        result.append("{")
              .append("\"repos\":")
              .append(response.toString())
              .append("}");
        
        return result.toString();
    }
    
}
