package com.romanidze.pdfcreation.interfaces;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public interface DocumentUtilsInterface {
    
    void addEmptyLine(Paragraph paragraph, int number);//добавление отступов
    void getInfo(Map<String, String> songMap, List<Double> durationList);//получение информации от пользователя
    void addMetaData(Document document);//добавление мета - свойств
    
}
