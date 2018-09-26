package com.romanidze.gitrepoclone.processing;

import com.romanidze.gitrepoclone.implementations.GithubAPIImpl;
import com.romanidze.gitrepoclone.implementations.WorkWithJSONImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class LaunchFunction {
    
    public static void launch(){            
        
        Scanner sc = new Scanner(System.in);        
        
        String username = sc.nextLine();
        
        //Неизвестно почему, но maven опережает вывод в консоли
        //обычного System.out, и не выводит его, поэтому я заменил его
        //на System.err
        
        System.err.println();
        System.err.println();
        
        GithubAPIImpl ghAPI = new GithubAPIImpl();
        
        String responseResult = ghAPI.makeGETRequest(ghAPI.buildRequestURL(username));
        
        Map<String, String> repoMap = new LinkedHashMap<>();
        
        JSONParser parser = new JSONParser();
        JSONObject responseJSON = null;
        
        try {
            
            responseJSON = (JSONObject) parser.parse(responseResult);
            
        } catch (ParseException ex) {
            System.err.println("На вход программе подошла неверная строка. "
                    + "Перепроверьте её");
            
            System.exit(1);
        }
        
        WorkWithJSONImpl jsonWork = new WorkWithJSONImpl();
        
        repoMap.putAll(jsonWork.getCloneRepos(responseJSON));
        
        Set<String> keys = repoMap.keySet();
        
        System.out.println("Список репозиториев: ");
        System.out.println();
        
        keys.forEach((key) -> {
            System.out.println(key);
        });
        
        String repoName = sc.nextLine();
        
        Path folderToSave = Paths.get("src/main/resources/repo").toAbsolutePath();
        
        if(!Files.exists(folderToSave)){
            
            try {
                
                Files.createDirectory(folderToSave);
                
            } catch (IOException ex) {
                System.err.println("Произошла ошибка при создании папки");
                System.exit(1);
            }
            
        }
        
        boolean cloneResult = jsonWork.cloneRepoToFolder(folderToSave, repoMap.get(repoName));
        
        if(cloneResult){
            
            System.out.println("Репозиторий успешно склонирован!");
            
        }else{
            
            System.err.println("Проверьте работоспособность кода");
            System.exit(1);
            
        }
        
    }
    
}
