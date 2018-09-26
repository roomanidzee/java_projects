package com.romanidze.pdfcreation.implementations;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.romanidze.pdfcreation.interfaces.DocumentEditInterface;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class DocumentEditImpl implements DocumentEditInterface{

    private Font font;
    private Map<String, String> songs;
    private List<Double> duration;
    
    private DocumentEditImpl(){}
    
    public DocumentEditImpl(Font textFont, Map<String, String> songMap, List<Double> durationList)
    {
        
        this.font = textFont;
        this.songs = songMap;
        this.duration = durationList;
        
    }
    
    @Override
    public void addTitlePage(Document document) {
        
        DocumentUtilsImpl docUtil = new DocumentUtilsImpl();
        
        Paragraph preface = new Paragraph();
        
        docUtil.addEmptyLine(preface, 1);
        
        preface.add(new Paragraph("Таблица песен", this.font));
        preface.setAlignment(Element.ALIGN_CENTER);
        
        docUtil.addEmptyLine(preface, 1);
        
        preface.add(new Paragraph("В данной таблице вы увидете описание композиций", this.font));
        
        docUtil.addEmptyLine(preface, 1);
        
        document.newPage();
        
    }

    @Override
    public void addContent(Document document) {
        
        Anchor anchor = new Anchor("Глава для таблицы", this.font);
        anchor.setName("Глава для таблицы");
        
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        
        Paragraph subPara = new Paragraph("Вид: ", this.font);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Данные: "));
        
        createTable(subCatPart);
        
        try {
            
            document.add(catPart);
            
        } catch (DocumentException ex) {
            
            System.err.println("Произошла ошибка при добавлении таблицы");
            
        }
        
    }

    @Override
    public void createTable(Section section) {
       
        PdfPTable table = new PdfPTable(3);
        
        PdfPCell c1 = new PdfPCell(new Phrase("Псевдоним исполнителя", this.font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Название песни", this.font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Длительность(в минутах)", this.font));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        table.setHeaderRows(1);
        
        Iterator<Entry<String, String>> iter1 = this.songs.entrySet().iterator();
        Iterator<Double> iter2 = this.duration.iterator();
        
        while(iter1.hasNext() && iter2.hasNext()){
            
            Entry<String, String> entry = iter1.next();
            
            String nickname = entry.getKey();
            String title = entry.getValue();
            
            table.addCell(nickname);
            table.addCell(title);
            table.addCell(iter2.next().toString());
            
        }
        
        section.add(table);
        
    }
    
}
