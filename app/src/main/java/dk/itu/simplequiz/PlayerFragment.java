package dk.itu.simplequiz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

public class PlayerFragment extends Fragment {

    private Context context;

    private EditText[] players;
    private Drawable[] images;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext(); // For easy use later on
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View playerMenu = inflater
                .inflate(R.layout.fragment_players, container, false);

        // EditTexts and Drawables
        EditText p1 = playerMenu.findViewById(R.id.first_name);
        EditText p2 = playerMenu.findViewById(R.id.second_name);
        EditText p3 = playerMenu.findViewById(R.id.third_name);
        EditText p4 = playerMenu.findViewById(R.id.fourth_name);
        players = new EditText[] {p1, p2, p3, p4};

        Resources res = getResources();
        Drawable img1 = ResourcesCompat.getDrawable(res, R.drawable.bear, null);
        Drawable img2 = ResourcesCompat.getDrawable(res, R.drawable.crocodile, null);
        Drawable img3 = ResourcesCompat.getDrawable(res, R.drawable.boar, null);
        Drawable img4 = ResourcesCompat.getDrawable(res, R.drawable.duck, null);
        images = new Drawable[] {img1, img2, img3, img4};

        // Players
        Player.initPlayers();

        // Button
        MaterialButton beginButton = playerMenu.findViewById(R.id.participants_begin);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check for minimum number of players
                if (!enoughPlayers()) {
                    Message.show(context, Message.NOT_ENOUGH_PLAYERS);
                    return; }

                // Add players
                for (int i = 0; i < players.length; i++) {
                    String name = players[i].getText().toString();
                    if (!name.equals(""))
                        Player.addPlayer(name, images[i]); }

                Navigation.findNavController(view)
                        .navigate(R.id.action_playerFragment_to_quizFragment); }
        });

        return playerMenu;
    }

    /**
     * This method checks whether the current contents of the EditText fields corresponds
     * to the minimum number of players required for the quiz (2).
     * @return true if at least 2 EditText fields contain text, false otherwise
     */
    private boolean enoughPlayers() {
        int num = 0;
        for (EditText field : players) {
            if (!field.getText().toString().equals("")) num++;
        } return num >= 2;
    }
}
