package app.kannadariddles.com.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import app.kannadariddles.com.model.Riddle;

/**
 * Created by varun.am on 28/10/18
 */
public class MainViewModel extends AndroidViewModel {
    
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    
    public MutableLiveData<List<Riddle>> riddles = new MutableLiveData<>();
    
    
}
