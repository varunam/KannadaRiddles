package app.kannadariddles.com.services;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by varun.am on 25/11/18
 */
public class RiddlesLoadingScheduler {
    
    private static final String TAG = RiddlesLoadingScheduler.class.getSimpleName();
    private static final int REMINDER_INTERVAL_IN_DAYS = 1;
    private static final long SYNC_FLEXI_SECONDS = 10;
    //private static final int REMINDER_INTERVAL_SECONDS = 20;
    private static final String JOB_TAG = "riddlesJob";
    private static final long REMINDER_INTERVAL_SECONDS = TimeUnit.DAYS.toSeconds(REMINDER_INTERVAL_IN_DAYS);
    
    public static void scheduleRiddlesLoading(Context context){
        Log.e(TAG,"scheduling riddles loading...");
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Job.Builder riddlesJob = firebaseJobDispatcher.newJobBuilder()
                .setService(RiddlesJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow((int) REMINDER_INTERVAL_SECONDS, (int) (REMINDER_INTERVAL_SECONDS + SYNC_FLEXI_SECONDS)));
        
        firebaseJobDispatcher.mustSchedule(riddlesJob.build());
        Log.e(TAG,"scheduled riddles loading...");
    }
}
