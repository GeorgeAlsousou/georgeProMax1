package com.example.georgepromax;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class gameTimeLeaderBoard extends AppCompatActivity {

    private ListView listView; // הרשימה
    private String[] dbName=new String[100]; // מערך שמות שחקנים
    private String[] dbTime=new String[100]; // מערך זמני השקחנים
    int index=0; // עזר
    private LeaderboardItem leaderboardItem; // מאפייני שחקן ברשימה
    private ArrayList<LeaderboardItem> list; // רשימה המכילה את הנתונים של השחקנים
    private MyAdapter myAdapter; // התאמה בין הנתונים לרשימה על המסך

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_time_leader_board);

        init();
    }

    private void init() {
        listView=findViewById(R.id.listLeaderboard);
        list=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("proMax/playerModel"); // גישה לנתונים במסד

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                index=0;
                for (DataSnapshot ds : snapshot.getChildren()) { // מעבר על הרשימה
                    dbName[index] = snapshot.child((ds.getKey())).child("userName").getValue().toString(); // קבלת שם משתמש
                    dbTime[index] = snapshot.child((ds.getKey())).child("gameTimeBest").getValue().toString(); // קבלת זמן משחק
                    index = index + 1;
                }

                sortUp(); // סידור המערכים מהקטן לגדול

                for(int i=0;i<index;i++){ // הכנסת נתונים לרשימה
                    leaderboardItem = new LeaderboardItem();
                    if(i==0) // מקום ראשון זהב
                        leaderboardItem.setImgItem(R.drawable.firsttime);
                    else if(i==1) // מקום שני כסף
                        leaderboardItem.setImgItem(R.drawable.secondtime);
                    else if(i==2) // מקום שלישי ארד
                        leaderboardItem.setImgItem(R.drawable.thirdtime);
                    else
                    leaderboardItem.setImgItem(R.drawable.hello01); // לב
                    leaderboardItem.setName(dbName[i]);
                    leaderboardItem.setTime(dbTime[i]);
                    list.add(leaderboardItem);
                }

                makeMyList(); // הצגת הרשימה
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"***Error***",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeMyList() { // הצגת הרשימה
        myAdapter=new MyAdapter(this,0,0,list); // התאמה בין נתונים למסך
        listView.setAdapter(myAdapter); // הצגת הנתונים במסך
    }


    public int minIndexArr(int[]arr){ // מחזיר מיקום של מספר הכי קטן במערך
        int imin=0;
        for(int i=1;i<arr.length;i++){
            if(arr[i] < arr[imin])
                imin=i;
        }
        return imin;
    }

    public void sortUp(){ // מיון המערכים
        int[]ezerTi=new int[index]; // זמן במספרים
        String[]ezerTs=new String[index]; // הזמן במחרוזות
        String[]ezerN=new String[index]; // השמות
        int imin;
        for(int i=0; i<index; i++){
            ezerTs[i]=dbTime[i];
            ezerN[i]=dbName[i];
            ezerTi[i]=Integer.valueOf(ezerTs[i]);
        }
        for(int i=0;i<index;i++){
            imin=minIndexArr(ezerTi); // מיקום של מינימום
            dbTime[i]=ezerTs[imin];
            dbName[i]=ezerN[imin];
            ezerTi[imin]=Integer.MAX_VALUE; // המינימום הופך למספר גדול כדי שנוכל לקבל את המינימום הבא
        }
    }
}