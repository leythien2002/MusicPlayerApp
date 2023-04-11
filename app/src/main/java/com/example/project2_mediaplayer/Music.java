package com.example.project2_mediaplayer;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Music implements Serializable {
    private String songimage;
    private int authorid;
    private String songTitle;
    private String authorName;
    private String songLink;
    private int favorite;
    private String songID;

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public Music(String songimage, int authorid, String songTitle, String authorName, String songLink, int favorite, String songID) {
        this.songimage = songimage;
        this.authorid = authorid;
        this.songTitle = songTitle;
        this.authorName = authorName;
        this.songLink = songLink;
        this.favorite = favorite;
        this.songID = songID;
    }

    public Music() {
    }

    public Music(String songimage, int authorid, String songTitle, String authorName, String songLink, int favorite) {
        this.songimage = songimage;
        this.authorid = authorid;
        this.songTitle = songTitle;
        this.authorName = authorName;
        this.songLink = songLink;
        this.favorite = favorite;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Music(String songimage, String songTitle, String authorName) {
        this.songimage = songimage;
        this.songTitle = songTitle;
        this.authorName = authorName;
    }

    public Music(String songimage, int authorid,  String songTitle, String authorName, String songLink) {
        this.songimage = songimage;
        this.authorid = authorid;
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

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("favorite", favorite);

        return result;
    }


}
