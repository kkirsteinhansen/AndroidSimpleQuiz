package dk.itu.simplequiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class PlayerActivity extends AppCompatActivity {

    private Button beginButton, backButton;
    private Context context;
    private EditText[] players;
    private Drawable[] images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        context = PlayerActivity.this;

        // Buttons
        beginButton = findViewById(R.id.participants_begin);
        backButton = findViewById(R.id.back_to_main);

        // EditTexts
        EditText p1 = findViewById(R.id.first_name);
        EditText p2 = findViewById(R.id.second_name);
        EditText p3 = findViewById(R.id.third_name);
        EditText p4 = findViewById(R.id.fourth_name);
        players = new EditText[] {p1, p2, p3, p4};

        Resources res = getResources();
        Drawable img1 = ResourcesCompat.getDrawable(res, R.drawable.bear, null);
        Drawable img2 = ResourcesCompat.getDrawable(res, R.drawable.crocodile, null);
        Drawable img3 = ResourcesCompat.getDrawable(res, R.drawable.boar, null);
        Drawable img4 = ResourcesCompat.getDrawable(res, R.drawable.duck, null);
        images = new Drawable[] {img1, img2, img3, img4};

        // Players
        Player.initPlayers();


        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!enoughPlayers()) {
                    Message.show(context, Message.NOT_ENOUGH_PLAYERS);
                    return;
                }

                for (int i = 0; i < players.length; i++) {
                    String name = players[i].getText().toString();
                    if (!name.equals(""))
                        Player.addPlayer(name, images[i]);
                }

                Intent intent = new Intent(context, QuizActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean enoughPlayers() {
        int num = 0;
        for (EditText field : players) {
            if (!field.getText().toString().equals("")) num++;
        } return num >= 2;
    }
}
