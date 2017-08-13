package com.romanidze.data_scraper.functions;

import com.romanidze.data_scraper.entities.Record;
import com.romanidze.data_scraper.interfaces.FileWorkInterface;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class FileWorkImplementation implements FileWorkInterface{
    
    private List<String> titlesList = new ArrayList<>();
    private List<String> viewsList = new ArrayList<>();
    
    private final Path FILEPATH = Paths.get("src/main/resources/data_files/records.csv").toAbsolutePath();
    
    private final String NEW_LINE_SEPARATOR = "\n";
    
    private final Object[] FILE_HEADER = {"Название песни", "Просмотры"};
    
    public FileWorkImplementation(List<String> titlesList, List<String> viewsList){
        
        if(titlesList.size() != viewsList.size()){
            
            System.err.println("Массивы значений должны быть одинакового "
                    + "размера!");
            System.exit(1);
            
        }
        
        this.titlesList = titlesList;
        this.viewsList = viewsList;
        
        if(!Files.exists(this.FILEPATH)){
            
            try {
                
                Files.createFile(this.FILEPATH);
                
            } catch (IOException ex) {
                System.err.println("Произошла ошибка при создании файла");
            }
            
        }
        
    }
    
    @Override
    public void saveData() {
        
       List<Record> recordList = new ArrayList<>();
       
       int size = this.titlesList.size();
       
       for(int i = 0; i < size; i++){
           
           recordList.add(new Record(this.titlesList.get(i), this.viewsList.get(i)));
           
       }
       
       try(FileWriter fw = new FileWriter(this.FILEPATH.toFile()))
       {
           
           CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(this.NEW_LINE_SEPARATOR);
           
           CSVPrinter csvFilePrinter = new CSVPrinter(fw,csvFileFormat);
           
           csvFilePrinter.printRecord(this.FILE_HEADER);
           
           List<String> songRecord = null;
           
           for(Record record: recordList){
               
               songRecord = new ArrayList<>();
               
               songRecord.add(record.getSongTitle());
               songRecord.add(record.getViewCounter());
               
               csvFilePrinter.printRecord(songRecord);
               
           }
           
       }catch(IOException e){
           
           System.err.println("Произошла ошибка во время сохранения данных");
           
       }finally{
           
           System.out.println("Сохранение данных выполнено!");
           
       }
        
    }
    
}
