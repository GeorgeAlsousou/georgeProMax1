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

public class AdapterItems extends ArrayAdapter<ClassItem> {
    private Context context;
    private List<ClassItem> object;

    public AdapterItems(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ClassItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.object=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.itemdemo, parent, false);

        TextView txtUserItem = view.findViewById(R.id.txtUserItem);
        TextView txtPasswordItem = view.findViewById(R.id.txtPasswordItem);
        ImageView imgItem = view.findViewById(R.id.imgItem);

        ClassItem temp=object.get(position);
        txtUserItem.setText("User: "+temp.getUserName());
        txtPasswordItem.setText("Password "+temp.getPassword());
        imgItem.setImageResource(temp.getImgItem());

        return view;
    }
}
