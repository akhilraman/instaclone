package com.example.instagramclone;

public class comment {
    String username;
    String comment;
    comment(String u,String c){
        this.username=u;
        this.comment=c;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

