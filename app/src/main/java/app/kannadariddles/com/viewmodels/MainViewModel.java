package app.kannadariddles.com.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import app.kannadariddles.com.data.KannadaRiddlesDatabase;
import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 28/10/18
 */
public class MainViewModel extends AndroidViewModel {
    
    private LiveData<List<KannadaRiddle>> riddles;
    
    public MainViewModel(@NonNull Application application) {
        super(application);
        riddles = KannadaRiddlesDatabase.getInstance(this.getApplication()).kannadaRiddlesDao().loadAllKannadaRiddles();
    }
    
    public LiveData<List<KannadaRiddle>> getRiddles () {
        return riddles;
    }
    
    
}
