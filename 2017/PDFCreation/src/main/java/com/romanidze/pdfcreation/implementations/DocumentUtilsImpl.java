package com.romanidze.pdfcreation.implementations;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.romanidze.pdfcreation.interfaces.DocumentUtilsInterface;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class DocumentUtilsImpl implements DocumentUtilsInterface{

    @Override
    public void addEmptyLine(Paragraph paragraph, int number) {
        
        for(int i = 0; i < number; i++){
            
            paragraph.add(new Paragraph(" "));
            
        }
        
    }

    @Override
    public void getInfo(Map<String, String> songMap, List<Double> durationList) {
        
        System.out.println("Привет, дорогой пользователь!");
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Введите количество композиций: ");
        
        int limit = sc.nextInt();
        
        System.out.println();
        System.out.println();
        
        Scanner sc1 = new Scanner(System.in);
        Scanner sc2 = new Scanner(System.in);
        Scanner sc3 = new Scanner(System.in);
        
        String nickname = "";
        String songTitle = "";
        Double trackDuration = 0.0;
        
        for(int i = 0; i < limit; i++){
            
            System.out.print("Введите псевдоним исполнителя: ");
            nickname = sc1.nextLine();
            System.out.println();
            
            System.out.print("Введите название композиции: ");
            songTitle = sc2.nextLine();
            System.out.println();
            
            System.out.print("Введите длительность композиции: ");
            trackDuration = sc3.nextDouble();
            System.out.println();
            
            System.out.println();
            
            songMap.put(nickname, songTitle);
            durationList.add(trackDuration);
            
        }
        
    }

    @Override
    public void addMetaData(Document document) {
        
        document.addTitle("TrackDB document");
        document.addSubject("Using iText");
        document.addKeywords("Java, iText, PDF, Maven");
        document.addLanguage("RU");
        document.addAuthor("Andrey Romanov");
        document.addCreator("Andrey Romanov");
        document.addCreationDate();
        
    }
    
}
