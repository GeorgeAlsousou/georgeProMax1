
package com.example.georgepromax;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MasterP extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private  ListView listView;
    private ClassItem classItem;
    ArrayList<ClassItem> list;
    private int[] imid={R.drawable.hello02, R.drawable.hello01};
    private String[] userArr={"The User1", "The User2"};
    private String[] passwordArr={"Password1", "Password2"};
    AdapterItems adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_p);

        //init();

    }
    public void init() {
        listView = findViewById(R.id.listView);
        list=new ArrayList<>();

        for(int i=0;i<imid.length;i++){
            classItem=new ClassItem();
            classItem.setImgItem(imid[i]);
            classItem.setUserName(userArr[i]);
            classItem.setPassword(passwordArr[i]);
            list.add(classItem);
        }

        adapterItems = new AdapterItems(this,0,0,list);
        listView.setAdapter(adapterItems);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        alert(list,i);
    }

    public void alert(ArrayList<ClassItem> list, int i){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("User Data");
        alertDialog.setMessage("User: "+list.get(i).getUserName());
        alertDialog.setIcon(list.get(i).getImgItem());
        alertDialog.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                i=i+1;
                Intent intent = new Intent(MasterP.this, UserViewData.class);
                intent.putExtra("UserName", list.get(i).getUserName());
                intent.putExtra("Password", list.get(i).getPassword());
                intent.putExtra("Image", list.get(i).getImgItem());
                startActivity(intent);

                //Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.show();
    }
}
