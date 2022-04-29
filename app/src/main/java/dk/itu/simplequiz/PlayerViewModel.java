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

    public void addPoint(Player p) {
        p.addPoint();
        livePlayers.setValue(Player.initPlayers());
    }
}
