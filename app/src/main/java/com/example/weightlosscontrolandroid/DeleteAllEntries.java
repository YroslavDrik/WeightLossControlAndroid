package com.example.weightlosscontrolandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeleteAllEntries extends AppCompatActivity {

    private static final String USERNAME_KEY = "UserDataUserName";
    private static final String SAVEDATAUSER = "SaveDataUser";
    private Button DeleteWeightButton;
    private DatabaseReference mDatabase;
    private String UserName;

    private EditText EditTextWeight, EditTextDesiredWeight;

    private Double weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_all_weight_button);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DeleteWeightButton = findViewById(R.id.DeleteWeightButton);
        EditTextWeight = findViewById(R.id.EditTextWeight);
        EditTextDesiredWeight = findViewById(R.id.EditTextDesiredWeight);
        SharedPreferences DataUser = getSharedPreferences(SAVEDATAUSER, MODE_PRIVATE);


        UserName = DataUser.getString(USERNAME_KEY, null);
        //disable toolBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        DeleteWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weightText = EditTextWeight.getText().toString().trim();

                if (!weightText.isEmpty()) {
                    try {
                        weight = Double.parseDouble(weightText);
                    } catch (NumberFormatException ignored) {
                    }
                }

                mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(currentDate);

                        List<String> DateList = new ArrayList<>();
                        DateList.add(formattedDate);

                        List<Double> WeightList = new ArrayList<Double>();
                        WeightList.add(weight);
                        Double  DesiredWeightDouble = Double.parseDouble(EditTextDesiredWeight.getText().toString());
                        mDatabase.child("WeightChanges").setValue(WeightList);
                        mDatabase.child("DateChanges").setValue(DateList);
                        mDatabase.child("DesiredWeight").setValue(DesiredWeightDouble);
                        startActivity(new Intent(DeleteAllEntries.this , MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}