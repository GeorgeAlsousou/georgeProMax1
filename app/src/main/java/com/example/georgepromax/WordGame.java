package com.example.georgepromax;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Random;

public class WordGame extends AppCompatActivity implements View.OnClickListener {
    private TextView wordGameTitle,lettersCount;
    private EditText txtUserWord;
    private ImageButton sendWord;
    private ImageButton info;
    private Button[] btnArr;
    private int[] counter;

    Utility utility=new Utility();
    private String[] words={"table"+"chair"+"apple"+"computer"+"school","shirt","pants"};
    private String theWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game);

        init();
    }

    public void init(){
        counter=new int[26];
        Arrays.fill(counter, 0);

        btnArr=new Button[26];
        btnArr[0]=findViewById(R.id.btn00);
        btnArr[1]=findViewById(R.id.btn01);
        btnArr[2]=findViewById(R.id.btn02);
        btnArr[3]=findViewById(R.id.btn03);
        btnArr[4]=findViewById(R.id.btn04);
        btnArr[5]=findViewById(R.id.btn05);
        btnArr[6]=findViewById(R.id.btn06);
        btnArr[7]=findViewById(R.id.btn07);
        btnArr[8]=findViewById(R.id.btn08);
        btnArr[9]=findViewById(R.id.btn09);
        btnArr[10]=findViewById(R.id.btn10);
        btnArr[11]=findViewById(R.id.btn11);
        btnArr[12]=findViewById(R.id.btn12);
        btnArr[13]=findViewById(R.id.btn13);
        btnArr[14]=findViewById(R.id.btn14);
        btnArr[15]=findViewById(R.id.btn15);
        btnArr[16]=findViewById(R.id.btn16);
        btnArr[17]=findViewById(R.id.btn17);
        btnArr[18]=findViewById(R.id.btn18);
        btnArr[19]=findViewById(R.id.btn19);
        btnArr[20]=findViewById(R.id.btn20);
        btnArr[21]=findViewById(R.id.btn21);
        btnArr[22]=findViewById(R.id.btn22);
        btnArr[23]=findViewById(R.id.btn23);
        btnArr[24]=findViewById(R.id.btn24);
        btnArr[25]=findViewById(R.id.btn25);


        wordGameTitle=findViewById(R.id.wordGameTitle);
        lettersCount=findViewById(R.id.lettersCount);
        txtUserWord=findViewById(R.id.txtUserWord);
        sendWord=findViewById(R.id.sendWord);
        info=findViewById(R.id.info);

        Random random=new Random();
        theWord=words[random.nextInt(5)];
        sendWord.setOnClickListener(this);
        info.setOnClickListener(this);

        for(int i=0; i<btnArr.length; i++) {
            btnArr[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if(view==sendWord) {
            if (txtUserWord.getText().toString().equals(theWord)) {
                lettersCount.setText("B I N G O !!!");
            }
            else {
                int num = utility.sharedLetters(txtUserWord.getText().toString(), theWord);
                lettersCount.setText(num + " shared letters");
            }
        }
        else if(view==info){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("How to play");
            alertDialog.setIcon(R.drawable.help);
            alertDialog.setMessage("You need to guess the word by typing words of your own.\n"+"" +
                    "You can mark which letters you think are possible and which are not by clicking on the letters below.");
            alertDialog.setNeutralButton("ok",null);
            alertDialog.show();
        }
        else userMarkLetters(view);
    }

    public void userMarkLetters(View view) {
        for(int i=0; i< btnArr.length; i++){
            if(btnArr[i]==view) {
                if (counter[i] == 0) {
                    btnArr[i].setBackgroundColor(Color.GREEN);
                    btnArr[i].setTextColor(Color.BLACK);
                    counter[i]++;
                }
                else if(counter[i]==1){
                    btnArr[i].setBackgroundColor(Color.RED);
                    btnArr[i].setTextColor(Color.WHITE);
                    counter[i]++;
                }
                else if(counter[i]==2){
                    btnArr[i].setBackgroundColor(Color.rgb(72,72,72));
                    btnArr[i].setTextColor(Color.WHITE);
                    counter[i]=0;
                }
            }
        }
    }
}