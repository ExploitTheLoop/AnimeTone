package com.anime.animetone;

public class albumModel {
    String albumname;
    String albumimg;
    public albumModel(String albumanem,String albumimg){
        this.albumname = albumanem;
        this.albumimg = albumimg;

    }

    public String getAlbumname() {
        return albumname;
    }

    public String getAlbumimg() {
        return albumimg;
    }
}
