package haq.app.thirdapp.roomDatabase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.roomDatabase.DAOs.PhotoDAO;
import haq.app.thirdapp.constants.Constants;

@androidx.room.Database(entities = PhotoDTO.class, exportSchema = false, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public abstract PhotoDAO wallpaperDAO();

    public static void makeInstance(Context context) {
        instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, Constants.DATABASE_NAME)
                //.allowMainThreadQueries()
                .build();
    }

    public static MyDatabase getInstance() {
        return instance;
    }

    public static void closeInstance() {
        instance.close();
    }
}
