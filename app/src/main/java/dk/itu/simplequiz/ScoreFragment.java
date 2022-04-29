package dk.itu.simplequiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class ScoreFragment extends Fragment {

    private PlayerViewModel livePlayers;
    private TextView[] playerNames;
    private TextView[] playerScores;
    private int numOfPlayers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        livePlayers = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_score, container, false);

        TextView p1_name = v.findViewById(R.id.p1_name_sc);
        TextView p2_name = v.findViewById(R.id.p2_name_sc);
        TextView p3_name = v.findViewById(R.id.p3_name_sc);
        TextView p4_name = v.findViewById(R.id.p4_name_sc);
        playerNames = new TextView[] {p1_name, p2_name, p3_name, p4_name};

        TextView p1_score = v.findViewById(R.id.p1_score_sc);
        TextView p2_score = v.findViewById(R.id.p2_score_sc);
        TextView p3_score = v.findViewById(R.id.p3_score_sc);
        TextView p4_score = v.findViewById(R.id.p4_score_sc);
        playerScores = new TextView[] {p1_score, p2_score, p3_score, p4_score};

        numOfPlayers = Player.getListOfPlayers().size();
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE); }

        updateScores();

        livePlayers.getObservable().observe(getViewLifecycleOwner(),
                scores -> updateScores());

        return v;
    }

    private void updateScores() {
        List<Player> players = Player.getListOfPlayers();
        for (int i = 0; i < numOfPlayers; i++) {
            playerNames[i].setText(players.get(i).getName());
            playerScores[i].setText((players.get(i).getScoreString()));
        }
    }

}
