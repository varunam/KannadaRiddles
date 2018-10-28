package app.kannadariddles.com.data.firebasedatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.kannadariddles.com.model.Riddle;
import app.kannadariddles.com.viewmodels.MainViewModel;

/**
 * Created by varun.am on 28/10/18
 */
public class FirebaseDB {
    
    private static final String TAG = FirebaseDB.class.getSimpleName();
    private static final String KANNADA_RIDDLES = "KannadaRiddles";
    
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MainViewModel mainViewModel;
    
    public FirebaseDB(AppCompatActivity activity) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(KANNADA_RIDDLES);
        //enabling offline capabilities
        mainViewModel = ViewModelProviders.of(activity).get(MainViewModel.class);
    }
    
    public void init() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Riddle riddle = dataSnapshot.getValue(Riddle.class);
                if (riddle != null) {
                    List<Riddle> riddleList = new ArrayList<>();
                    riddleList.add(riddle);
                    mainViewModel.riddles.postValue(riddleList);
                    Log.e(TAG, riddle.getRiddle() + "\n" + riddle.getClues() + "\n" + riddle.getAnswer());
                } else
                    Log.e(TAG, "Riddle received is NULL");
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
    }
    
}
