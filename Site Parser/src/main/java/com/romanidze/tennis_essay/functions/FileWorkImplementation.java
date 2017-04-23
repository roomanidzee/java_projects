package com.romanidze.tennis_essay.functions;

import com.romanidze.tennis_essay.interfaces.FileWorkInterface;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class FileWorkImplementation implements FileWorkInterface{

    //private ClassLoader classLoader = getClass().getClassLoader();
    private final File FILE = new File("src\\main\\resources\\data_files\\tracks.txt");
    
    private final String ENCODING = "UTF-8";
    
    @Override
    public void saveData(String dataToFile) {
        
        try(PrintWriter pw = new PrintWriter(this.FILE, this.ENCODING))
        {
            
            pw.println(dataToFile);
            
        }catch(IOException error){
            
            System.err.println("Файл не найден");
            
        }
        
    }
    
}
