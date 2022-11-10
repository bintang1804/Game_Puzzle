package com.example.tugas4bintang;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button[][] buttons;
    private int emptyX = 3;
    private int emptyY = 3;
    private RelativeLayout group;
    private char[] tiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadViews();
        loadLetters();
        generateLetters();
        loadDataToViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ulangi){
            loadViews();
            loadLetters();
            generateLetters();
            loadDataToViews();
        }else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;

        for (int i = 0; i < group.getChildCount() - 1; i++){
            buttons[i/4][i%4].setText(String.valueOf(tiles[i]));
            buttons[i/4][i%4].setBackgroundResource(android.R.drawable.btn_default);
        }

        buttons[emptyX][emptyY].setText("");
        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this, R.color.kosong));
    }

    private void generateLetters() {
        int x = 15;
        Random random = new Random();

        while (x>1){
            int randomNum = random.nextInt(x--);
            char temp = tiles[randomNum];
            tiles[randomNum] = tiles[x];
            tiles[x] = temp;
        }

        if (!isSolvable()){
            generateLetters();
        }
    }

    private boolean isSolvable() {
        int countInvaersion = 0;

        for (int i = 0; i<15; i++){
            for (int j = 0; j < i; j++){
                if (tiles[j] > tiles[i]){
                    countInvaersion++;
                }
            }
        }
        return countInvaersion%2 == 0;
    }

    private void loadLetters() {
        tiles = new char[16];
        for (int i = 0; i < group.getChildCount() - 1; i++){
            tiles[i] =  (char) ('A' + i);
        }
    }

    private void loadViews() {
        group = findViewById(R.id.group);
        buttons = new Button[4][4];

        for (int i = 0; i < group.getChildCount(); i++){
            buttons[i/4][i%4] = (Button) group.getChildAt(i);
        }
    }

    public void buttonClick(View view){
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0)-'0';
        int y = button.getTag().toString().charAt(1)-'0';

        if ((Math.abs(emptyX - x)==1 && emptyY==y)||(Math.abs(emptyY - y)==1 && emptyX==x)){
            buttons[emptyX][emptyY].setText(button.getText().toString());
            buttons[emptyX][emptyY].setBackgroundResource(android.R.drawable.btn_default);

            button.setText("");
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.kosong));
            emptyX = x;
            emptyY = y;
            checkWin();
        }

    }

    private void checkWin() {
        boolean isWin = false;
        char n;
        if (emptyX == 3 && emptyY == 3){
            for (int i = 0; i < group.getChildCount()-1; i++) {
                n = (char) ('A' + i);
                if (buttons[i/4][i%4].getText().toString().equals(String.valueOf(n))){
                    isWin = true;
                }else{
                    isWin = false;
                    break;
                }
            }
        }

        if (isWin){
            Toast.makeText(this, "Game, You Won It", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < group.getChildCount(); i++){
                buttons[i/4][i%4].setClickable(false);
            }
        }
    }
}