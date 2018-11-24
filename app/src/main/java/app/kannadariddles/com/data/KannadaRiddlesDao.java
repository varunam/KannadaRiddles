package app.kannadariddles.com.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 04/11/18
 */
@Dao
public interface KannadaRiddlesDao {
    
    @Query("SELECT * FROM kannadariddles ORDER BY id")
    LiveData<List<KannadaRiddle>> loadAllKannadaRiddles();
    
    @Query("SELECT * FROM KannadaRiddles WHERE id = :id")
    KannadaRiddle loadRiddleById(int id);
    
    @Insert
    void insertRiddle(KannadaRiddle kannadaRiddle);
    
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateKannadaRiddle(KannadaRiddle kannadaRiddle);
    
    @Query("SELECT SUM(score) FROM kannadariddles")
    int getTotalScore();
    
    @Query("SELECT * FROM kannadariddles WHERE riddle = :riddle")
    Cursor hasRiddle(String riddle);
    
}
