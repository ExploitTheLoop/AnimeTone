package com.anime.animetone;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class MySingleton<T> {
    private static MySingleton instance;
    private List<T> dataList;

    private MySingleton() {
        // Private constructor to prevent instantiation
        dataList = new ArrayList<>();
    }

    public static synchronized MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public void clearDataList() {
        this.dataList.clear();
    }

    public void addDataList(ArrayList<T> dataListToAdd) {
        this.dataList.addAll(dataListToAdd);
    }
}


