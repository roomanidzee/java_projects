package com.romanidze.gitrepoclone.interfaces;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface GithubAPIInterface {
    
    String buildRequestURL(String username);//создание ссылки для запроса
    String makeGETRequest(String requestURL);//отправка запроса на созданную ссылку
    
}
