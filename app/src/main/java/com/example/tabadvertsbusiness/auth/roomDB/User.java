package com.example.tabadvertsbusiness.auth.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String name;

    
}
