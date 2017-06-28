package com.romanidze.pdfcreation.processing;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.romanidze.pdfcreation.implementations.DocumentEditImpl;
import com.romanidze.pdfcreation.implementations.DocumentUtilsImpl;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class LaunchFunction {
    
    public static void launch(){
        
        ConstantInit constants = new ConstantInit();
        
        Font font = FontFactory.getFont(constants.getTimesNewRomanFont().toString(), BaseFont.IDENTITY_H, true);
        
        Map<String, String> songsMap = new HashMap<>();
        List<Double> durationList = new ArrayList<>();
        
        DocumentUtilsImpl docUtils = new DocumentUtilsImpl();
        
        docUtils.getInfo(songsMap, durationList);
        
        DocumentEditImpl docEdit = new DocumentEditImpl(font, songsMap, durationList);
        
        Document document = new Document();
        
        try {
            
            PdfWriter.getInstance(document, new FileOutputStream(constants.getPDFPath().toString()));
            document.open();
            docUtils.addMetaData(document);
            docEdit.addTitlePage(document);
            docEdit.addContent(document);
            document.close();
            
        } catch (FileNotFoundException ex) {
            
            System.err.println("Файл не найден");
            
        } catch (DocumentException ex) {
            
            System.err.println("Произошла ошибка записи в документ");
            
        }
        
    }
    
}
