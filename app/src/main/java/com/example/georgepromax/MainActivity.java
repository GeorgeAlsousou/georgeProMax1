package com.example.georgepromax;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicService musicService;
    private boolean checkMusic;

    private TextView txtTitle; // הכותרת של הדף
    private Button play; // כפתור שמעביר למסך הניווט
    private int index=0; // לניהול הלולאה להוצאת נתונים ממסד הנתונים

    public PlayerModel playerModel=new PlayerModel(); //להכנסת הנתונים של שחקן ממסד הנתונים
    private String thePlayer; // שם שחקן
    private String gameTimeBest; // השיא שלו

    private String[] dbName; //מערך שמות שהתקבלו ממסד הנתונים
    private String[] dbPass; // מערך סיסמאות שהתקבלו ממסד הנתונים
    private EditText txtUser; // כניסה שם משתמש
    private TextView txtValidUser; // האם השם תקין
    private EditText txtPassword; // כניסה סיסמה
    private TextView txtValidPassword; // האם הסיסמה תקינה
    private Button btnSend; // כפתור שליחת נתונים שהוכנסו (כניסה)
    private Button btnRegister; // הרשמה לאפליקציה
    private CheckBox checkUser; // קבלת השחקן האחרון שנרשם (במקום לכתוב שם משתמש וסיסמה)

    private ImageView imgLog; // תמונות מתחת לכותרת
    private ImageView imgHello00; // תמונות מתחת לכותרת
    private ImageView imgHello01; // תמונות מתחת לכותרת
    private ImageView imgHello02; // תמונות מתחת לכותרת

    private Random random; //קבלת מספר רנדומלי לסדר של התמונות ולמילים (welcome, hello...)


    private ImageButton imgBtnEye01; //כפתור להסתרת סיסמה
    private ImageButton imgBtnEye02; // כפתור להצגת סיסמה
    private Validators valid=new Validators(); // אובייקט לשימוש בפעולות המחלקה לבדיקת תקינות


    ////////////////////////////////////////
    private String[] welcome; // המילים לקבלת השחקן (hello, welcome, ahoy)
    private int[] randomArr; // מספרים אקראיים לסדר של התמונות
    private int r; //משתנה למספר האקראי
    ////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(); //תוכן
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu); // בחירת תפריט מתאים
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if(itemId==R.id.Registration){ //לחיצה על הרשמה
            Intent intent=new Intent(this,RegistrationPage.class);
            startActivity(intent);
        }
        if(itemId==R.id.musicChangeMode){ //שינוי מצב מוזיקה הפעל/הפסק
            if(checkMusic) {
                stopService(new Intent(this, MusicService.class));
                checkMusic=false;
            }
            else {
                startService(new Intent(this, MusicService.class));
                checkMusic=true;
            }
        }
        if(itemId==R.id.alarm){ // מעבר לדף ההתראה
            Intent intent=new Intent(this,alarmSet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
       startService(new Intent(this,MusicService.class));
       checkMusic=true;

        random=new Random();
        welcome=new String[] {"Hello ", "Welcome ", "Ahoy ", "Howdy "}; //מילות ברכה

        play=findViewById(R.id.play);
        txtTitle=findViewById(R.id.txtTitle);
        txtUser=findViewById(R.id.txtUser);
        txtValidUser=findViewById(R.id.txtValidUser);
        txtPassword=findViewById(R.id.txtPassword);
        txtValidPassword=findViewById(R.id.txtValidPassword);
        btnSend=findViewById(R.id.btnSend);
        btnRegister=findViewById(R.id.btnRegister);
        checkUser=findViewById(R.id.checkUser);
        imgBtnEye01=findViewById(R.id.imgBtnEye01);
        imgBtnEye02=findViewById(R.id.imgBtnEye02);
        imgHello00=findViewById(R.id.imgHello00);
        imgHello01=findViewById(R.id.imgHello01);
        imgHello02=findViewById(R.id.imgHello02);
        imgLog=findViewById(R.id.imgLog);

        btnSend.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        play.setOnClickListener(this::onClick);
        imgBtnEye01.setOnClickListener(this);
        imgBtnEye02.setOnClickListener(this);
        checkUser.setOnClickListener(this);

        Intent intent=getIntent();
        String name=intent.getStringExtra("UserName"); // קבלת שם משתמש מהרשמה
        String pass=intent.getStringExtra("Password"); // קבלת סיסמה מהרשמה
        if(name!=null)
            txtUser.setText(name);
        if(pass!=null)
            txtPassword.setText(pass);
    }


    @Override
    public void onClick(View view) {
        if(view==btnSend) { // לחיצה על שלח
            randomArr=valid.randomArr(0,2); // מערך של שלושה מספרים אקראיים (לתמונות)
            r=random.nextInt(4); // מערך של 4 מספרים אקראיים למילות הברכה
            txtValidUser.setText(valid.checkUserName(txtUser.getText().toString())); //תקינות
            txtValidPassword.setText((valid.checkPassword(txtPassword.getText().toString()))); //תקינות
            if(txtValidUser.getText().toString().equals("") && txtValidPassword.getText().toString().equals("")) { //אם הקלט תקין

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("proMax/playerModel"); // השחקנים במסד הנתונים
                dbName = new String[100];
                dbPass = new String[100];
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) { //לולאה שעוברת על כל השחקנים במסד
                            dbName[index] = snapshot.child((ds.getKey())).child("userName").getValue().toString(); // שם משתמש מהמסד
                            dbPass[index] = snapshot.child((ds.getKey())).child("password").getValue().toString(); // סיסמה מהמסד

                            if (dbName[index].trim().equals(txtUser.getText().toString().trim())
                                    && dbPass[index].trim().equals(txtPassword.getText().toString().trim())){ //אם שם המשתמש והסיסמה שנקלטו תקינים

                                playerModel.setUserName(dbName[index]); //יצירת מודל שחקן
                                playerModel.setPassword(dbPass[index]);// יצירת מודל שחקן
                                playerModel.setEmail(snapshot.child((ds.getKey())).child("email").getValue().toString()); // יצירת מודל שחקן
                                playerModel.setPhoneNumber(snapshot.child((ds.getKey())).child("phoneNumber").getValue().toString()); // יצירת מודל שחקן
                                playerModel.setGameTimeBest(snapshot.child((ds.getKey())).child("gameTimeBest").getValue().toString()); // יצירת מודל שחקן

                                thePlayer=dbName[index];
                                gameTimeBest=snapshot.child((ds.getKey())).child("gameTimeBest").getValue().toString();

                                play.setVisibility(View.VISIBLE); // הופעת הכפתור למסך הניווט
                                String s = welcome[r] + txtUser.getText().toString(); // שלום לשחקן
                                txtTitle.setText(s);
                                imgLog.setVisibility(view.GONE); // הסתרת התמונה הנוכחית

                                /////// תמונות אקראיות

                                if (randomArr[0] == 0) imgHello00.setImageResource(R.drawable.hello00);
                                else if (randomArr[0] == 1) imgHello00.setImageResource(R.drawable.hello01);
                                else imgHello00.setImageResource(R.drawable.hello02);

                                if (randomArr[1] == 0) imgHello01.setImageResource(R.drawable.hello00);
                                else if (randomArr[1] == 1) imgHello01.setImageResource(R.drawable.hello01);
                                else imgHello01.setImageResource(R.drawable.hello02);

                                if (randomArr[2] == 0) imgHello02.setImageResource(R.drawable.hello00);
                                else if (randomArr[2] == 1) imgHello02.setImageResource(R.drawable.hello01);
                                else imgHello02.setImageResource(R.drawable.hello02);

                                /////// תמונות אקראיות

                                imgHello00.setVisibility(View.VISIBLE);
                                imgHello01.setVisibility(View.VISIBLE);
                                imgHello02.setVisibility(View.VISIBLE);
                            }
                            index = index + 1; //קידום הלולאה

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"***Error***",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if(view==btnRegister){ //מעבר לדף ההרשמה
            Intent intent=new Intent(this, RegistrationPage.class);
            startActivity(intent);
        }

        if(view==imgBtnEye01){ // הסתרת סיסמה
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgBtnEye01.setVisibility(View.GONE);
            imgBtnEye02.setVisibility(View.VISIBLE);
        }
        if(view==imgBtnEye02){ // הצגת סיסמה
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL|InputType.TYPE_CLASS_TEXT);
            imgBtnEye01.setVisibility(View.VISIBLE);
            imgBtnEye02.setVisibility(View.GONE);
        }

        if(checkUser.isChecked()){ // קבלת נתוני שחקן השמורים במכשיר
            SharedPreferences sharedPreferences=getSharedPreferences("userData", MODE_PRIVATE);
            txtUser.setText(sharedPreferences.getString("UserName",""));
            txtPassword.setText(sharedPreferences.getString("Password",""));
            checkUser.setChecked(false);
        }

        if(view==play){ // מעבר למסך הניווט והעברת פרטי שחקן
            Intent intent=new Intent(this,navigation.class);
            intent.putExtra("userName",playerModel.getUserName());
            intent.putExtra("password",playerModel.getPassword());
            intent.putExtra("email",playerModel.getEmail());
            intent.putExtra("phoneNumber",playerModel.getPhoneNumber());
            intent.putExtra("gameTimeBest",playerModel.getGameTimeBest());
            startActivity(intent);
        }
    }
}
