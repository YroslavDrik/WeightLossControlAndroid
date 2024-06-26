package com.example.weightlosscontrolandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weightlosscontrolandroid.ui.home.DataBaseError;
import com.example.weightlosscontrolandroid.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class LoadDataActivity extends AppCompatActivity {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";
    private static final String PASSWORD_KEY = "UserDataPassword";


    private String UserName , Password;
    private DatabaseReference mDatabase;
    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load_data);
        init();




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void init(){
        getDataUser();
        if (isOnline()) {
            setActivity(UserName , Password);

        } else {
            Intent intent =new Intent(LoadDataActivity.this , DataBaseError.class);
            startActivity(intent);
        }

        //disable toolBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //disable toolBar
    }


    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }


    private void getDataUser(){
        SharedPreferences DataUser = getSharedPreferences( SAVEDATAUSER  , MODE_PRIVATE);
        UserName = DataUser.getString(USERNAME_KEY , null);
        Password = DataUser.getString(PASSWORD_KEY, null);

    }
    private void setActivity(String UserName , String UserPassword){
        if(Objects.isNull(UserName) && Objects.isNull(UserPassword)){
            Intent intent = new Intent(LoadDataActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Intent intent = new Intent(LoadDataActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //sad
                    Intent intent =new Intent(LoadDataActivity.this , DataBaseError.class);
                    startActivity(intent);
                }
            });
        }
    }

}