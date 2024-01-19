package com.anime.animetone;

import java.io.Serializable;

public class Model {
    //firstly define instance variables

    String audio_name,audio_url,audio_category,audio_series,audio_series_img_url;
    int id;

    //then create an constructor that will be called by MainActivity.java

    public Model(int id, String audio_name, String audio_url, String audio_category, String audio_series,String audio_series_img_url) {
        this.id = id;
        this.audio_name = audio_name;
        this.audio_url = audio_url;
        this.audio_category = audio_category;
        this.audio_series = audio_series;
        this.audio_series_img_url = audio_series_img_url;
    }

    //then create getter and setter methods


    public int getId() {
        return id;
    }

    public String getAudio_name() {
        return audio_name;
    }
    public String getAudio_url() {
        return audio_url;
    }
    public String audio_category() {
        return audio_category;
    }

    public String audio_series() {
        return audio_series;
    }

    public String audio_series_img_url() {
        return audio_series_img_url;
    }
}
