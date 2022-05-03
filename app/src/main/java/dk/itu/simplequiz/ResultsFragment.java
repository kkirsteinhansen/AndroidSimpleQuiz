package dk.itu.simplequiz;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ResultsFragment extends Fragment {

    List<Player> playerRanking;

    Drawable crown;
    Drawable skull;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        crown = ResourcesCompat.getDrawable(res, R.drawable.crown, null);
        skull = ResourcesCompat.getDrawable(res, R.drawable.skull, null);

        playerRanking = Player.getRankedList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View results = inflater
                .inflate(R.layout.fragment_results, container, false);

        // TextViews
        TextView winner = results.findViewById(R.id.winner);

        TextView firstName = results.findViewById(R.id.first_name);
        TextView secondName = results.findViewById(R.id.second_name);
        TextView thirdName = results.findViewById(R.id.third_name);
        TextView fourthName = results.findViewById(R.id.fourth_name);
        TextView[] playerNames = {firstName, secondName, thirdName, fourthName};

        TextView firstScore = results.findViewById(R.id.first_score);
        TextView secondScore = results.findViewById(R.id.second_score);
        TextView thirdScore = results.findViewById(R.id.third_score);
        TextView fourthScore = results.findViewById(R.id.fourth_score);
        TextView[] playerScores = {firstScore, secondScore, thirdScore, fourthScore};

        ImageView firstImg = results.findViewById(R.id.first_img);
        ImageView secondImg = results.findViewById(R.id.second_img);
        ImageView thirdImg = results.findViewById(R.id.third_img);
        ImageView fourthImg = results.findViewById(R.id.fourth_img);
        ImageView[] playerImages = {firstImg, secondImg, thirdImg, fourthImg};

        ImageView firstIcon = results.findViewById(R.id.first_icon);
        ImageView secondIcon = results.findViewById(R.id.second_icon);
        ImageView thirdIcon = results.findViewById(R.id.third_icon);
        ImageView fourthIcon = results.findViewById(R.id.fourth_icon);
        ImageView[] rankIcons = {firstIcon, secondIcon, thirdIcon, fourthIcon};

        // Set winner string
        winner.setText(Player.getWinner());

        // Make number of visible rows correspond to number of players
        for (int i = 0; i < playerRanking.size(); i++) {
            playerImages[i].setVisibility(View.VISIBLE);
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE);
            rankIcons[i].setVisibility(View.VISIBLE);
        }

        // Set crown icon for top scores
        for (int i = 0; i < playerRanking.size(); i++) {
            Player p = playerRanking.get(i);
            playerImages[i].setImageDrawable(p.getImage());
            playerNames[i].setText(p.getName());
            playerScores[i].setText(p.getScoreString());
            if (p.getScore() == playerRanking.get(0).getScore())
                if (p.getScore() == 0) rankIcons[i].setImageDrawable(skull);
                else rankIcons[i].setImageDrawable(crown);
        }

        // Set skull icon for lowest score only
        if (Player.singleLowestScore())
            rankIcons[playerRanking.size()-1].setImageDrawable(skull);

        // Clear list of players to avoid problems when starting a new game
        Player.getListOfPlayers().clear();

        // Button
        MaterialButton back = results.findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_resultsFragment_to_mainMenuFragment); }
        });

        return results;
    }
}
