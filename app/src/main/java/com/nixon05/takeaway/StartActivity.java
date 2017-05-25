package com.nixon05.takeaway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class StartActivity extends AppCompatActivity {

    public static int numberOfPiles = 0;

    private RadioButton twoPiles;
    private RadioButton fourPiles;
    private RadioButton sixPiles;
    private RadioButton eightPiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        twoPiles = (RadioButton) findViewById(R.id.radTwoPiles);
        fourPiles = (RadioButton) findViewById(R.id.radFourPiles);
        sixPiles = (RadioButton) findViewById(R.id.radSixPiles);
        eightPiles = (RadioButton) findViewById(R.id.radEightPiles);

        AppRater.app_launched(this);
    }
    public void startGame(View view){
        if(twoPiles.isChecked()){
            numberOfPiles = 2;
        }else if(fourPiles.isChecked()){
            numberOfPiles = 4;
        }else if(sixPiles.isChecked()){
            numberOfPiles = 6;
        }else{
            numberOfPiles = 8;
        }
        Intent gameActivity = new Intent(this, GameActivity.class);
        startActivity(gameActivity);

    }
}
