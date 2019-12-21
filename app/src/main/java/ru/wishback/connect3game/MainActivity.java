package ru.wishback.connect3game;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    int duration = 1500;

    GameBrain game;
    
    ArrayList<ImageView> yellows0;
    ArrayList<ImageView> yellows1;
    ArrayList<ImageView> yellows2;
    ArrayList<ArrayList<ImageView>> yellows;
    
    ArrayList<ImageView> reds0;
    ArrayList<ImageView> reds1;
    ArrayList<ImageView> reds2;
    ArrayList<ArrayList<ImageView>> reds;

    void loadView() {
        yellows0 = new ArrayList();
        yellows1 = new ArrayList();
        yellows2 = new ArrayList();
        yellows  = new ArrayList();

        yellows0.add((ImageView) findViewById(R.id.yellow00));
        yellows0.add((ImageView) findViewById(R.id.yellow01));
        yellows0.add((ImageView) findViewById(R.id.yellow02));

        yellows1.add((ImageView) findViewById(R.id.yellow10));
        yellows1.add((ImageView) findViewById(R.id.yellow11));
        yellows1.add((ImageView) findViewById(R.id.yellow12));

        yellows2.add((ImageView) findViewById(R.id.yellow20));
        yellows2.add((ImageView) findViewById(R.id.yellow21));
        yellows2.add((ImageView) findViewById(R.id.yellow22));

        yellows.add(yellows0); yellows.add(yellows1); yellows.add(yellows2);

        reds0 = new ArrayList();
        reds1 = new ArrayList();
        reds2 = new ArrayList();
        reds  = new ArrayList();

        reds0.add((ImageView) findViewById(R.id.red00));
        reds0.add((ImageView) findViewById(R.id.red01));
        reds0.add((ImageView) findViewById(R.id.red02));

        reds1.add((ImageView) findViewById(R.id.red10));
        reds1.add((ImageView) findViewById(R.id.red11));
        reds1.add((ImageView) findViewById(R.id.red12));

        reds2.add((ImageView) findViewById(R.id.red20));
        reds2.add((ImageView) findViewById(R.id.red21));
        reds2.add((ImageView) findViewById(R.id.red22));

        reds.add(reds0); reds.add(reds1); reds.add(reds2);
    }

    protected ImageView getYellowByTag(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        return yellows.get(i).get(j);
    }

    protected ImageView getRedByTag(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        return reds.get(i).get(j);
    }

    protected void hideYellows() {
        for (int i = 0; i < yellows.size(); i++) {
            for (int j = 0; j < yellows.get(i).size() ; j++) {
                yellows.get(i).get(j).animate().alpha(0).setDuration(1);
            }
        }
    }

    protected void hideReds() {
        for (int i = 0; i < reds.size(); i++) {
            for (int j = 0; j < reds.get(i).size() ; j++) {
                reds.get(i).get(j).animate().alpha(0).setDuration(1);
            }
        }
    }

    protected void hideRed(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        reds.get(i).get(j).animate().alpha(0).setDuration(duration);
    }

    protected void hideYellow(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        yellows.get(i).get(j).animate().alpha(0).setDuration(duration);
    }

    protected void animateRed(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        reds.get(i).get(j).animate().alpha(1).translationY(-1000);
        reds.get(i).get(j).animate().translationY(0).setDuration(duration);
    }

    protected void animateYellow(String tag) {
        int index = Integer.parseInt(tag);
        int i = index / 10;
        int j = index % 10;
        yellows.get(i).get(j).animate().alpha(1).translationY(-1000);
        yellows.get(i).get(j).animate().translationY(0).setDuration(duration);
    }

    @Override
    protected void onStart () {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadView();
        game = new GameBrain();
        start();

    }

    public void start() {
        this.hideReds();
        this.hideYellows();
        this.game.newgame();
        TextView message = (TextView) findViewById(R.id.message);
        message.setText("New game");
    }

    public void startNewGame(View view) {
        Log.i("Start New Game", "Pressed");
        start();
    }

    public void moveHasBeenMade(View view) {
        ImageView imageView = (ImageView) view;
        int id = imageView.getId();
        String tag = imageView.getTag().toString();
        Log.i("Move has been made", "Pressed. Id: " + id + " Tag: " + tag);
        ImageView targetView = this.getRedByTag(tag);
        Log.i("Target View", targetView.toString());
        TextView message = (TextView) findViewById(R.id.message);
        if (game.status != GameBrain.GameStatus.paly) {
            message.setText(this.game.win);
            return;
        }
        if ( game.isValid(tag) ) { animateRed(tag); }
        if (this.game.play(tag)) {
            if (this.game.whoIsNext == "") {
                animateYellow(this.game.lastMove);
                message.setText(this.game.win + " has won");
            } else {
                animateYellow(this.game.lastMove);
                if (this.game.win != "" ) {
                    message.setText(this.game.win);
                } else {
                    message.setText("Play");
                }
            }
        } else {
            message.setText("Wrong move");
        }

    }
}
