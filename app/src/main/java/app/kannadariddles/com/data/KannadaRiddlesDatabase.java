package app.kannadariddles.com.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 04/11/18
 */
@Database(entities = {KannadaRiddle.class}, version = 1, exportSchema = false)
public abstract class KannadaRiddlesDatabase extends RoomDatabase {

    private static final String TAG = KannadaRiddlesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "kannadariddles";
    private static KannadaRiddlesDatabase kannadaRiddlesDatabase;
    
    public static KannadaRiddlesDatabase getInstance(Context context){
        if(kannadaRiddlesDatabase==null){
            synchronized (LOCK){
                Log.d(TAG,"Creating new database instance");
                kannadaRiddlesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        KannadaRiddlesDatabase.class, DATABASE_NAME)
                        //TODO temporary main thread
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG,"Getting the database instance");
        return kannadaRiddlesDatabase;
    }

    public abstract KannadaRiddlesDao kannadaRiddlesDao();
}
