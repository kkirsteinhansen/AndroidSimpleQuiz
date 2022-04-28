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

public class ScoreFragment extends Fragment {

    private PlayerViewModel livePlayers;
    private TextView scores;

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

        scores = v.findViewById(R.id.scores);

        scores.setText(livePlayers.getCurrentScores());

        livePlayers.getObservable().observe(getViewLifecycleOwner(),
                currentScores -> scores.setText(livePlayers.getCurrentScores()));

        return v;
    }

}
