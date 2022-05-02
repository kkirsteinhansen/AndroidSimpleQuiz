package dk.itu.simplequiz;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends ViewModel implements Comparable<Player> {

    private String name;
    private int score;
    private Drawable img;
    private static List<Player> listOfPlayers;

    public Player(String name, Drawable img) {
        this.name = name;
        this.img = img;
        score = 0;
    }

    public static List<Player> initPlayers() {
        if (listOfPlayers == null) {
            listOfPlayers = new ArrayList<>();
            return listOfPlayers;
        }
        return listOfPlayers;
    }

    public int getScore() {
        return score;
    }

    public String getScoreString() {
        return "" + score;
    }

    public void addPoint() {
        score++;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return img;
    }

    public static void addPlayer(String name, Drawable img) {
        listOfPlayers.add(new Player(name, img));
    }

    public static List<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    @Override
    public int compareTo(Player that) {
        if (this.score > that.score) return 1;
        if (this.score < that.score) return -1;
        return 0;
    }

    public static List<Player> getRankedList() {
        List<Player> ranked = listOfPlayers;
        Collections.sort(ranked);
        Collections.reverse(ranked);
        return ranked;
    }

    public static boolean singleLowestScore() {
        List<Player> ranked = getRankedList();
        int lowestScore = ranked.get(ranked.size()-1).getScore();
        for (int i = 0; i < ranked.size()-1; i++) {
            if (ranked.get(i).getScore() == lowestScore) return false;
        }
        return true;
    }

    public static String getWinner() {
        List<Player> ranked = listOfPlayers;
        int highestScore = ranked.get(0).getScore();

        if (highestScore == 0) return "Uh oh! No one got a single point.";

        int numOfWinners = 1;
        for (int i = 1; i < ranked.size(); i++) {
            if (ranked.get(i).getScore() == highestScore) numOfWinners++; }

        String congrats = "Congratulations, ";

        switch (numOfWinners) {
            case 1: return congrats + ranked.get(0).getName() + "!";
            case 2: return congrats + ranked.get(0).getName() + " and " + ranked.get(1).getName() + "!";
            case 3: return congrats + ranked.get(0).getName() + ", " + ranked.get(1).getName() + "\nand "
                    + ranked.get(2).getName() + "!";
        } return congrats + "everyone!"; }
}
