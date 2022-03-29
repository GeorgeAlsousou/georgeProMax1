package com.example.georgepromax;

import androidx.appcompat.app.AppCompatActivity;

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

public class alarmSet extends AppCompatActivity implements View.OnClickListener {

    private TextView timetxt;
    private Button selectTimeBtn,setAlarmBtn,cancelAlarmBtn;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        init();
    }

    public void init(){
        timetxt=findViewById(R.id.timetxt);
        selectTimeBtn=findViewById(R.id.selectTimeBtn);
        setAlarmBtn=findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn=findViewById(R.id.cancelAlarmBtn);

        createNotificationChannel();

        selectTimeBtn.setOnClickListener(this);
        setAlarmBtn.setOnClickListener(this::onClick);
        cancelAlarmBtn.setOnClickListener(this::onClick);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
            showTimePicker();
        }
        if(view==setAlarmBtn){
            setAlarm();
        }
        if(view==cancelAlarmBtn){
            cancelAlarm();
        }
    }

    private void cancelAlarm() {
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager == null){
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Alarm cancelled",Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this,"Alarm set successfully",Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"georgeProMax");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theTime;
                if(picker.getHour()>12){
                    if(picker.getMinute()<10)
                        theTime=String.format("%02d",(picker.getHour()-12))+" : "+"0"+String.format("%02d",picker.getMinute())+" PM";
                    else
                        theTime=String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM";
                }
                else {
                    if(picker.getMinute()<10)
                        theTime=picker.getHour()+" : "+"0"+ picker.getMinute() + " AM";
                    else
                        theTime=picker.getHour()+" : " + picker.getMinute() + " AM";
                }

                selectTimeBtn.setText(theTime);
                timetxt.setText(theTime);

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
            }
        });
    }
}