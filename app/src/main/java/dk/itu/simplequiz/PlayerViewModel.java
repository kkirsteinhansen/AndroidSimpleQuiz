package dk.itu.simplequiz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private static MutableLiveData<List<Player>> livePlayers;

    public PlayerViewModel(@NonNull Application app) {
        super(app);
        if (livePlayers == null) {
            livePlayers = new MutableLiveData<>();
            livePlayers.setValue(Player.initPlayers());
        }
    }

    public MutableLiveData<List<Player>> getObservable() {
        return livePlayers;
    }

    /**
     * This method is a wrapper method for the corresponding addPoint() method in
     * the Player class. This method calls the original method and then resets the
     * value of the MutableLiveData object. This ensures that the observer is
     * notified of a change in the data.
     * @param p the player who should receive a point
     */
    public void addPoint(Player p) {
        p.addPoint();
        livePlayers.setValue(Player.initPlayers());
    }
}
