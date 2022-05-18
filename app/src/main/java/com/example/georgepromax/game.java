package com.example.georgepromax;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class game extends AppCompatActivity implements View.OnClickListener {
    private TextView GameTitle; // כותרת הדף
    private TextView CountDown; // טיימר בשניות
    private Button[] btnArr; // מערך כפתורים
    private Button btnBest,saveChanges; // שיא אישי ושמירת שיא
    private Utility utility = new Utility(); // אובייקט לשימוש בפעולות המחלקה
    private ImageButton help,undo; // כפתור איך לשחק וכפתור לביטול פעולה אחרונה שבוצעה
    private int[] arr = utility.randomArr(1, 15); // מערך מספרים אקראיים בתחום
    private Stack<Integer> stackUndo=new Stack<>(); // מחסנית לשמירה וביטול פעולות

    private String thePlayer,gameTimeBest; // שם השחקן והשיא שלו
    private PlayerModel playerModel=new PlayerModel(); // מודל השחקן


    private Button btnLeaderboard; // כפתור מעבר לטבלת השיאים

    private String[] dbName; // מערך שמות משתמשים מהמסד
    private int index=0; // משתנה עזר
    private String temporary; // משתנה עזר

    private CountDownTimer countDownTimer; // טיימר
    private int counter = 0; // ספירה
    private long maxTime = 600000; // הגבלת זמן
    private boolean isRunning = false; // האם הזמן רץ

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.twoFour) { //אם 2 על 4
            setGameGrid(0); // מייצג לוח של 2 על 4
            resetCounter(); // איפוס טיימר
            buttonColor24(); // אם המספר במקום הנכון הוא נצבע בירוק
            emptyMyStack(); // לנקות את המחסנית לחזרה אחורה
        }
        if (itemId == R.id.three) {
            setGameGrid(1); // מייצג לוח של 3 על 3
            resetCounter(); // איפוס טיימר
            buttonColor33(); // אם המספר במקום הנכון הוא נצבע בירוק
            emptyMyStack(); // לנקות את המחסנית לחזרה אחורה
        }
        if (itemId == R.id.four) {
            setGameGrid(2); // מייצג לוח של 4 על 4
            resetCounter(); // איפוס טיימר
            buttonColor44(); // אם המספר במקום הנכון הוא נצבע בירוק
            emptyMyStack(); // לנקות את המחסנית לחזרה אחורה
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetCounter() {
        if (counter != 0) // אם הספירה לא 0
            countDownTimer.cancel(); // עוצרים את הספירה
        counter = 0; // מאפסים את הספירה
        isRunning = false; // טיימר לא רץ
        CountDown.setText("" + counter); // מאפסים את הטקסט
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init(); // תוכן
    }

    private void myTimer() {
        if (!isRunning) { // אם הטיימר לא רץ
            countDownTimer = new CountDownTimer(600000, 1000) { // יוצרים טיימר חדש
                @Override
                public void onTick(long l) { // כל ספירה
                    CountDown.setText("" + counter);
                    counter++;
                }

                @Override
                public void onFinish() {
                    CountDown.setText("Game Over");
                } // הגבלת הזמן
            }.start();
            isRunning = true; // הטימר התחיל לרוץ
        }
    }

    private void init() {
        CountDown = findViewById(R.id.CountDown);
        GameTitle = findViewById(R.id.GameTitle);

        btnArr = new Button[16];
        btnArr[0] = findViewById(R.id.Button00);
        btnArr[1] = findViewById(R.id.Button01);
        btnArr[2] = findViewById(R.id.Button02);
        btnArr[3] = findViewById(R.id.Button03);
        btnArr[4] = findViewById(R.id.Button04);
        btnArr[5] = findViewById(R.id.Button05);
        btnArr[6] = findViewById(R.id.Button06);
        btnArr[7] = findViewById(R.id.Button07);
        btnArr[8] = findViewById(R.id.Button08);
        btnArr[9] = findViewById(R.id.Button09);
        btnArr[10] = findViewById(R.id.Button10);
        btnArr[11] = findViewById(R.id.Button11);
        btnArr[12] = findViewById(R.id.Button12);
        btnArr[13] = findViewById(R.id.Button13);
        btnArr[14] = findViewById(R.id.Button14);
        btnArr[15] = findViewById(R.id.Button15);

        Intent intent=getIntent();
        playerModel.setUserName(intent.getStringExtra("userName"));
        playerModel.setPassword(intent.getStringExtra("password"));
        playerModel.setEmail(intent.getStringExtra("email"));
        playerModel.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        playerModel.setGameTimeBest(intent.getStringExtra("gameTimeBest"));
        gameTimeBest=playerModel.getGameTimeBest();
        thePlayer=playerModel.getUserName();

        help = findViewById(R.id.help);
        undo=findViewById(R.id.undo);
        btnLeaderboard=findViewById(R.id.btnLeaderboard);

        btnBest=findViewById(R.id.btnBest);
        saveChanges=findViewById(R.id.saveChanges);
        btnBest.setOnClickListener(this::onClick);
        saveChanges.setOnClickListener(this::onClick);
        btnLeaderboard.setOnClickListener(this);

        arr = utility.randomArr(1, 15);
        if(checkSolvable(arr)==false) {
            while (!checkSolvable(arr)) { //כל עוד המספרים אינם פתירים, ניצור מספרים חדשים
                arr = utility.randomArr(1, 15);
            }
        }

        for (int j = 0; j < arr.length; j++) {
            btnArr[j].setText("" + arr[j]);//הכנסת מספרים לכפתורים
        }


        for (int i = 0; i < btnArr.length; i++) {
            btnArr[i].setOnClickListener(this);
        }
        GameTitle.setOnClickListener(this);
        help.setOnClickListener(this);
        undo.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view == help) { //איך לשחק
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("How to play");
            alertDialog.setIcon(R.drawable.help);
            alertDialog.setMessage("You need to sort the numbers by order, you can move a number if there is an empty slot next to it.\n" + "" +
                    "Numbers in the correct position will be green.\n" + "" +
                    "You can choose a different grid in the menu.");
            alertDialog.setNeutralButton("ok", null);
            alertDialog.show();
        } else{

            if(view==btnBest){/// השיא של השחקן
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Personal Best");
                alertDialog.setIcon(R.drawable.hello00);
                alertDialog.setMessage(playerModel.getGameTimeBest()+" Seconds");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.show();
            }
            if(view==saveChanges){ //לשמור שיא חדש במסד
                save();
            }
            if(view==btnLeaderboard){ //מעבר ללוח השחקנים
                Intent intent=new Intent(this,gameTimeLeaderBoard.class);
                startActivity(intent);
            }

            play(view); //מהלך
            myTimer();


            boolean win = false;

            if (btnArr[7].getVisibility() == View.GONE) { //// 3*3
                if (btnArr[0].getText().toString().equals("" + 1) &&
                        btnArr[1].getText().toString().equals("" + 2) &&
                        btnArr[2].getText().toString().equals("" + 3) &&
                        btnArr[4].getText().toString().equals("" + 4) &&
                        btnArr[5].getText().toString().equals("" + 5) &&
                        btnArr[6].getText().toString().equals("" + 6) &&
                        btnArr[8].getText().toString().equals("" + 7) &&
                        btnArr[9].getText().toString().equals("" + 8) &&
                        btnArr[10].getText().toString().equals("")) win = true;

                buttonColor33();
            } else if (btnArr[8].getVisibility() == View.GONE) {///// 2*4
                for(int i=0;i<7;i++){
                    if(btnArr[i].getText().toString().equals(""+(i+1)))
                        win=true;
                    else{
                        win=false;
                        i=7;
                    }
                }
                buttonColor24();
            } else {
                for(int i=0;i<15;i++){
                    if(btnArr[i].getText().toString().equals(""+(i+1)))
                        win=true;
                    else{
                        win=false;
                        i=15;
                    }
                }
                buttonColor44();
            }

            if(view==undo){ //חזור אחורה
                if(!stackUndo.isEmpty()){
                    int a=stackUndo.pop();
                    int b=stackUndo.pop();
                    String s=btnArr[a].getText().toString();
                    btnArr[a].setText(btnArr[b].getText().toString());
                    btnArr[b].setText(s);
                }
            }

            if (win == true) {
                countDownTimer.cancel();
                isRunning = false;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("You Win!!!");
                alertDialog.setIcon(R.drawable.hello00);
                temporary=CountDown.getText().toString();
                alertDialog.setMessage(CountDown.getText().toString() + " Seconds");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Integer.valueOf(temporary)<Integer.valueOf(gameTimeBest)){ //אם נקבע שיא חדש
                            playerModel.setGameTimeBest(temporary);
                            gameTimeBest=temporary;
                        }
                        if (btnArr[7].getVisibility() == View.GONE) { //// 3*3
                            //////// משחק חדש
                            setGameGrid(1);
                            resetCounter();
                            buttonColor33();
                            emptyMyStack();
                        }
                        else if (btnArr[8].getVisibility() == View.GONE){//// 2*4
                            /////// משחק חדש
                            setGameGrid(0);
                            resetCounter();
                            buttonColor24();
                            emptyMyStack();
                        }
                        else { /// 4*4
                            //////// משחק חדש
                            setGameGrid(2);
                            resetCounter();
                            buttonColor44();
                            emptyMyStack();
                        }
                    }
                });
                alertDialog.show();
            }
        }
        if(view==GameTitle){ ////cheat FOR TESTING
            for(int p=0;p<btnArr.length-1;p++)btnArr[p].setText(""+(p+1));
            btnArr[15].setText("15");
            btnArr[14].setText("");
        }
    }

    private void save() { //// שמירה במסד
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("proMax/playerModel");
        dbName = new String[100];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    dbName[index] = snapshot.child((ds.getKey())).child("userName").getValue().toString();
                    if(dbName[index].equals(thePlayer)){
                        myRef.child(ds.getKey()).setValue(playerModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"***Error***",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void emptyMyStack(){ // מרוקן את המחסנית
        while (!stackUndo.isEmpty())
            stackUndo.pop();
    }

    public boolean checkSolvable(int[] arr){ /// אם כמות ההחלפות שיש לבצע הוא מספר זוגי אז המספרים פתירים
        boolean check=false;
        int count=0;
        for(int i=0;i<arr.length;i++){
            for(int j=i+1; j<arr.length; j++) {
                if (arr[i] > arr[j]) {
                    count++;
                }
            }
        }
        check=count%2==0;
        return check;
    }


    private void buttonColor44() { // אם מספר במקום הנכון יש לצבוע בירוק
        for(int i=0; i<btnArr.length; i++){
            if(btnArr[i].getText().toString().equals(""+(i+1)))
                btnArr[i].setTextColor(Color.GREEN);
            else
                btnArr[i].setTextColor(Color.WHITE);
        }
    }

    private void buttonColor24() { // אם מספר במקום הנכון יש לצבוע בירוק
        for(int i=0; i<7; i++){
            if(btnArr[i].getText().toString().equals(""+(i+1)))
                btnArr[i].setTextColor(Color.GREEN);
            else
                btnArr[i].setTextColor(Color.WHITE);
        }
    }

    private void buttonColor33() { // אם מספר במקום הנכון יש לצבוע בירוק
        if(btnArr[0].getText().toString().equals(""+1))btnArr[0].setTextColor(Color.GREEN);
        else btnArr[0].setTextColor(Color.WHITE);
        if(btnArr[1].getText().toString().equals(""+2))btnArr[1].setTextColor(Color.GREEN);
        else btnArr[1].setTextColor(Color.WHITE);
        if(btnArr[2].getText().toString().equals(""+3))btnArr[2].setTextColor(Color.GREEN);
        else btnArr[2].setTextColor(Color.WHITE);
        if(btnArr[4].getText().toString().equals(""+4))btnArr[4].setTextColor(Color.GREEN);
        else btnArr[4].setTextColor(Color.WHITE);
        if(btnArr[5].getText().toString().equals(""+5))btnArr[5].setTextColor(Color.GREEN);
        else btnArr[5].setTextColor(Color.WHITE);
        if(btnArr[6].getText().toString().equals(""+6))btnArr[6].setTextColor(Color.GREEN);
        else btnArr[6].setTextColor(Color.WHITE);
        if(btnArr[8].getText().toString().equals(""+7))btnArr[8].setTextColor(Color.GREEN);
        else btnArr[8].setTextColor(Color.WHITE);
        if(btnArr[9].getText().toString().equals(""+8))btnArr[9].setTextColor(Color.GREEN);
        else btnArr[9].setTextColor(Color.WHITE);
    }

    private void setGameGrid(int i) { // מצב משחק
        if(i==0){ /// 2*4
            for(int j=0; j<btnArr.length; j++){
                if(j<=7)
                    btnArr[j].setVisibility(View.VISIBLE);
                else {
                    btnArr[j].setVisibility(View.GONE);
                    btnArr[j].setText(""+999);
                }
            }
            arr=utility.randomArr(1,7);
            if(checkSolvable(arr)==false) {
                while (!checkSolvable(arr)) {
                    arr = utility.randomArr(1, 7);
                }
            }
            for(int j=0; j<7; j++){
                btnArr[j].setText(""+arr[j]);
            }
            btnArr[7].setText("");
        }

        if(i==1){ // 3*3
            for(int j=0; j<btnArr.length; j++){
                if(j==3 || j==7 || j==11 || j>=12) {
                    btnArr[j].setVisibility(View.GONE);
                    btnArr[j].setText(""+999);
                }
                else
                    btnArr[j].setVisibility(View.VISIBLE);
            }
            arr=utility.randomArr(1,8);
            if(checkSolvable(arr)==false) {
                while (!checkSolvable(arr)) {
                    arr = utility.randomArr(1, 8);
                }
            }
            for(int j=0; j<8; j++){
                if(btnArr[j].getVisibility()==View.VISIBLE)
                    if(j<=2)
                        btnArr[j].setText(""+arr[j]);
                if(j>=3 && j<=5)
                    btnArr[j+1].setText(""+arr[j]);
                if(j>=6)
                    btnArr[j+2].setText(""+arr[j]);
            }
            btnArr[10].setText("");
        }

        if(i==2){ /// 4*4
            for(int j=0; j<btnArr.length; j++){
                btnArr[j].setVisibility(View.VISIBLE);
            }
            arr=utility.randomArr(1,15);
            if(checkSolvable(arr)==false) {
                while (!checkSolvable(arr)) {
                    arr = utility.randomArr(1, 15);
                }
            }
            for(int j=0; j<15; j++){
                btnArr[j].setText(""+arr[j]);
            }
            btnArr[15].setText("");
        }
    }

    private void play(View view) { /// מהלך
        if(view==btnArr[0]){
            if(btnArr[1].getText().toString().equals("")){
                btnArr[1].setText(btnArr[0].getText().toString());
                btnArr[0].setText("");
                stackUndo.push(0);
                stackUndo.push(1);
            }
            if(btnArr[4].getText().toString().equals("")){
                btnArr[4].setText(btnArr[0].getText().toString());
                btnArr[0].setText("");
                stackUndo.push(0);
                stackUndo.push(4);
            }
        }
        if(view==btnArr[1]){
            if(btnArr[0].getText().toString().equals("")){
                btnArr[0].setText(btnArr[1].getText().toString());
                btnArr[1].setText("");
                stackUndo.push(1);
                stackUndo.push(0);
            }
            if(btnArr[2].getText().toString().equals("")){
                btnArr[2].setText(btnArr[1].getText().toString());
                btnArr[1].setText("");
                stackUndo.push(1);
                stackUndo.push(2);
            }
            if(btnArr[5].getText().toString().equals("")){
                btnArr[5].setText(btnArr[1].getText().toString());
                btnArr[1].setText("");
                stackUndo.push(1);
                stackUndo.push(5);
            }
        }
        if(view==btnArr[2]){
            if(btnArr[1].getText().toString().equals("")){
                btnArr[1].setText(btnArr[2].getText().toString());
                btnArr[2].setText("");
                stackUndo.push(2);
                stackUndo.push(1);
            }
            if(btnArr[3].getText().toString().equals("")){
                btnArr[3].setText(btnArr[2].getText().toString());
                btnArr[2].setText("");
                stackUndo.push(2);
                stackUndo.push(3);
            }
            if(btnArr[6].getText().toString().equals("")){
                btnArr[6].setText(btnArr[2].getText().toString());
                btnArr[2].setText("");
                stackUndo.push(2);
                stackUndo.push(6);
            }
        }
        if(view==btnArr[3]){
            if(btnArr[2].getText().toString().equals("")){
                btnArr[2].setText(btnArr[3].getText().toString());
                btnArr[3].setText("");
                stackUndo.push(3);
                stackUndo.push(2);
            }
            if(btnArr[7].getText().toString().equals("")){
                btnArr[7].setText(btnArr[3].getText().toString());
                btnArr[3].setText("");
                stackUndo.push(3);
                stackUndo.push(7);
            }
        }
        if(view==btnArr[4]){
            if(btnArr[0].getText().toString().equals("")){
                btnArr[0].setText(btnArr[4].getText().toString());
                btnArr[4].setText("");
                stackUndo.push(4);
                stackUndo.push(0);
            }
            if(btnArr[5].getText().toString().equals("")){
                btnArr[5].setText(btnArr[4].getText().toString());
                btnArr[4].setText("");
                stackUndo.push(4);
                stackUndo.push(5);
            }
            if(btnArr[8].getText().toString().equals("")){
                btnArr[8].setText(btnArr[4].getText().toString());
                btnArr[4].setText("");
                stackUndo.push(4);
                stackUndo.push(8);
            }
        }
        if(view==btnArr[5]){
            if(btnArr[1].getText().toString().equals("")){
                btnArr[1].setText(btnArr[5].getText().toString());
                btnArr[5].setText("");
                stackUndo.push(5);
                stackUndo.push(1);
            }
            if(btnArr[4].getText().toString().equals("")){
                btnArr[4].setText(btnArr[5].getText().toString());
                btnArr[5].setText("");
                stackUndo.push(5);
                stackUndo.push(4);
            }
            if(btnArr[6].getText().toString().equals("")){
                btnArr[6].setText(btnArr[5].getText().toString());
                btnArr[5].setText("");
                stackUndo.push(5);
                stackUndo.push(6);
            }
            if(btnArr[9].getText().toString().equals("")){
                btnArr[9].setText(btnArr[5].getText().toString());
                btnArr[5].setText("");
                stackUndo.push(5);
                stackUndo.push(9);
            }
        }
        if(view==btnArr[6]){
            if(btnArr[2].getText().toString().equals("")){
                btnArr[2].setText(btnArr[6].getText().toString());
                btnArr[6].setText("");
                stackUndo.push(6);
                stackUndo.push(2);
            }
            if(btnArr[5].getText().toString().equals("")){
                btnArr[5].setText(btnArr[6].getText().toString());
                btnArr[6].setText("");
                stackUndo.push(6);
                stackUndo.push(5);
            }
            if(btnArr[7].getText().toString().equals("")){
                btnArr[7].setText(btnArr[6].getText().toString());
                btnArr[6].setText("");
                stackUndo.push(6);
                stackUndo.push(7);
            }
            if(btnArr[10].getText().toString().equals("")){
                btnArr[10].setText(btnArr[6].getText().toString());
                btnArr[6].setText("");
                stackUndo.push(6);
                stackUndo.push(10);
            }
        }
        if(view==btnArr[7]){
            if(btnArr[3].getText().toString().equals("")){
                btnArr[3].setText(btnArr[7].getText().toString());
                btnArr[7].setText("");
                stackUndo.push(7);
                stackUndo.push(3);
            }
            if(btnArr[6].getText().toString().equals("")){
                btnArr[6].setText(btnArr[7].getText().toString());
                btnArr[7].setText("");
                stackUndo.push(7);
                stackUndo.push(6);
            }
            if(btnArr[11].getText().toString().equals("")){
                btnArr[11].setText(btnArr[7].getText().toString());
                btnArr[7].setText("");
                stackUndo.push(7);
                stackUndo.push(11);
            }
        }
        if(view==btnArr[8]){
            if(btnArr[4].getText().toString().equals("")){
                btnArr[4].setText(btnArr[8].getText().toString());
                btnArr[8].setText("");
                stackUndo.push(8);
                stackUndo.push(4);
            }
            if(btnArr[9].getText().toString().equals("")){
                btnArr[9].setText(btnArr[8].getText().toString());
                btnArr[8].setText("");
                stackUndo.push(8);
                stackUndo.push(9);
            }
            if(btnArr[12].getText().toString().equals("")){
                btnArr[12].setText(btnArr[8].getText().toString());
                btnArr[8].setText("");
                stackUndo.push(8);
                stackUndo.push(12);
            }
        }
        if(view==btnArr[9]){
            if(btnArr[5].getText().toString().equals("")){
                btnArr[5].setText(btnArr[9].getText().toString());
                btnArr[9].setText("");
                stackUndo.push(9);
                stackUndo.push(5);
            }
            if(btnArr[8].getText().toString().equals("")){
                btnArr[8].setText(btnArr[9].getText().toString());
                btnArr[9].setText("");
                stackUndo.push(9);
                stackUndo.push(8);
            }
            if(btnArr[10].getText().toString().equals("")){
                btnArr[10].setText(btnArr[9].getText().toString());
                btnArr[9].setText("");
                stackUndo.push(9);
                stackUndo.push(10);
            }
            if(btnArr[13].getText().toString().equals("")){
                btnArr[13].setText(btnArr[9].getText().toString());
                btnArr[9].setText("");
                stackUndo.push(9);
                stackUndo.push(13);
            }
        }
        if(view==btnArr[10]){
            if(btnArr[6].getText().toString().equals("")){
                btnArr[6].setText(btnArr[10].getText().toString());
                btnArr[10].setText("");
                stackUndo.push(10);
                stackUndo.push(6);
            }
            if(btnArr[9].getText().toString().equals("")){
                btnArr[9].setText(btnArr[10].getText().toString());
                btnArr[10].setText("");
                stackUndo.push(10);
                stackUndo.push(9);
            }
            if(btnArr[11].getText().toString().equals("")){
                btnArr[11].setText(btnArr[10].getText().toString());
                btnArr[10].setText("");
                stackUndo.push(10);
                stackUndo.push(11);
            }
            if(btnArr[14].getText().toString().equals("")){
                btnArr[14].setText(btnArr[10].getText().toString());
                btnArr[10].setText("");
                stackUndo.push(10);
                stackUndo.push(14);
            }
        }
        if(view==btnArr[11]){
            if(btnArr[7].getText().toString().equals("")){
                btnArr[7].setText(btnArr[11].getText().toString());
                btnArr[11].setText("");
                stackUndo.push(11);
                stackUndo.push(7);
            }
            if(btnArr[10].getText().toString().equals("")){
                btnArr[10].setText(btnArr[11].getText().toString());
                btnArr[11].setText("");
                stackUndo.push(11);
                stackUndo.push(10);
            }
            if(btnArr[15].getText().toString().equals("")){
                btnArr[15].setText(btnArr[11].getText().toString());
                btnArr[11].setText("");
                stackUndo.push(11);
                stackUndo.push(15);
            }
        }
        if(view==btnArr[12]) {
            if (btnArr[8].getText().toString() == "") {
                btnArr[8].setText(btnArr[12].getText().toString());
                btnArr[12].setText("");
                stackUndo.push(12);
                stackUndo.push(8);
            }
            if (btnArr[13].getText().toString() == "") {
                btnArr[13].setText(btnArr[12].getText().toString());
                btnArr[12].setText("");
                stackUndo.push(12);
                stackUndo.push(13);
            }
        }
        if(view==btnArr[13]){
            if(btnArr[9].getText().toString().equals("")){
                btnArr[9].setText(btnArr[13].getText().toString());
                btnArr[13].setText("");
                stackUndo.push(13);
                stackUndo.push(9);
            }
            if(btnArr[12].getText().toString().equals("")){
                btnArr[12].setText(btnArr[13].getText().toString());
                btnArr[13].setText("");
                stackUndo.push(13);
                stackUndo.push(12);
            }
            if(btnArr[14].getText().toString().equals("")){
                btnArr[14].setText(btnArr[13].getText().toString());
                btnArr[13].setText("");
                stackUndo.push(13);
                stackUndo.push(14);
            }
        }
        if(view==btnArr[14]){
            if(btnArr[10].getText().toString().equals("")){
                btnArr[10].setText(btnArr[14].getText().toString());
                btnArr[14].setText("");
                stackUndo.push(14);
                stackUndo.push(10);
            }
            if(btnArr[13].getText().toString().equals("")){
                btnArr[13].setText(btnArr[14].getText().toString());
                btnArr[14].setText("");
                stackUndo.push(14);
                stackUndo.push(13);
            }
            if(btnArr[15].getText().toString().equals("")){
                btnArr[15].setText(btnArr[14].getText().toString());
                btnArr[14].setText("");
                stackUndo.push(14);
                stackUndo.push(15);
            }
        }
        if(view==btnArr[15]){
            if(btnArr[11].getText().toString().equals("")){
                btnArr[11].setText(btnArr[15].getText().toString());
                btnArr[15].setText("");
                stackUndo.push(15);
                stackUndo.push(11);
            }
            if(btnArr[14].getText().toString().equals("")){
                btnArr[14].setText(btnArr[15].getText().toString());
                btnArr[15].setText("");
                stackUndo.push(15);
                stackUndo.push(14);
            }
        }
    }
}