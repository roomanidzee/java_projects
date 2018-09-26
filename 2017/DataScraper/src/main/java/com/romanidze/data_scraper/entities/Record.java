package com.romanidze.data_scraper.entities;

import java.util.Objects;

/**
 *
 * @author Андрей Романов <steampart@gmail.com>
 */
public class Record {
    
    private String songTitle;
    private String viewCounter;
    
    public Record(String newSongTitle, String newViewCounter){
        
        this.songTitle = newSongTitle;
        this.viewCounter = newViewCounter;
        
    }

    public String getSongTitle() {
        return songTitle;
    }

    
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getViewCounter() {
        return viewCounter;
    }

    public void setViewCounter(String viewCounter) {
        this.viewCounter = viewCounter;
    }

    @Override
    public int hashCode() {
        
        int hash = 3;
        
        hash = 13 * hash + Objects.hashCode(this.songTitle);
        hash = 13 * hash + Objects.hashCode(this.viewCounter);
        
        return hash;
        
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if(!(obj instanceof Record)){
            
            return false;
            
        }
        
        final Record other = (Record) obj;
        
        if (!Objects.equals(this.getSongTitle(), other.getSongTitle())) {
            return false;
        }
        
        return Objects.equals(this.getViewCounter(), other.getViewCounter());
    }
    
    @Override
    public String toString(){
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("Record{")
          .append("songTitle = ")
          .append(this.getSongTitle())
          .append(", viewCounter = ")
          .append(this.getViewCounter())
          .append("}");
        
        return sb.toString();
        
    }
    
}
