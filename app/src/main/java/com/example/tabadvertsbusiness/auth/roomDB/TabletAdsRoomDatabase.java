package com.example.tabadvertsbusiness.auth.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.AdvertViewDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.DownloadDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.EntertainmentDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.TabletAssignDAO;
import com.example.tabadvertsbusiness.auth.roomDB.DAO.TabletAssignedCarWorkPlaceDAO;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.AdvertViewsRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.Download;
import com.example.tabadvertsbusiness.auth.roomDB.entity.EntertainmentRoom;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignation;
import com.example.tabadvertsbusiness.auth.roomDB.entity.TabletAssignedCarWorkplace;
import com.example.tabadvertsbusiness.constants.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {AdvertRoom.class, AdvertViewsRoom.class, TabletAssignedCarWorkplace.class,
        Download.class, EntertainmentRoom.class, TabletAssignation.class},
        version = 15,
        exportSchema = false)
public abstract class TabletAdsRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THEARD = 4;
    private static volatile TabletAdsRoomDatabase INSTANCE;
   public static final ExecutorService dbExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THEARD);

   public static synchronized TabletAdsRoomDatabase getDatabase(Context context){
        if(INSTANCE==null){
            if (INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TabletAdsRoomDatabase.class, Constants.getDbName())
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract AdvertDAO getAdvertDAO();

   public abstract DownloadDAO getDownloadDAO();
   public abstract EntertainmentDAO getEntertainmentDAO();

   public abstract AdvertViewDAO getAdvertViewDAO();
   public abstract TabletAssignDAO getTabletAssignation();

   public abstract TabletAssignedCarWorkPlaceDAO getTabletCarWorkPlaceDAO();
}
