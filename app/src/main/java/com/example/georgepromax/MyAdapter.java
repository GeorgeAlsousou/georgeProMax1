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
    private Context context; // באיזה מקום נשתמש בזה
    private List<LeaderboardItem> object; // האובייקטים מהסוג הנ"ל

    public MyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<LeaderboardItem> objects) { // בנאי
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.object=objects;
    }

    public View getView(int position, View convertView, ViewGroup parent){ // הפיכת קובץ העיצוב לView שאפשר להשתמש בו
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.leaderboarddemo, parent, false); //turns the layout into a view

        TextView txtNameL = view.findViewById(R.id.txtNameL); // השם
        TextView txtTimeL = view.findViewById(R.id.txtTimeL); // הזמן
        ImageView leaderboardImg = view.findViewById(R.id.leaderboardImg); // התמונה

        LeaderboardItem temp=object.get(position); // לקיחת נתונים מרשימת האובייקטים
        txtNameL.setText("Name: "+temp.getName());
        txtTimeL.setText("Time "+temp.getTime());
        leaderboardImg.setImageResource(temp.getImgItem());

        return view; // החזרת הנתונים בנראות המבוקשת
    }
}
