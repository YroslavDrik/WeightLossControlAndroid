package com.example.weightlosscontrolandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";
    private static final String PASSWORD_KEY = "UserDataPassword";

    private DatabaseReference mDatabase;
    private Button CreateAccount , LoginIn;
    private TextInputEditText EditTextUserName , TextBoxPassword;
    private Todo todo;
    private SharedPreferences DataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        init();

        //disable toolBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //disable toolBar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });{
            LoginIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String NameUser = EditTextUserName.getText().toString();
                    String PasswordText = TextBoxPassword.getText().toString();
                    if(!TextUtils.isEmpty(NameUser) && !TextUtils.isEmpty(PasswordText)){
                        DataSnapshot dataSnapshot;
                        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(NameUser);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Todo todo = snapshot.getValue(Todo.class);
                                if (todo.UserNameDataBase.equals(NameUser) && todo.Password.equals(PasswordText)) {
                                    SaveDataUser(todo.UserNameDataBase , todo.Password);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "no", Toast.LENGTH_SHORT).show();
                                    //sad
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                    });}
                }
            });
        }
    }
    private void init(){
        CreateAccount = findViewById(R.id.CreateAccount);
        LoginIn = findViewById(R.id.SignInButton);
        EditTextUserName = findViewById(R.id.TextBoxUserName);
        TextBoxPassword = findViewById(R.id.TextBoxPassword);
    }

    private void SaveDataUser(String NameUser, String PasswordText){
         DataUser = getSharedPreferences(SAVEDATAUSER, MODE_PRIVATE);
         SharedPreferences.Editor editor = DataUser.edit();
         editor.putString(USERNAME_KEY, NameUser);
         editor.putString(PASSWORD_KEY, PasswordText);
         editor.apply();
    }


}
