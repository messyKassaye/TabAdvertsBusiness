package com.example.tabadvertsbusiness.auth.model;

public class DownloadRequests {
    private int id;
    private String downloadedAdverts;
    private String device_id;
    private String file_name;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownloadedAdverts() {
        return downloadedAdverts;
    }

    public void setDownloadedAdverts(String downloadedAdverts) {
        this.downloadedAdverts = downloadedAdverts;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
