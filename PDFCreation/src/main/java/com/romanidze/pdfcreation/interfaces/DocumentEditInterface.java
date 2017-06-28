package com.romanidze.pdfcreation.interfaces;

import com.itextpdf.text.Document;
import com.itextpdf.text.Section;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface DocumentEditInterface {
    
    void addTitlePage(Document document);
    void addContent(Document document);
    void createTable(Section section);
    
}
