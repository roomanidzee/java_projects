package com.romanidze.tennis_essay.functions;

import com.romanidze.tennis_essay.interfaces.FileWorkInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class FileWorkImplementation implements FileWorkInterface{

    private final Path FILEPATH = Paths.get("src/main/resources/data_files/tracks.txt").toAbsolutePath();
    
    private final String ENCODING = "UTF-8";
    
    @Override
    public void saveData(String dataToFile) {
        
        try(PrintWriter pw = new PrintWriter(this.FILEPATH.toFile(), this.ENCODING))
        {
            
            pw.println(dataToFile);
            
        }catch(IOException error){
            
            System.err.println("Файл не найден");
            
        }
        
    }
    
}
