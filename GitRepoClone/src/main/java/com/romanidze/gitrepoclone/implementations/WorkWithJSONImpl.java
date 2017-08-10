package com.romanidze.gitrepoclone.implementations;

import com.romanidze.gitrepoclone.interfaces.WorkWithJSONInterface;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class WorkWithJSONImpl implements WorkWithJSONInterface{

    @Override
    public Map<String, String> getCloneRepos(JSONObject jsonString) {
        
        Map<String, String> resultMap = new LinkedHashMap<>();
        
        JSONArray repoArray = (JSONArray) jsonString.get("repos");
        
        Iterator iter = repoArray.iterator();
        
        JSONObject tempObject = null;
        
        while(iter.hasNext()){
            
            tempObject = (JSONObject)iter.next();
            resultMap.put((String) tempObject.get("full_name"), (String) tempObject.get("clone_url"));
            
        }
        
        return resultMap;
        
    }

    @Override
    public boolean cloneRepoToFolder(Path pathToFolder, String cloneURL) {
        
        if(!Files.exists(pathToFolder)){
            
            System.err.println("Создайте папку для сохранения репозитория");
            return false;
            
        }
        
        File directory = pathToFolder.toFile();
        
        try {
            
            Git git = Git.cloneRepository()
                    .setURI(cloneURL)
                    .setDirectory(directory)
                    .call();
            
        } catch (GitAPIException ex) {
            
            System.err.println("Произошла ошибка во время клонирования репозитория");
            
        }
        
        return directory.list().length > 0;
        
    }
    
}
