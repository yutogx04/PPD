package com.codequest.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.codequest.database.dao.TrackDao;
import com.codequest.database.dao.UserDao;
import com.codequest.database.entity.TrackEntity;
import com.codequest.database.entity.UserEntity;
@Database(entities = {UserEntity.class, TrackEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract UserDao userDao();
    public abstract TrackDao trackDao();
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "codequest_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
