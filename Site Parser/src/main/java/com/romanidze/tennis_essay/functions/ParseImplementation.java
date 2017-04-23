package com.romanidze.tennis_essay.functions;

import com.romanidze.tennis_essay.interfaces.ParseInterface;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class ParseImplementation implements ParseInterface{

    @Override
    public String getAllTitles(Document doc) {
        
        Elements data = doc.select("table[class$=table] > tbody > tr > td > a");              
        
        String temp = data.toString();
        
        String temp1 = temp.replaceAll("<a href=\\\"[^\\\"]([^>]+)>| ", " ");
        
        String temp2 = temp1.replaceAll("</a>", " ");
        
        String temp3 = temp2.replaceAll("&nbsp", " ");
        
        return temp3;
        
    }
    
}
