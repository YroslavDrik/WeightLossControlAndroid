package com.example.weightlosscontrolandroid.ui.notifications;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weightlosscontrolandroid.ListAdapter;
import com.example.weightlosscontrolandroid.LoginActivity;
import com.example.weightlosscontrolandroid.Todo;
import com.example.weightlosscontrolandroid.UpdateDataBase;
import com.example.weightlosscontrolandroid.databinding.FragmentNotificationsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";
    private static final String PASSWORD_KEY = "UserDataPassword";

    private Button ExitButton;
    private Button ChangeDesiredWeigh;
    private SwipeRefreshLayout SwipeLayout;
    private TextView WeightNow , WeightDesired;
    private EditText AddDesiredWeightEditText;

    private FragmentNotificationsBinding binding;
    private DatabaseReference mDatabase;
    private String UserName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        SwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateBase();
                SwipeLayout.setRefreshing(false);
            }
        });

        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences(SAVEDATAUSER, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(USERNAME_KEY);
                editor.remove(PASSWORD_KEY);
                editor.apply();
                startActivity(new Intent(getActivity() , LoginActivity.class));


            }
        });

        ChangeDesiredWeigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double  DesiredWeight= Double.parseDouble(AddDesiredWeightEditText.getText().toString());
                WeightDesired.setText("Desired Weight: " + DesiredWeight.toString());
                mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mDatabase.child("DesiredWeight").setValue(DesiredWeight);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return root;
    }

    private void init(){
        ExitButton = binding.ExitButton;
        SwipeLayout = binding.SwipeLayout;
        WeightDesired = binding.desiredWeight;
        WeightNow = binding.WeightNow;
        ChangeDesiredWeigh = binding.ChangeDesiredWeigh;
        AddDesiredWeightEditText = binding.AddDesiredWeightEditText;
        getDataUser();
        UpdateBase();
    }
    private void SetText(Double desired , Double now){
        WeightDesired.setText("Desired Weight: " +desired.toString());
        WeightNow.setText("Weight Now: "+now.toString());
    }


    private void UpdateBase(){

        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Todo UserData = snapshot.getValue(Todo.class);
                int size = UserData.WeightChanges.size()-1;
                SetText(UserData.DesiredWeight ,UserData.WeightChanges.get(size) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getDataUser(){
        SharedPreferences DataUser = getActivity().getSharedPreferences( SAVEDATAUSER  , MODE_PRIVATE);
        UserName = DataUser.getString(USERNAME_KEY , null);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}