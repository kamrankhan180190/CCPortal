package com.example.kamrankhan.pstapplication;


public class RootFolders implements Comparable<RootFolders>{
    private String folder_name;
    private int folder_count;

    //Constructor
    public RootFolders() {

    }
    //Constructor
    public RootFolders(String folder_name, int folder_count) {

        this.folder_name = folder_name;
        this.folder_count = folder_count;
    }

    //Getter and Setter
    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public int getFolder_count() {
        return folder_count;
    }

    public void setFolder_count(int folder_count) {
        this.folder_count = folder_count;
    }

    @Override
    public int compareTo(RootFolders other) {
        return folder_name.compareTo(other.folder_name);
    }
}
