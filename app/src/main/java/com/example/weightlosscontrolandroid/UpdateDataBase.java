package com.example.weightlosscontrolandroid;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDataBase {


    private DatabaseReference mDatabase;
    private String UserName;


    private Todo UserData;


    public UpdateDataBase(String UserName){
        this.UserName = UserName;
    }
    public void UpdateBase(){


        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData = snapshot.getValue(Todo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public Todo GetUserData(){
        UpdateBase();
        return UserData;
    }
}
