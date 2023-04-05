package com.example.project2_mediaplayer;

import java.io.Serializable;

public class Music implements Serializable {
    private String songimage;
    private int authorid;
    private int songDuration;
    private String songTitle;
    private String authorName;
    private String songLink;

    public Music() {
    }

    public Music(String songimage, String songTitle, String authorName) {
        this.songimage = songimage;
        this.songTitle = songTitle;
        this.authorName = authorName;
    }

    public Music(String songimage, int authorid, int songDuration, String songTitle, String authorName, String songLink) {
        this.songimage = songimage;
        this.authorid = authorid;
        this.songDuration = songDuration;
        this.songTitle = songTitle;
        this.authorName = authorName;
        this.songLink = songLink;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getSongimage() {
        return songimage;
    }

    public void setSongimage(String songimage) {
        this.songimage = songimage;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}
