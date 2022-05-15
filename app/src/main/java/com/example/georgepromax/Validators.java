 package com.example.georgepromax;

import android.content.Context;
import android.util.Patterns;

 public class Validators extends Utility{

    private Context context;

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

     public String checkUserNameR(String userName, String[]dbNames){
         if(userName.equals(""))
             return "Please Enter User Name";
         else
         if(userName.length()<4)
             return "Minimum 4 Characters";
         else{
             /*FirebaseDatabase database = FirebaseDatabase.getInstance();
             DatabaseReference myRef = database.getReference().child("proMax/playerModel");
             dbName = new String[100];
             myRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     index=0;
                     temp=0;
                     for(DataSnapshot ds: snapshot.getChildren()){
                         dbName[index]=snapshot.child((ds.getKey())).child("userName").getValue().toString();


                         if(!userName.equals(dbName[temp]))
                             temp++;

                         index++;
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {
                     return;
                 }
             });*/

             temp=0;
             for(index=0; index<dbNames.length;index++){
                 if(!userName.equals(dbNames[temp]))
                     temp++;
             }
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
