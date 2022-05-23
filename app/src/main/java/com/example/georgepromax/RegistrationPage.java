package com.example.georgepromax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity implements OnClickListener {

    private TextView txtTitleR; // כותרת הדף
    private EditText txtUserR; // שם המשתמש
    private TextView txtValidUserR; // תקינות שם המשתמש
    private EditText txtPasswordR; // סיסמה
    private TextView txtValidPasswordR; // תקינות הסיסמה
    private EditText txtEmailR; // מייל
    private TextView txtValidEmailR; // תקינות המייל
    private EditText txtPhoneR; // מספר טלפון
    private TextView txtValidPhoneR; // תקינות מספר טלפון
    private Button btnSendR; // כפתור שליחה
    private Validators valid=new Validators(this); // מחלקה שמכילה פעולות לבדיקת תקינות
    private String []dbName; // מערך שמות מהמסד


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        init(); // תוכן
    }

    public void init(){
        txtTitleR=findViewById(R.id.txtTitleR);
        txtUserR=findViewById(R.id.txtUserR);
        txtValidUserR=findViewById(R.id.txtValidUserR);
        txtPasswordR=findViewById(R.id.txtPasswordR);
        txtValidPasswordR=findViewById(R.id.txtValidPasswordR);
        txtEmailR=findViewById(R.id.txtEmailR);
        txtValidEmailR=findViewById(R.id.txtValidEmailR);
        txtPhoneR=findViewById(R.id.txtPhoneR);
        txtValidPhoneR=findViewById(R.id.txtValidPhoneR);
        btnSendR=findViewById(R.id.btnSendR);

        btnSendR.setOnClickListener(this); // לחיצה על שלח
    }

    @Override
    public void onClick(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("proMax/playerModel");
        dbName = new String[100];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index=0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    dbName[index]=snapshot.child((ds.getKey())).child("userName").getValue().toString();

                    index++;
                }
                txtValidUserR.setText(valid.checkUserNameR(txtUserR.getText().toString(),dbName)); // תקינות
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

        txtValidPasswordR.setText(valid.checkPassword(txtPasswordR.getText().toString())); // תקינות
        txtValidEmailR.setText(valid.checkEmail(txtEmailR.getText().toString())); // תקינות
        txtValidPhoneR.setText(valid.checkPhone(txtPhoneR.getText().toString())); // תקינות
        if(txtValidUserR.getText().toString()=="" && txtValidPasswordR.getText().toString()=="" && txtValidEmailR.getText().toString()=="" && txtValidPhoneR.getText().toString()==""){ /// אם הכל תקין

            SharedPreferences sharedPreferences=getSharedPreferences("userData", MODE_PRIVATE); //שמירת שם משתמש וסיסמה על המכשיר
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserName", txtUserR.getText().toString()); // שם משתמש
            editor.putString("Password", txtPasswordR.getText().toString()); // סיסמה
            editor.commit();

            Intent i= new Intent(this, MainActivity.class); // מעבר למסך הפתיחה
            i.putExtra("UserName", txtUserR.getText().toString()); // העברת שם המשתמש למסך הפתיחה
            i.putExtra("Password", txtPasswordR.getText().toString()); // העברת סיסמה מסך הפתיחה


            /*PlayerModel playerModel=new PlayerModel(txtUserR.getText().toString(),txtPasswordR.getText().toString(),txtEmailR.getText().toString(),txtPhoneR.getText().toString());
            DataBaseHelper dataBaseHelper=new DataBaseHelper(RegistrationPage.this,"player.db",null,1);
            dataBaseHelper.addOne(playerModel);*/

            // Write a message to the database
//            FirebaseDatabase database = FirebaseDatabase.getInstance(); // מסד הנתונים
//            DatabaseReference myRef = database.getReference().child("proMax/playerModel"); // מיקום שמירת המשתמש

            PlayerModel player=new PlayerModel(); // יצירת מודל שחקן
            player.setUserName(txtUserR.getText().toString()); // הכנסת מידע שחקן
            player.setPassword(txtPasswordR.getText().toString()); // הכנסת מידע שחקן
            player.setEmail(txtEmailR.getText().toString()); // הכנסת מידע שחקן
            player.setPhoneNumber(txtPhoneR.getText().toString()); // הכנסת מידע שחקן
            player.setGameTimeBest("999999"); // הכנסת מידע שחקן

            myRef.child(myRef.push().getKey()).setValue(player);/// הכנסת שחקן למסד

            startActivity(i); // מעבר למסך הפתיחה
        }
    }
}