package com.example.votingapp;

import javafx.beans.property.SimpleStringProperty;

public class Member {
    private SimpleStringProperty firstName, lastName, email, joinDate, memberID, ssn, category;
    Member(String fnm, String lnm, String email, String join, String id, String ssn, String cat){
        firstName = new SimpleStringProperty(fnm);
        lastName = new SimpleStringProperty(lnm);
        this.email = new SimpleStringProperty(email);
        joinDate = new SimpleStringProperty(join);
        memberID = new SimpleStringProperty(id);
        this.ssn = new SimpleStringProperty(ssn);
        category = new SimpleStringProperty(cat);
    }
    public String getFirstName(){return firstName.get();}
    public String getLastName(){return lastName.get();}
    public String getEmail(){return email.get();}
    public String getJoinDate(){return joinDate.get();}
    public String getMemberID(){return memberID.get();}
    public String getSsn(){return ssn.get();}
    public String getCategory(){return category.get();}
}
