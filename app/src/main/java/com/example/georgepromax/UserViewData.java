package com.example.georgepromax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserViewData extends AppCompatActivity implements View.OnClickListener {
    private ImageView showUserImage;
    private TextView showUserName, showUserPassword;
    private Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_data);

        init();
    }
    public void init(){
        showUserImage=findViewById(R.id.showUserImage);
        showUserName=findViewById(R.id.showUserName);
        showUserPassword=findViewById(R.id.showUserPassword);
        btnGoBack=findViewById(R.id.btnGoBack);

        Intent intent=getIntent();
        String name=intent.getStringExtra("UserName");
        String password=intent.getStringExtra("Password");
        int img=intent.getIntExtra("Image",0);

        if(name!=null)showUserName.setText(intent.getStringExtra("UserName"));
        if(password!=null)showUserPassword.setText(intent.getStringExtra("Password"));
        showUserImage.setImageResource(intent.getIntExtra("Image",0));


        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,MasterP.class);
        startActivity(intent);
    }
}