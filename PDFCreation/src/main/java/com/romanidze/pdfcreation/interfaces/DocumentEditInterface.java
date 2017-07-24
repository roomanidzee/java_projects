package com.romanidze.pdfcreation.interfaces;

import com.itextpdf.text.Document;
import com.itextpdf.text.Section;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface DocumentEditInterface {
    
    void addTitlePage(Document document);//добавление титульника
    void addContent(Document document);//добавление содержимого страницы
    void createTable(Section section);//создание таблицы на странице
    
}
