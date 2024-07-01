package com.example.weightlosscontrolandroid.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weightlosscontrolandroid.R;
import com.example.weightlosscontrolandroid.RenderChart;
import com.example.weightlosscontrolandroid.Todo;
import com.example.weightlosscontrolandroid.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";

    private DatabaseReference mDatabase;

    private SwipeRefreshLayout SwipeUpdate;

    private Todo UserData;

    private FragmentHomeBinding binding;
    private ProgressBar DayProgressBar , WeightProgressBar;
    private TextView DayText , WeightText;
    private BarChart BarChart;

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
        DayProgressBar = binding.progressBarDay;
        WeightProgressBar = binding.progressBarWeight;
        DayText = binding.TextDay;
        WeightText = binding.TextWeight;
        BarChart = binding.barChart;

    }


    private void UpdateBase(){

        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData = snapshot.getValue(Todo.class);
                SetProgressBar(UserData.DateChanges.get(0) , UserData.WeightChanges , UserData.DesiredWeight);
                RenderChart renderChart = new RenderChart(getActivity(), UserData.WeightChanges, BarChart);
                renderChart.renderBarChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SetProgressBar(String dateString , List<Double> ListWeight , Double Desired){
        int currentYear = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentYear = Year.now().getValue();
            LocalDate date = LocalDate.parse(dateString);
            LocalDate currentDate = LocalDate.now();
            int daysBetween = (int) ChronoUnit.DAYS.between(date ,currentDate );
            if(Year.isLeap(currentYear)){
                //366
                DayProgressBar.setMax(366);

                DayProgressBar.setProgress(daysBetween);
                DayText.setText(daysBetween + "/366");
            }else{
                DayProgressBar.setMax(365);
                DayProgressBar.setProgress(daysBetween);
                DayText.setText(daysBetween + "/365");
            }
            double MaxProgressBarWeight = ListWeight.get(0)-Desired;
            double ProgressBarWeight = ListWeight.get(0)- ListWeight.get(ListWeight.size()-1);
            double roundedMaxProgressBarWeight = Math.round(MaxProgressBarWeight * 10.0) / 10.0;
            double roundedProgressBarWeight = Math.round(ProgressBarWeight * 10.0) / 10.0;
            WeightProgressBar.setMax((int)MaxProgressBarWeight);
            WeightProgressBar.setProgress((int) ProgressBarWeight);
            WeightText.setText((roundedProgressBarWeight)+"/"+(roundedMaxProgressBarWeight));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}