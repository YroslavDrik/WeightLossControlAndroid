package com.example.weightlosscontrolandroid.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weightlosscontrolandroid.Todo;
import com.example.weightlosscontrolandroid.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";

    private DatabaseReference mDatabase;

    private SwipeRefreshLayout SwipeUpdate;

    private Todo UserData;

    private TextView TextHome;
    private FragmentHomeBinding binding;
    private String UserName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        UpdateBase();

        SwipeUpdate.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateBase();
                SwipeUpdate.setRefreshing(false);
            }
        });
        return root;

    }

    private void init(){
        SharedPreferences DataUser = getActivity().getSharedPreferences( SAVEDATAUSER  , getActivity().MODE_PRIVATE);
        UserName = DataUser.getString(USERNAME_KEY , null);
        SwipeUpdate = binding.swipeUpdate;
        TextHome = binding.textHome;
    }

    private void UpdateBase(){

        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData = snapshot.getValue(Todo.class);
                TextHome.setText(UserData.Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}