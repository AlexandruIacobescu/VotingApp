package com.example.votingapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Query {
    private SimpleIntegerProperty queryid;
    private SimpleStringProperty text;
    Query(Integer id, String text){
        queryid = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
    }
    public String getText(){
        return text.get();
    }
    public Integer getQueryId(){
        return queryid.get();
    }
}
