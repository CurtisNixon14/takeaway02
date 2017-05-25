package com.nixon05.takeaway;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


import java.util.ArrayList;
import java.util.List;

import static com.nixon05.takeaway.StartActivity.numberOfPiles;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "Game Activty";

    private int totalCoins;
    private int pileRows;
    private boolean playersTurn = true;
    private List<Integer> coinPiles;
    private Animation selectedPileAnimation;


    private LinearLayout gameLinearLayout;
    private TextView playerTurn;
    private Button pile1Coins;
    private Button pile2Coins;
    private Button pile3Coins;
    private Button pile4Coins;
    private Button pile5Coins;
    private Button pile6Coins;
    private Button pile7Coins;
    private Button pile8Coins;
    private Button endTurn;
    private Button deselectButtons;
    private LinearLayout[] rowsLinearLayout;
    private EditText coinsToTake;
    private Animation shakeAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.v(TAG, "On Create");

        //load selected animation
        selectedPileAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.incorrect_shake);
        selectedPileAnimation.setRepeatCount(3);


        //shake animation used when the player clicks end turn
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);
        //get reference to GUI components
        gameLinearLayout = (LinearLayout) findViewById(R.id.gameLinearLayout);

        playerTurn = (TextView)findViewById(R.id.txtPlayerTurn);

        endTurn = (Button) findViewById(R.id.btnEndTurn);
        endTurn.setOnClickListener(endTurnListener);
        endTurn.setEnabled(false);

        deselectButtons = (Button) findViewById(R.id.btnDeselect);
        deselectButtons.setOnClickListener(deselectListener);

        pile1Coins = (Button) findViewById(R.id.btnPile1);
        pile2Coins = (Button) findViewById(R.id.btnPile2);
        pile3Coins = (Button) findViewById(R.id.btnPile3);
        pile4Coins = (Button) findViewById(R.id.btnPile4);
        pile5Coins = (Button) findViewById(R.id.btnPile5);
        pile6Coins = (Button) findViewById(R.id.btnPile6);
        pile7Coins = (Button) findViewById(R.id.btnPile7);
        pile8Coins = (Button) findViewById(R.id.btnPile8);

        coinsToTake = (EditText) findViewById(R.id.numberEntered);

        rowsLinearLayout = new LinearLayout[4];

        rowsLinearLayout[0] = (LinearLayout) findViewById(R.id.row1LinearLayout);
        rowsLinearLayout[1] = (LinearLayout) findViewById(R.id.row2LinearLayout);
        rowsLinearLayout[2] = (LinearLayout) findViewById(R.id.row3LinearLayout);
        rowsLinearLayout[3] = (LinearLayout) findViewById(R.id.row4LinearLayout);

        //configure listenters for the pile rows
        for(LinearLayout row : rowsLinearLayout){
            for(int column = 0; column < row.getChildCount(); column++){
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(pileButtonListener);
            }
        }


        coinPiles = new ArrayList<>();
        //fill each "pile with ten coins
        for(int i = 0; i < numberOfPiles; i++){
            coinPiles.add(10);
        }
        Log.v(TAG, "size of array list" + coinPiles.size());
        //set number of coins in each pile
        //set number of coins in each pile
        pile1Coins.setText(getString(R.string.pile1, 10));
        pile2Coins.setText(getString(R.string.pile2, 10));
        pile3Coins.setText(getString(R.string.pile3, 10));
        pile4Coins.setText(getString(R.string.pile4, 10));
        pile5Coins.setText(getString(R.string.pile5, 10));
        pile6Coins.setText(getString(R.string.pile6, 10));
        pile7Coins.setText(getString(R.string.pile7, 10));
        pile8Coins.setText(getString(R.string.pile8, 10));
        Log.v(TAG,"coiPiles" + coinPiles.toString());

        //hide all pile LinearLayouts
        for(LinearLayout layout: rowsLinearLayout)
            layout.setVisibility(View.GONE);

        //display appropriate number of pile Linear Layouts
        for(int row = 0; row < numberOfPiles/2; row++){
            rowsLinearLayout[row].setVisibility(View.VISIBLE);
        }

        //display the players turn
        playerTurn.setText(getString(R.string.player_turn, 1));

    }

    private int countCoins(){
        int coins = 0;
        for(int i = 0; i < coinPiles.size(); i++){
            coins += coinPiles.get(i);
        }
        return coins;
    }

    private OnClickListener pileButtonListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            Button selectedButton = ((Button) v);
            selectedButton.startAnimation(shakeAnimation);
            Log.v(TAG, "Other button clicked");
            endTurn.setEnabled(true);
            disableButtons();
            selectedButton.setEnabled(true);

        }

    };

    private OnClickListener endTurnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            endTurn();
        }
    };

    private OnClickListener deselectListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            enableButtons();
        }
    };

    public void endTurn(){
        for(int row = 0 ; row < numberOfPiles/2; row++){
            LinearLayout piles = rowsLinearLayout[row];
            for(int i = 0; i < piles.getChildCount(); i++){
                if(piles.getChildAt(i).isEnabled()){
                    endTurn.startAnimation(shakeAnimation);
                    enableButtons();

                    if(coinsToTake.getText().toString().equals("")){
                        Toast.makeText(GameActivity.this, "You must add an integer into the text field above the End Turn Button before pushing the End Turn button", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int numberEntered = getNumberEntered();
                    removeCoins(row, i, numberEntered);
                    int h = countCoins();
                    Log.v(TAG, Integer.toString(h));
                    if(h == 0){
                        endGame();

                    }
                    displayCorrectCoins(row, i);
                    switchPlayersTurn();
                    return;
                }
            }
        }

    }
    private void returnToStart(){
        Intent startActivity = new Intent(this, StartActivity.class);
        startActivity(startActivity);
    }
    private void endGame(){
        Log.v(TAG, "end game called");
        int winner;
        if(playersTurn){
            winner = 1;
        }else{
            winner = 2;
        }
        Toast.makeText(GameActivity.this, "Player " + winner + " Wins!!!", Toast.LENGTH_LONG).show();
        returnToStart();

    }
    private void switchPlayersTurn(){
        if(playersTurn){
            playerTurn.setText(getString(R.string.player_turn, 2));
            playersTurn = false;
        }else{
            playerTurn.setText(getString(R.string.player_turn, 1));
            playersTurn = true;
        }
        return;
    }
    private void displayCorrectCoins(int row, int column){
        switch(row){
            case 0:
                if(column == 0){
                    pile1Coins.setText(getString(R.string.pile1, coinPiles.get(0)));
                }else{
                    pile2Coins.setText(getString(R.string.pile2, coinPiles.get(1)));
                }
                return;
            case 1:
                if(column == 0){
                    pile3Coins.setText(getString(R.string.pile3, coinPiles.get(2)));
                }else{
                    pile4Coins.setText(getString(R.string.pile4, coinPiles.get(3)));
                }
                return;
            case 2:
                if(column == 0){
                    pile5Coins.setText(getString(R.string.pile5, coinPiles.get(4)));
                }else{
                    pile6Coins.setText(getString(R.string.pile6, coinPiles.get(5)));
                }
                return;
            case 3:
                if(column == 0){
                    pile7Coins.setText(getString(R.string.pile7, coinPiles.get(6)));
                }else{
                    pile8Coins.setText(getString(R.string.pile8, coinPiles.get(7)));
                }
                return;
        }
    }
    private int getNumberEntered(){
            String numb = coinsToTake.getText().toString();
            int n = Integer.parseInt(numb);
            coinsToTake.setText("");
            return n;
    }

    private void removeCoins(int row, int column, int coins){
        switch(row){
            case 0:
                if(column == 0){
                    if(coins > coinPiles.get(0)){
                        coinPiles.set(0,0);
                    }else{
                        coinPiles.set(0,coinPiles.get(0) - coins);
                    }
                }else{
                    if(coins > coinPiles.get(1)){
                        coinPiles.set(1,0);
                    }else{
                        coinPiles.set(1, coinPiles.get(1) - coins);
                    }
                }
                return;
            case 1:
                if(column == 0){
                    if(coins > coinPiles.get(2)){
                        coinPiles.set(2,0);
                    }else{
                        coinPiles.set(2,coinPiles.get(2) - coins);
                    }
                }else{
                    if(coins > coinPiles.get(3)){
                        coinPiles.set(3,0);
                    }else{
                        coinPiles.set(3, coinPiles.get(3) - coins);
                    }
                }
                return;
            case 2:
                if(column == 0){
                    if( coins > coinPiles.get(4)){
                        coinPiles.set(4,0);
                    }else{
                        coinPiles.set(4,coinPiles.get(4) - coins);
                    }
                }else{
                    if(coins > coinPiles.get(5)){
                        coinPiles.set(5,0);
                    }else{
                        coinPiles.set(5,coinPiles.get(5) - coins);
                    }
                }
                return;
            case 3:
                if(column == 0){
                    if(coins > coinPiles.get(6)){
                        coinPiles.set(6,0);
                    }else{
                        coinPiles.set(6,coinPiles.get(6) - coins);
                    }
                }else{
                    if(coins > coinPiles.get(7)){
                        coinPiles.set(7,0);
                    }else{
                        coinPiles.set(7,coinPiles.get(7) - coins);
                    }
                }
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.takeaway_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.reset_quiz:
                //call reset quiz method
                Toast.makeText(GameActivity.this, "Reset Game", Toast.LENGTH_SHORT).show();
                returnToStart();
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void disableButtons(){
        for(int row = 0; row < numberOfPiles/2; row++){
            LinearLayout piles = rowsLinearLayout[row];
            for(int i = 0; i < piles.getChildCount(); i++)
                piles.getChildAt(i).setEnabled(false);
        }
    }

    //enable buttons if the user wants to change pile selected
    private void enableButtons(){
        for(int row = 0; row < numberOfPiles/2; row++){
            LinearLayout piles = rowsLinearLayout[row];
            for(int i = 0; i < piles.getChildCount(); i++)
                piles.getChildAt(i).setEnabled(true);
        }
    }
}
