package com.example.project2_mediaplayer;

import java.io.Serializable;

public class Author implements Serializable {

    private String authorname;
    private int authorid;
    private String authorimage1;

    private String authorimage2;



    public Author() {
    }

    public Author(String authorname, int authorid, String authorimage1, String authorimage2) {
        this.authorname = authorname;
        this.authorid = authorid;
        this.authorimage1 = authorimage1;
        this.authorimage2 = authorimage2;
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

    public String getAuthorimage1() {
        return authorimage1;
    }

    public void setAuthorimage1(String authorimage1) {
        this.authorimage1 = authorimage1;
    }

    public String getAuthorimage2() {
        return authorimage2;
    }

    public void setAuthorimage2(String authorimage2) {
        this.authorimage2 = authorimage2;
    }
}
