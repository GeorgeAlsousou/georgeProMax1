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

//    private TextView txt;
    private ListView listView;
    private String[] dbName=new String[100];
    private String[] dbTime=new String[100];
    int index=0;
    private LeaderboardItem leaderboardItem;
    private ArrayList<LeaderboardItem> list;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_time_leader_board);

        init();
    }

    private void init() {
        listView=findViewById(R.id.listLeaderboard);
        list=new ArrayList<>();
//        txt=findViewById(R.id.txtLeaderboard);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("proMax/playerModel");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                index=0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    dbName[index] = snapshot.child((ds.getKey())).child("userName").getValue().toString();
                    dbTime[index] = snapshot.child((ds.getKey())).child("gameTimeBest").getValue().toString();
                    index = index + 1;
                }

                sortUp();

                for(int i=0;i<index;i++){
                    leaderboardItem = new LeaderboardItem();
                    if(i==0)
                        leaderboardItem.setImgItem(R.drawable.firsttime);
                    else if(i==1)
                        leaderboardItem.setImgItem(R.drawable.secondtime);
                    else if(i==2)
                        leaderboardItem.setImgItem(R.drawable.thirdtime);
                    else
                    leaderboardItem.setImgItem(R.drawable.hello01);
                    leaderboardItem.setName(dbName[i]);
                    leaderboardItem.setTime(dbTime[i]);
                    list.add(leaderboardItem);
                }

                makeMyList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"***Error***",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeMyList() {
        /*dbName[0]="hello";
        dbTime[0]="99";
        dbName[1]="drjj";
        dbTime[1]="12";
        LeaderboardItem test=new LeaderboardItem();
        test.setName(dbName[0]);
        test.setTime(dbTime[0]);
        test.setImgItem(R.drawable.hello01);
        list.add(test);
        test=new LeaderboardItem();
        test.setName(dbName[1]);
        test.setTime(dbTime[1]);
        test.setImgItem(R.drawable.hello01);
        list.add(test);*/
        myAdapter=new MyAdapter(this,0,0,list);
        listView.setAdapter(myAdapter);
    }


    public int minIndexArr(int[]arr){
        int imin=0;
        for(int i=1;i<arr.length;i++){
            if(arr[i] < arr[imin])
                imin=i;
        }
        return imin;
    }

    public void sortUp(){
        int[]ezerTi=new int[index];
        String[]ezerTs=new String[index];
        String[]ezerN=new String[index];
        int imin;
        for(int i=0; i<index; i++){
            ezerTs[i]=dbTime[i];
            ezerN[i]=dbName[i];
            ezerTi[i]=Integer.valueOf(ezerTs[i]);
        }
        for(int i=0;i<index;i++){
            imin=minIndexArr(ezerTi);
            dbTime[i]=ezerTs[imin];
            dbName[i]=ezerN[imin];
            ezerTi[imin]=Integer.MAX_VALUE;
        }
    }
}