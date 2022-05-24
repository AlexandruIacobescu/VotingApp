package com.example.votingapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Query {
    private SimpleIntegerProperty queryid, approve, disapprove;
    private SimpleStringProperty text, accesslevel;
    Query(Integer id, String text){
        queryid = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
    }
    Query(Integer id, String text, String accesslevel, int approve, int disapprove){
        queryid = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
        this.accesslevel = new SimpleStringProperty(accesslevel);
        this.approve = new SimpleIntegerProperty(approve);
        this.disapprove = new SimpleIntegerProperty(disapprove);
    }
    public String getText(){
        return text.get();
    }
    public Integer getQueryId(){
        return queryid.get();
    }
    public String getAccessLevel(){return accesslevel.get();}
    public int getApprove(){return approve.get(); }
    public int getDisapprove(){return disapprove.get();}
}
