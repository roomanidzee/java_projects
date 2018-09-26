package com.romanidze.pdfcreation.processing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class ConstantInit {
    
    private Path TIMES_NEW_ROMAN_FONT = null;
    private Path PATH_TO_PDF = null;
    
    public ConstantInit(){
        
        this.PATH_TO_PDF = Paths.get("src/main/resources/docs/resultPdf.pdf");
        this.TIMES_NEW_ROMAN_FONT =
                Paths.get("src/main/resources/fonts/TimesNewRomanRegular/TimesNewRomanRegular.ttf");

        try{
            
            if(!Files.exists(this.PATH_TO_PDF)){
            
                Files.createFile(this.PATH_TO_PDF);
            
            }
            
            this.PATH_TO_PDF = this.PATH_TO_PDF.toRealPath();
            
            this.TIMES_NEW_ROMAN_FONT = this.TIMES_NEW_ROMAN_FONT.toRealPath();
            
        }catch(IOException e){
            
            System.err.println("Произошла ошибка при создании и нахождении файлов");
            
        }
            
    }
    
    public Path getPDFPath(){
        
        return this.PATH_TO_PDF;
        
    }
    
    public Path getTimesNewRomanFont(){
        
        return this.TIMES_NEW_ROMAN_FONT;
        
    }
    
}
