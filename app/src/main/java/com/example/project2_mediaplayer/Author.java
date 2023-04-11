package com.example.project2_mediaplayer;

import java.io.Serializable;

public class Author implements Serializable {

    private String authorname;
    private int authorid;
    private String authorimage;



    public Author() {
    }

    public Author(String authorname, int authorid, String authorimage) {
        this.authorname = authorname;
        this.authorid = authorid;
        this.authorimage = authorimage;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getAuthorimage() {
        return authorimage;
    }

    public void setAuthorimage(String authorimage) {
        this.authorimage = authorimage;
    }


}
