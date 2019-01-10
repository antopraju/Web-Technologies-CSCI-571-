package com.example.heman.travelsearch;

public class Review {
    private String displayName, Uri, artistName, datetime, type;

    public Review(){

    }

    public Review(String displayName, String Uri, String artistName, String datetime, String type) {

        this.displayName = displayName;
        this.Uri = Uri;
        this.artistName = artistName;
        this.datetime = datetime;
        this.type = type;
    }

    public void setdisplayName(String displayName){
        this.displayName = displayName;
    }

    public void setUri(String Uri){
        this.Uri = Uri;
    }

    public void setartistName(String artistName){
        this.artistName = artistName;
    }

    public void setdatetime(String datetime){
        this.datetime = datetime;
    }

    public void settype(String type){
        this.type = type;
    }


    public String getdisplayName() {
        return displayName;
    }


    public String getUri() {
        return Uri;
    }


    public String getartistName() {
        return artistName;
    }


    public String getdatetime() {
        return datetime;
    }


    public String gettype() {
        return type;
    }

}
