package com.romanidze.data_scraper.interfaces;

import java.util.List;
import org.jsoup.nodes.Document;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface ParseInterface {
    
     List<String> getAllTitles(Document doc);
     List<String> getAllViews(Document doc);
    
}
