package com.example.georgepromax;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

public class MyAdapter extends ArrayAdapter<LeaderboardItem> {
    private Context context;
    private List<LeaderboardItem> object;

    public MyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<LeaderboardItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.object=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.leaderboarddemo, parent, false);

        TextView txtUserItem = view.findViewById(R.id.txtNameL);
        TextView txtPasswordItem = view.findViewById(R.id.txtTimeL);
        ImageView imgItem = view.findViewById(R.id.leaderboardImg);

        LeaderboardItem temp=object.get(position);
        txtUserItem.setText("Name: "+temp.getName());
        txtPasswordItem.setText("Time "+temp.getTime());
        imgItem.setImageResource(temp.getImgItem());

        return view;
    }
}
