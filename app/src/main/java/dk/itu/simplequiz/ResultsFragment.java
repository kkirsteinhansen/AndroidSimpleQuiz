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

    Drawable crown;
    Drawable skull;
    List<Player> playerRanking;

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

        View results = inflater.inflate(R.layout.fragment_results, container, false);

        TextView winner = results.findViewById(R.id.winner);

        ImageView firstImg = results.findViewById(R.id.first_img);
        TextView firstName = results.findViewById(R.id.first_name);
        TextView firstScore = results.findViewById(R.id.first_score);
        ImageView firstIcon = results.findViewById(R.id.first_icon);

        ImageView secondImg = results.findViewById(R.id.second_img);
        TextView secondName = results.findViewById(R.id.second_name);
        TextView secondScore = results.findViewById(R.id.second_score);
        ImageView secondIcon = results.findViewById(R.id.second_icon);

        ImageView thirdImg = results.findViewById(R.id.third_img);
        TextView thirdName = results.findViewById(R.id.third_name);
        TextView thirdScore = results.findViewById(R.id.third_score);
        ImageView thirdIcon = results.findViewById(R.id.third_icon);

        ImageView fourthImg = results.findViewById(R.id.fourth_img);
        TextView fourthName = results.findViewById(R.id.fourth_name);
        TextView fourthScore = results.findViewById(R.id.fourth_score);
        ImageView fourthIcon = results.findViewById(R.id.fourth_icon);

        ImageView[] playerImages = {firstImg, secondImg, thirdImg, fourthImg};
        TextView[] playerNames = {firstName, secondName, thirdName, fourthName};
        TextView[] playerScores = {firstScore, secondScore, thirdScore, fourthScore};
        ImageView[] rankIcons = {firstIcon, secondIcon, thirdIcon, fourthIcon};

        winner.setText(Player.getWinner());

        for (int i = 0; i < playerRanking.size(); i++) {
            playerImages[i].setVisibility(View.VISIBLE);
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE);
            rankIcons[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < playerRanking.size(); i++) {
            Player p = playerRanking.get(i);
            playerImages[i].setImageDrawable(p.getImage());
            playerNames[i].setText(p.getName());
            playerScores[i].setText(p.getScoreString());
            if (p.getScore() == playerRanking.get(0).getScore())
                if (p.getScore() == 0) rankIcons[i].setImageDrawable(skull);
                else rankIcons[i].setImageDrawable(crown);
        }

        if (Player.singleLowestScore())
            rankIcons[playerRanking.size()-1].setImageDrawable(skull);

        Player.getListOfPlayers().clear();

        MaterialButton back = results.findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
                        .navigate(R.id.action_resultsFragment_to_mainMenuFragment);
            }
        });

        return results;
    }

}
