package com.romanidze.data_scraper.functions;

import com.romanidze.data_scraper.interfaces.ParseInterface;
import java.util.ArrayList;
import java.util.List;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class ParseImplementation implements ParseInterface{

    @Override
    public List<String> getAllTitles(Document doc) {
        
        Elements elements = doc.select("table[class$=table] > tbody > tr > td > a");
        
        List<String> resultList = new ArrayList<>(elements.size());       
        
        int counter = 0;
        
        counter = elements.stream().map((_item) -> 1).reduce(counter, Integer::sum);
        
        ProgressBar pb = new ProgressBar("Названия песен", counter, ProgressBarStyle.ASCII);
        
        pb.start();
        
        elements.forEach((element) -> {
            resultList.add(element.text());
            pb.step();
            pb.setExtraMessage("Загружаю...");
        });
        
        pb.stop();
        
        return resultList;
        
    }

    @Override
    public List<String> getAllViews(Document doc) {
        
        Elements elements = doc.select("table[class$=table] > tbody > tr");
        
        List<String> resultList = new ArrayList<>(elements.size());
        
        int counter = 0;
        
        counter = elements.stream().map((_item) -> 1).reduce(counter, Integer::sum);
        
        ProgressBar pb = new ProgressBar("Количество просмотров для каждой песни",
                                       counter, ProgressBarStyle.ASCII);
        
        Elements siblings = null;
        Element temp = null;
        
        pb.start();
        
        for(Element element: elements){
            
            siblings = element.children();
            temp = siblings.get(1);
            
            resultList.add(temp.text());
            
            pb.step();
            pb.setExtraMessage("Загружаю...");
            
        }
        
        pb.stop();
        
        return resultList;
        
    }
    
}
