package dk.itu.simplequiz;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Resources res = getResources();
        Drawable winnerIcon = ResourcesCompat.getDrawable(res, R.drawable.crown, null);
        Drawable loserIcon = ResourcesCompat.getDrawable(res, R.drawable.skull, null);

        List<Player> listOfPlayers = Player.getRankedList();

        TextView winner = findViewById(R.id.winner);

        ImageView firstImg = findViewById(R.id.first_img);
        TextView firstName = findViewById(R.id.first_name);
        TextView firstScore = findViewById(R.id.first_score);
        ImageView firstIcon = findViewById(R.id.first_icon);

        ImageView secondImg = findViewById(R.id.second_img);
        TextView secondName = findViewById(R.id.second_name);
        TextView secondScore = findViewById(R.id.second_score);
        ImageView secondIcon = findViewById(R.id.second_icon);

        ImageView thirdImg = findViewById(R.id.third_img);
        TextView thirdName = findViewById(R.id.third_name);
        TextView thirdScore = findViewById(R.id.third_score);
        ImageView thirdIcon = findViewById(R.id.third_icon);

        ImageView fourthImg = findViewById(R.id.fourth_img);
        TextView fourthName = findViewById(R.id.fourth_name);
        TextView fourthScore = findViewById(R.id.fourth_score);
        ImageView fourthIcon = findViewById(R.id.fourth_icon);

        ImageView[] playerImages = {firstImg, secondImg, thirdImg, fourthImg};
        TextView[] playerNames = {firstName, secondName, thirdName, fourthName};
        TextView[] playerScores = {firstScore, secondScore, thirdScore, fourthScore};
        ImageView[] rankIcons = {firstIcon, secondIcon, thirdIcon, fourthIcon};

        winner.setText(Player.getWinner());

        for (int i = 0; i < listOfPlayers.size(); i++) {
            playerImages[i].setVisibility(View.VISIBLE);
            playerNames[i].setVisibility(View.VISIBLE);
            playerScores[i].setVisibility(View.VISIBLE);
            rankIcons[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < listOfPlayers.size(); i++) {
            Player p = listOfPlayers.get(i);
            playerImages[i].setImageDrawable(p.getImage());
            playerNames[i].setText(p.getName());
            //playerScores[i].setText(p.getScore());
            if (p.getScore() == listOfPlayers.get(0).getScore())
                rankIcons[i].setImageDrawable(winnerIcon);
        }

        if (Player.singleLowestScore())
            rankIcons[listOfPlayers.size()-1].setImageDrawable(loserIcon);

        Player.getListOfPlayers().clear();

        Button back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
