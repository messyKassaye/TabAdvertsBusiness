package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloads")
public class Download {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "fileName")
    private String fileName;

    @ColumnInfo(name = "downloadStatus")
    private String downloadStatus;

    @ColumnInfo(name = "process")
    private String process;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
