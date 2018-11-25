package app.kannadariddles.com.services;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by varun.am on 25/11/18
 */
public class RiddlesJobService extends JobService {
    
    private static final String TAG = RiddlesJobService.class.getSimpleName();
    private AsyncTask asyncTask;
    
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(TAG, "Started RiddlesJobService");
        asyncTask = new RiddlesLoaderService(this).execute();
        return true;
    }
    
    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e(TAG, "Stopped RiddlesJobService");
        if (asyncTask != null)
            asyncTask.cancel(true);
        return true;
    }
}
