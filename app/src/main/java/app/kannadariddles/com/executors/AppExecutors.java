package app.kannadariddles.com.executors;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    
    private static final Object LOCK = new Object();
    private static final String TAG = AppExecutors.class.getSimpleName();
    private static AppExecutors appExecutors;
    private Executor diskIO, mainThread;
    
    private AppExecutors(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }
    
    public static AppExecutors getInstance(Context context) {
        if (appExecutors == null) {
            Log.e(TAG, "Creating AppExecutors");
            appExecutors = new AppExecutors(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
        }
        Log.e(TAG, "Returning appExecutors");
        return appExecutors;
    }
    
    public Executor diskIO() {
        return diskIO;
    }
    
    public Executor mainThread() {
        return mainThread;
    }
    
    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
    
}
