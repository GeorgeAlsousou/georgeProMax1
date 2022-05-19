package com.example.georgepromax;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class alarmSet extends AppCompatActivity implements View.OnClickListener {

    private TextView timetxt; // הזמן
    private Button selectTimeBtn,setAlarmBtn,cancelAlarmBtn; // כפתורים
    private MaterialTimePicker picker; // שעון
    private Calendar calendar; // לוח שנה
    private AlarmManager alarmManager; // מנהל ההתראות
    private PendingIntent pendingIntent;//מעבר למסך הבית

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        init(); // תוכן
    }

    public void init(){
        timetxt=findViewById(R.id.timetxt);
        selectTimeBtn=findViewById(R.id.selectTimeBtn);
        setAlarmBtn=findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn=findViewById(R.id.cancelAlarmBtn);

        calendar = Calendar.getInstance(); // ברירת מחדל ב8 וחצי בבוקר
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        createNotificationChannel();

        selectTimeBtn.setOnClickListener(this);
        setAlarmBtn.setOnClickListener(this::onClick);
        cancelAlarmBtn.setOnClickListener(this::onClick);
    }

    private void createNotificationChannel() { // יצירת ערוץ לשליחת ההתראה
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // אם גרסת אנדרויד היא 8.0 ומעלה
            CharSequence name = "georgeProMaxReminderChannel";
            String description = "Channel for alarm manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("georgeProMax",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onClick(View view) {
        if(view==selectTimeBtn){
            showTimePicker(); // בחירת זמן
        }
        if(view==setAlarmBtn){
            setAlarm(); // הגדר התראה
        }
        if(view==cancelAlarmBtn){
            cancelAlarm(); // בטל התראה
        }
    }

    private void cancelAlarm() { // מבטל את ההתראה
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager == null){
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Alarm cancelled",Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() { // מפעיל את ההתראה לזמן שנבחר
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this,"Alarm set successfully",Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder() // יצירת שעון בחירה
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"georgeProMax"); // הצג את שעון הבחירה

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() { // כאשר בוחרים שעה
            @Override
            public void onClick(View v) {
                String theTime;
                if(picker.getHour()<10){ // אם השעות מספר חד ספרתי נוסיף אפס לפני
                    if(picker.getMinute()<10) // אם הדקות מספר חד ספרתי נוסיף אפס לפני
                        theTime="0"+picker.getHour()+":"+"0"+picker.getMinute();
                    else
                        theTime="0"+picker.getHour()+":"+picker.getMinute();
                }
                else {
                    if(picker.getMinute()<10) // אם הדקות מספר חד ספרתי נוסיף אפס לפני
                        theTime=picker.getHour()+":"+"0"+ picker.getMinute();
                    else
                        theTime=picker.getHour()+":"+picker.getMinute();
                }

                selectTimeBtn.setText(theTime);
                timetxt.setText(theTime);
                // נשנה את הזמן שכתוב

                calendar = Calendar.getInstance(); // נגדיר את לוח שנה עם הזמן שנבחר
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
            }
        });
    }
}