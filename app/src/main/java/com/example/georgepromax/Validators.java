 package com.example.georgepromax;

import android.content.Context;
import android.util.Patterns;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

 public class Validators extends Utility{

    private Context context;
    public String str="1";

    public Validators(){};

    public Validators(Context context) {
        this.context = context;
    }

    public String checkUserName(String userName){
        if(userName.equals(""))
            return "Please Enter User Name";
        else
            if(userName.length()<4)
                return "Minimum 4 Characters";
            else
                return "";
    }

     public String checkUserNameR(String userName){
         if(userName.equals(""))
             return "Please Enter User Name";
         else
         if(userName.length()<4)
             return "Minimum 4 Characters";
         else{
             FirebaseDatabase database = FirebaseDatabase.getInstance();
             DatabaseReference myRef = database.getReference().child("proMax/playerModel");
             dbName = new String[100];
             myRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     index=0;
                     temp=0;
                     for(DataSnapshot ds: snapshot.getChildren()){
                         dbName[index]=snapshot.child((ds.getKey())).child("userName").getValue().toString();

                         //str=str+dbName[index];

                         if(!userName.trim().equals(dbName[temp].trim()))
                             temp++;

                         index++;
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {
                     return;
                 }
             });
             /*myRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     index=0;
                     temp=0;
                     for(DataSnapshot ds: snapshot.getChildren()){
                         dbName[index]=snapshot.child((ds.getKey())).child("userName").getValue().toString();

                         str=str+dbName[index];

                         if(!userName.trim().equals(dbName[temp].trim()))
                             temp++;

                         index++;
                     }
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {}
             });*/
             if(temp!=index)
                 return "User name already in use";
             else
                 return "";
         }
     }

    public String checkPassword(String pass){
        int countSmallLetter=0, countBigLetter=0, coutDigit=0, n=pass.length();
        if(pass.equals(""))
            return "Please Enter Password";
        else
        if(pass.length()<8)
            return "Minimum 8 characters";
        else{
            for(int i=0; i<n; i++){
                if(pass.charAt(i)>='a' && pass.charAt(i)<='z')countSmallLetter++;
                if(pass.charAt(i)>='A' && pass.charAt(i)<='Z')countBigLetter++;
                if(pass.charAt(i)>='0' && pass.charAt(i)<='9')coutDigit++;
            }
            /*countSmallLetter=numberAB(pass,'a','b');
            countBigLetter=numberAB(pass, 'A', 'B');
            coutDigit=numberAB(pass, '0', '9');*/
            if(countSmallLetter==0 || countBigLetter==0 || coutDigit==0)
                return "Password must consist at least: 1 small letter, 1 big letter and 1 number";
            else
                return "";
        }
    }

    public String checkEmail(String email){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Invalid Email Address";
        else
            return "";
    }

    public String checkPhone(String phone){
        if (phone.length()!=10)
            return "Invalid Phone Number";
        else{
            int count=0;
            for(int i=0; i<phone.length(); i++){
                if(phone.charAt(i)<'0' || phone.charAt(i)>'9')
                    count++;
            }
            if(count!=0)
                return "Phone must consist only numbers";
            else
                return "";
        }
    }
}
