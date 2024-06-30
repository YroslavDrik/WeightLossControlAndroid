package com.example.weightlosscontrolandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateAccount extends AppCompatActivity {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";
    private static final String PASSWORD_KEY = "UserDataPassword";

    private Button SignInButton;
    private DatabaseReference mDatabase;
    private TextInputEditText EditTextUserName , EditTextPassword , EditTextDesiredWeight , EditTextWeightChanges;
    List<Double> WeightChanges;
    List<String> DateChanges;

    private SharedPreferences DataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        init();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = sdf.format(new Date());
                if(!TextUtils.isEmpty(EditTextUserName.getText().toString()) &&
                   !TextUtils.isEmpty(EditTextPassword.getText().toString()) &&
                   !TextUtils.isEmpty(EditTextDesiredWeight.getText().toString())&&
                   !TextUtils.isEmpty(EditTextWeightChanges.getText().toString()))
                {
                    DateChanges.add(currentDate);
                    mDatabase = FirebaseDatabase.getInstance().getReference("Account");
                    WeightChanges.add(Double.parseDouble(EditTextWeightChanges.getText().toString()));

                    Todo todo = new Todo(
                            EditTextUserName.getText().toString(),
                            EditTextUserName.getText().toString(),
                            EditTextPassword.getText().toString(),
                            Double.parseDouble(EditTextDesiredWeight.getText().toString()),
                            WeightChanges,DateChanges);
                    String fer = EditTextUserName.getText().toString();

                    mDatabase.child(fer).setValue(todo).addOnCompleteListener(task -> {
                        SaveDataUser( EditTextUserName.getText().toString() ,EditTextPassword.getText().toString());
                        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });

            }}
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    private void init(){
        WeightChanges = new ArrayList<>();
        DateChanges = new ArrayList<>();
        SignInButton = findViewById(R.id.SignInButton);
        EditTextUserName = findViewById(R.id.EditTextUserName);
        EditTextPassword = findViewById(R.id.EditTextPassword);
        EditTextWeightChanges = findViewById(R.id.EditTextWeight);
        EditTextDesiredWeight = findViewById(R.id.EditTextDesiredWeight);
    }
    private void SaveDataUser(String NameUser, String PasswordText){
        DataUser = getSharedPreferences(SAVEDATAUSER, MODE_PRIVATE);
        SharedPreferences.Editor editor = DataUser.edit();
        editor.putString(USERNAME_KEY, NameUser);
        editor.putString(PASSWORD_KEY, PasswordText);
        editor.apply();
    }

}