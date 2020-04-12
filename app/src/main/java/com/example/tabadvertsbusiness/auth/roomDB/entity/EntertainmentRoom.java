package com.example.tabadvertsbusiness.auth.roomDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entertainment")
public class EntertainmentRoom {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "filePath")
    private String filePath;

    @ColumnInfo(name = "choose",defaultValue = "true")
    private boolean choose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }
}
