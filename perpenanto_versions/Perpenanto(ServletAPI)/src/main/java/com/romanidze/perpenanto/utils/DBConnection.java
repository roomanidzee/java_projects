package com.romanidze.perpenanto.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class DBConnection {

    private String DB_DRIVER;
    private String DB_HOST;
    private String DB_NAME;
    private String DB_USERNAME;
    private String DB_PASSWORD;
    private InputStream is;

    private DBConnection(){}

    public DBConnection(InputStream is){

        this.is = is;

        initConfiguration();

    }
    //Сеттеры приватные для функции initConfiguration(), которая описана ниже

    private void setDB_HOST(String DB_HOST) {
        this.DB_HOST = DB_HOST;
    }

    private void setDB_DRIVER(String DB_DRIVER) {
        this.DB_DRIVER = DB_DRIVER;
    }

    private void setDB_NAME(String DB_NAME) {
        this.DB_NAME = DB_NAME;
    }

    private void setDB_USERNAME(String DB_USERNAME) {
        this.DB_USERNAME = DB_USERNAME;
    }

    private void setDB_PASSWORD(String DB_PASSWORD) {
        this.DB_PASSWORD = DB_PASSWORD;
    }

    private void initConfiguration(){

        Properties prop = new Properties();

        try {
            prop.load(this.is);
        } catch (IOException e) {
            System.err.println("Произошла ошибка во время загрузки");
        }

        this.setDB_DRIVER(prop.getProperty("db.driver"));
        this.setDB_HOST(prop.getProperty("db.host"));
        this.setDB_NAME(prop.getProperty("db.name"));
        this.setDB_USERNAME(prop.getProperty("db.username"));
        this.setDB_PASSWORD(prop.getProperty("db.password"));

    }


    private String getDB_USERNAME() {
        return this.DB_USERNAME;
    }

    private String getDB_PASSWORD() {
        return this.DB_PASSWORD;
    }

    private String getDB_DRIVER() {
        return DB_DRIVER;
    }

    private String getDB_HOST() {
        return DB_HOST;
    }

    private String getDB_NAME() {
        return DB_NAME;
    }

    public Map<String, String> getDBConfig(){

        StringBuilder sb = new StringBuilder();

        sb.append("jdbc:postgresql://")
          .append(this.getDB_HOST()).append("/")
          .append(this.getDB_NAME());

        Map<String, String> resultMap = new LinkedHashMap<>();

        resultMap.put("db_url", sb.toString());
        resultMap.put("db_username", this.getDB_USERNAME());
        resultMap.put("db_password", this.getDB_PASSWORD());

        return resultMap;

    }

}
