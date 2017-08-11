package com.romanidze.gitrepoclone.interfaces;

import java.nio.file.Path;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface WorkWithJSONInterface {
    
    Map<String, String> getCloneRepos(JSONObject jsonString);//получение списка репозиториев
    boolean cloneRepoToFolder(Path pathToFolder, String cloneURL);//копирование определенного репозитория
                                                                  //на компьютер
    
}
