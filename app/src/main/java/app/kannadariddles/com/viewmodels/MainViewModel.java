package app.kannadariddles.com.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lib.riddlesprovider.model.Riddle;

import java.util.List;

/**
 * Created by varun.am on 28/10/18
 */
public class MainViewModel extends AndroidViewModel {
    
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    
    public MutableLiveData<List<Riddle>> riddles = new MutableLiveData<>();
    
    
}
