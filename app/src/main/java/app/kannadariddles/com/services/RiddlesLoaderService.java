package app.kannadariddles.com.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lib.riddlesprovider.RiddlesLoadedCallbacks;
import com.lib.riddlesprovider.RiddlesProvider;
import com.lib.riddlesprovider.model.Riddle;

import java.lang.ref.SoftReference;
import java.util.List;

import app.kannadariddles.com.data.KannadaRiddlesDatabase;
import app.kannadariddles.com.data.model.KannadaRiddle;
import app.kannadariddles.com.executors.AppExecutors;

/**
 * Created by varun.am on 25/11/18
 */
public class RiddlesLoaderService extends AsyncTask<Void, Void, Void> implements RiddlesLoadedCallbacks {
    
    private static final String TAG = RiddlesLoaderService.class.getSimpleName();
    private KannadaRiddlesDatabase kannadaRiddlesDatabase;
    private SoftReference<Context> context;
    
    public RiddlesLoaderService(Context context) {
        this.context = new SoftReference<>(context);
        kannadaRiddlesDatabase = KannadaRiddlesDatabase.getInstance(context);
    }
    
    @Override
    protected Void doInBackground(Void... voids) {
        Log.e(TAG, "Triggering riddles loading");
        RiddlesProvider riddlesProvider = new RiddlesProvider();
        riddlesProvider.loadRiddles(this);
        return null;
    }
    
    
    @Override
    public void onRiddlesLoaded(List<Riddle> riddles) {
        loadRiddlesToDb(riddles);
    }
    
    /**
     * loading riddles to local database
     *
     * @param riddles - riddles to be loaded to local database
     */
    private void loadRiddlesToDb(final List<Riddle> riddles) {
        Log.e(TAG, "Loading riddles to local database");
        for (int i = 0; i < riddles.size(); i++) {
            final int finalI = i;
            AppExecutors.getInstance(context.get()).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (kannadaRiddlesDatabase.kannadaRiddlesDao().hasRiddle(riddles.get(finalI).getRiddle()).getCount() == 0) {
                        KannadaRiddle kannadaRiddle = new KannadaRiddle(
                                riddles.get(finalI).getRiddle(),
                                riddles.get(finalI).getClues(),
                                riddles.get(finalI).getAnswer(),
                                false,
                                false
                        );
                        kannadaRiddlesDatabase.kannadaRiddlesDao().insertRiddle(kannadaRiddle);
                    } else {
                        Log.d(TAG, "Riddle exists in table. Skipping...");
                    }
                }
            });
        }
        Log.e(TAG, "Loading riddles to db finished");
        RiddlesLoadingScheduler.scheduleRiddlesLoading(context.get());
    }
}
