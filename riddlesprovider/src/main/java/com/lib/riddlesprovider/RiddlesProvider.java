package com.lib.riddlesprovider;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lib.riddlesprovider.model.Riddle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varun.am on 04/11/18
 */
public class RiddlesProvider {
    
    private static final String TAG = RiddlesProvider.class.getSimpleName();
    private static final String KANNADA_RIDDLES = "KannadaRiddles";
    
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    
    public RiddlesProvider() {
        Log.d(TAG, "Creating RiddlesProvider instance");
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Log.e(TAG,"Couldn't set persistence as it was already set");
            e.printStackTrace();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(KANNADA_RIDDLES);
    }
    
    public void loadRiddles(final RiddlesLoadedCallbacks riddlesLoadedCallbacks) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Riddle> riddleList = new ArrayList<>();
                for (DataSnapshot riddleSnapShot : dataSnapshot.getChildren()) {
                    riddleList.add(riddleSnapShot.getValue(Riddle.class));
                    Log.e(TAG, riddleList.size() + ":" + riddleSnapShot.getValue().toString());
                }
                riddlesLoadedCallbacks.onRiddlesLoaded(riddleList);
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
    }
    
}
