package com.example.georgepromax;

import java.util.Random;

public class Utility {

    public int index=0,temp=0; // משתנים שימושיים לפעולות

    public int[] randomArr(int x, int y){ // מחזיר מערך חד ממדי המכיל את כל המספרים בין שני פרמטרים בסדר אקראי
        Random random=new Random(); // שמתנה אקראי
        int big, small; // מספר גדול וקטן מבין הפרמטרים
        int [] arr; // המערך
        int num; // מספר אקראי

        if(x>=y){ // מי המספר הגדול ומי הקטן
            big=x;
            small=y;
        }
        else{
            big=y;
            small=x;
        }
        arr=new int[(big-small)+1]; // גודל המערך שווה להפרש בין הפרמטרים ועוד 1
        int j; // משתנה עזר
        for(int i=0; i<arr.length; i++){
            num=random.nextInt((big-small)+1)+small; // מספר אקראי בתחום
            for(j=0; j<i; j++) // מאפס עד i
                if(arr[j]==num)j=i+1; // אם המספר נמצא במערך, נסיים את הלולאה
            if(j==i) arr[i]=num; // אם המספר לא נמצא נוסיף אותו למקום הריק
            else i--; // אם המספר הופיע נחזור על הלולאה וננסה מספר אקראי אחר
        }
        return arr; // מחזיר את המערך
    }

    public char[] diffrentLetters(String s){ // מחזיר מערך תוים שבו כל תו יופיע פעם אחת בלבד
        char[] arr=new char[s.length()]; // מערך תווים
        for(int i=0; i<s.length(); i++)
            arr[i]=s.charAt(i); // הכנסת התוים במילה למערך
        int count=1,j; // משתנה לניהול הלולאה ומספר התוים שהוכנסו לעזר
        char[] ezer=new char[arr.length]; // מערך עזר
        ezer[0]=arr[0];
        for(int i=1; i<arr.length; i++){
            for(j=0; j<count && arr[i]!=ezer[j]; j++); //בדיקה אם התו מופיע כבר במערך העזר
            if(j==count){ // אם התו לא מופיע
                ezer[count]=arr[i]; // נוסיף אותו למערך העזר
                count++; // נגדיל את המספר
            }
        }
        char[] res=new char[count]; // מערך תוצאה באורך מספר התוים המופיעים בעזר
        for(int i=0; i<count; i++)
            res[i]=ezer[i]; // הכנסת התווים לתוצאה
        return res; // פתרון
    }

    public int sharedLetters(String s1, String s2){ // מקבלת שני מחרוזות ומחזירה כמה אותיות משותפות בין המחרוזות
        char[] arr1=diffrentLetters(s1); // מערך תווים שונים ממחרוזת ראשונה
        char[]arr2=diffrentLetters(s2); // מערך תווים שונים ממחרוזת שנייה
        int count=0; // כמות אותיות משותפות
        for(int i=0; i<arr1.length; i++){
            for(int j=0; j<arr2.length; j++)
                if(arr1[i]==arr2[j]) // אם התו מופיע בשני המערכים
                    count++; // אות משותפת
        }
        return count; // פתרון
    }



    /*public boolean loginCheck(String name, String pass){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("proMax/playerModel");
        String[] dbName = new String[100];
        String[] dbPass = new String[100];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                index=0;
                boolean flag=false;
                for(DataSnapshot ds: snapshot.getChildren()){
                    dbName[index]=snapshot.child((ds.getKey())).child("userName").getValue().toString();
                    dbPass[index]=snapshot.child((ds.getKey())).child("password").getValue().toString();



                    if(dbName[index].trim().equals(name.trim()) && dbPass[index].trim().equals(pass.trim()))
                        flag=true;
                    else
                        if(flag==false)
                            index=index+1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        if(dbName[index].equals(name) && dbPass[index].equals(pass))
            return true;

        return false;
    }*/
}
