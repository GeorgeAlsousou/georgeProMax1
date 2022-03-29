package com.example.georgepromax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class navigation extends AppCompatActivity implements View.OnClickListener {

    private ImageButton arrangeGameBtn,wordGameBtn,fourInLineBtn; // כפתורים
    private PlayerModel playerModel=new PlayerModel(); // מודל שחקן

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        init();
    }

    public void init(){
        arrangeGameBtn=findViewById(R.id.arrangeGameBtn);
        wordGameBtn=findViewById(R.id.wordGameBtn);
        fourInLineBtn=findViewById(R.id.fourInLineBtn);

        Intent intent=getIntent();// קבלת מידע מהדף הקודם
        playerModel.setUserName(intent.getStringExtra("userName")); // שם משתמש
        playerModel.setPassword(intent.getStringExtra("password")); // סיסמה
        playerModel.setEmail(intent.getStringExtra("email")); // מייל
        playerModel.setPhoneNumber(intent.getStringExtra("phoneNumber")); // מספר טלפון
        playerModel.setGameTimeBest(intent.getStringExtra("gameTimeBest")); // שיא במשחק

        arrangeGameBtn.setOnClickListener(this); // לחיצה
        wordGameBtn.setOnClickListener(this::onClick); // לחיצה
        fourInLineBtn.setOnClickListener(this::onClick); // לחיצה
    }

    @Override
    public void onClick(View view) {
        if(view==arrangeGameBtn){
            Intent intent=new Intent(this,game.class); // מעבר למשחק
            intent.putExtra("userName",playerModel.getUserName()); // שליחת מידע על שחקן
            intent.putExtra("password",playerModel.getPassword()); // שליחת מידע על שחקן
            intent.putExtra("email",playerModel.getEmail()); // שליחת מידע על שחקן
            intent.putExtra("phoneNumber",playerModel.getPhoneNumber()); // שליחת מידע על שחקן
            intent.putExtra("gameTimeBest",playerModel.getGameTimeBest()); // שליחת מידע על שחקן
            startActivity(intent);
        }
        if(view==wordGameBtn){
            Intent intent=new Intent(this,WordGame.class); // מעבר למשחק
            intent.putExtra("userName",playerModel.getUserName()); // שליחת מידע על שחקן
            intent.putExtra("password",playerModel.getPassword()); // שליחת מידע על שחקן
            intent.putExtra("email",playerModel.getEmail()); // שליחת מידע על שחקן
            intent.putExtra("phoneNumber",playerModel.getPhoneNumber()); // שליחת מידע על שחקן
            intent.putExtra("gameTimeBest",playerModel.getGameTimeBest()); // שליחת מידע על שחקן
            startActivity(intent);
        }
        if(view==fourInLineBtn){
            Intent intent=new Intent(this,fourInAline.class); // מעבר למשחק
            intent.putExtra("userName",playerModel.getUserName()); // שליחת מידע על שחקן
            intent.putExtra("password",playerModel.getPassword()); // שליחת מידע על שחקן
            intent.putExtra("email",playerModel.getEmail()); // שליחת מידע על שחקן
            intent.putExtra("phoneNumber",playerModel.getPhoneNumber()); // שליחת מידע על שחקן
            intent.putExtra("gameTimeBest",playerModel.getGameTimeBest()); // שליחת מידע על שחקן
            startActivity(intent);
        }
    }
}