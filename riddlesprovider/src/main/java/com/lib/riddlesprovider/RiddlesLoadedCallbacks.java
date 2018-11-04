package com.lib.riddlesprovider;

import com.lib.riddlesprovider.model.Riddle;

import java.util.List;

/**
 * Created by varun.am on 04/11/18
 */
public interface RiddlesLoadedCallbacks {
    void onRiddlesLoaded(List<Riddle> riddles);
}
