package com.example.project2_mediaplayer;

import java.io.Serializable;

public class Music implements Serializable {
    private int musicId;
    private String musicName;
    private String musicAuthor;

    private int resource;

    public Music(int musicId, String musicName, String musicAuthor) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicAuthor = musicAuthor;
    }

    public Music(int musicId, String musicName, String musicAuthor, int resource) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicAuthor = musicAuthor;
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }


}
