package com.example.georgepromax;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class fourInAline extends AppCompatActivity implements View.OnClickListener {

    private TextView txtFourTitle;

    private ImageView [] btn;
    private int turns=1; // 1 red // 2 blue

    private ImageView [][] grid;
    private int [][] gridMone; //0 nothing // 1 red // 2 blue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_in_aline);

        init();
    }

    public void init(){
        fvbid();

        for(int i=0;i<7;i++) {
            btn[i].setOnClickListener(this);
        }
    }

    private void fvbid() {
        txtFourTitle=findViewById(R.id.txtFourTitle);
        btn=new ImageView[7];
        btn[0]=findViewById(R.id.gridBtn00);
        btn[1]=findViewById(R.id.gridBtn01);
        btn[2]=findViewById(R.id.gridBtn02);
        btn[3]=findViewById(R.id.gridBtn03);
        btn[4]=findViewById(R.id.gridBtn04);
        btn[5]=findViewById(R.id.gridBtn05);
        btn[6]=findViewById(R.id.gridBtn06);

        grid=new ImageView[7][6];
        gridMone=new int[7][6];

        grid[0][0]=findViewById(R.id.grid00);
        grid[1][0]=findViewById(R.id.grid01);
        grid[2][0]=findViewById(R.id.grid02);
        grid[3][0]=findViewById(R.id.grid03);
        grid[4][0]=findViewById(R.id.grid04);
        grid[5][0]=findViewById(R.id.grid05);
        grid[6][0]=findViewById(R.id.grid06);

        grid[0][1]=findViewById(R.id.grid07);
        grid[1][1]=findViewById(R.id.grid08);
        grid[2][1]=findViewById(R.id.grid09);
        grid[3][1]=findViewById(R.id.grid10);
        grid[4][1]=findViewById(R.id.grid11);
        grid[5][1]=findViewById(R.id.grid12);
        grid[6][1]=findViewById(R.id.grid13);

        grid[0][2]=findViewById(R.id.grid14);
        grid[1][2]=findViewById(R.id.grid15);
        grid[2][2]=findViewById(R.id.grid16);
        grid[3][2]=findViewById(R.id.grid17);
        grid[4][2]=findViewById(R.id.grid18);
        grid[5][2]=findViewById(R.id.grid19);
        grid[6][2]=findViewById(R.id.grid20);

        grid[0][3]=findViewById(R.id.grid21);
        grid[1][3]=findViewById(R.id.grid22);
        grid[2][3]=findViewById(R.id.grid23);
        grid[3][3]=findViewById(R.id.grid24);
        grid[4][3]=findViewById(R.id.grid25);
        grid[5][3]=findViewById(R.id.grid26);
        grid[6][3]=findViewById(R.id.grid27);

        grid[0][4]=findViewById(R.id.grid28);
        grid[1][4]=findViewById(R.id.grid29);
        grid[2][4]=findViewById(R.id.grid30);
        grid[3][4]=findViewById(R.id.grid31);
        grid[4][4]=findViewById(R.id.grid32);
        grid[5][4]=findViewById(R.id.grid33);
        grid[6][4]=findViewById(R.id.grid34);

        grid[0][5]=findViewById(R.id.grid35);
        grid[1][5]=findViewById(R.id.grid36);
        grid[2][5]=findViewById(R.id.grid37);
        grid[3][5]=findViewById(R.id.grid38);
        grid[4][5]=findViewById(R.id.grid39);
        grid[5][5]=findViewById(R.id.grid40);
        grid[6][5]=findViewById(R.id.grid41);

        for(int i=0;i<7;i++)
            for(int j=0;j<6;j++)
                gridMone[i][j]=0;
    }

    @Override
    public void onClick(View view) {
        for(int i=0; i<7; i++){
            if(view==btn[i]){
                if(gridMone[i][0]!=0){
                    Toast.makeText(this,"Invalid Move",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int j = 5; j >= 0; j--) {
                        if (gridMone[i][j] == 0) {
                            if (turns == 1) {
                                grid[i][j].setImageResource(R.drawable.reddisk);
                                gridMone[i][j] = 1;
                                turns = 2;
                                j = -1;
                            } else if (turns == 2) {
                                grid[i][j].setImageResource(R.drawable.bluedisk);
                                gridMone[i][j] = 2;
                                turns = 1;
                                j = -1;
                            }
                        }
                    }
                }
            }
        }
        checkWinner();
    }

    private void checkWinner() {
        for(int i=0; i<7; i++){
            for(int j=0; j<3; j++){
                if(gridMone[i][j]==1 && gridMone[i][j+1]==1 && gridMone[i][j+2]==1 && gridMone[i][j+3]==1) {
                    winMessage(1);
                    return;
                }
                if(gridMone[i][j]==2 && gridMone[i][j+1]==2 && gridMone[i][j+2]==2 && gridMone[i][j+3]==2){
                    winMessage(2);
                    return;
                }
            }
        }

        for(int j=0; j<6; j++){
            for(int i=0; i<4; i++){
                if(gridMone[i][j]==1 && gridMone[i+1][j]==1 && gridMone[i+2][j]==1 && gridMone[i+3][j]==1){
                    winMessage(1);
                    return;
                }
                if(gridMone[i][j]==2 && gridMone[i+1][j]==2 && gridMone[i+2][j]==2 && gridMone[i+3][j]==2){
                    winMessage(2);
                    return;
                }
            }
        }

        for(int i=0; i<4; i++){
            for(int j=0; j<3; j++) {
                    if(gridMone[i][j]==1 && gridMone[i+1][j+1]==1 && gridMone[i+2][j+2]==1 && gridMone[i+3][j+3]==1) {
                        winMessage(1);
                        return;
                    }
                    if(gridMone[i][j]==2 && gridMone[i+1][j+1]==2 && gridMone[i+2][j+2]==2 && gridMone[i+3][j+3]==2) {
                        winMessage(2);
                        return;
                    }
            }
        }
        for(int i=6; i>=4; i--){
            for(int j=0; j<3; j++) {
                if(gridMone[i][j]==1 && gridMone[i-1][j+1]==1 && gridMone[i-2][j+2]==1 && gridMone[i-3][j+3]==1) {
                    winMessage(1);
                    return;
                }
                if(gridMone[i][j]==2 && gridMone[i-1][j+1]==2 && gridMone[i-2][j+2]==2 && gridMone[i-3][j+3]==2) {
                    winMessage(2);
                    return;
                }
            }
        }
    }

    private void winMessage(int i) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if(i==1){
            alertDialog.setTitle("Red Wins!!!");
            alertDialog.setIcon(R.drawable.reddisk);
        }
        else if(i==2){
            alertDialog.setTitle("Blue Wins!!!");
            alertDialog.setIcon(R.drawable.bluedisk);
        }
        alertDialog.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.setNegativeButton("Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(fourInAline.this,MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
}