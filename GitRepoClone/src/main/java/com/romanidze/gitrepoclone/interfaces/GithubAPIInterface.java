package com.romanidze.gitrepoclone.interfaces;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface GithubAPIInterface {
    
    String buildRequestURL(String username);
    String makeGETRequest(String requestURL);
    
}
