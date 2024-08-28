package com.example.weightlosscontrolandroid.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weightlosscontrolandroid.ListAdapter;
import com.example.weightlosscontrolandroid.Todo;
import com.example.weightlosscontrolandroid.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private static final String SAVEDATAUSER = "SaveDataUser";
    private static final String USERNAME_KEY = "UserDataUserName";

    private FragmentDashboardBinding binding;


    private Todo UserData;
    private DatabaseReference mDatabase;

    private String UserName;

    private Button AddWeightButton;
    private EditText AddWeightEditText;
    private  ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();



        AddWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weightText = AddWeightEditText.getText().toString().trim();
                if(!weightText.isEmpty()){
                    try {
                        Double weight = Double.parseDouble(weightText);
                        UpdateValue(weight);
                        AddWeightEditText.setText("");
                    } catch (NumberFormatException ignored) {}
                }
            }
        });




        return root;
    }

    private void init(){
        SharedPreferences DataUser = getActivity().getSharedPreferences( SAVEDATAUSER  , getActivity().MODE_PRIVATE);
        UserName = DataUser.getString(USERNAME_KEY , null);


        listView = binding.ListView;
        AddWeightButton = binding.AddWeight;
        AddWeightEditText =binding.AddWeightEditText;
        UpdateBase();
    }

    private void UpdateBase(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData = snapshot.getValue(Todo.class);
                List<String> StringList = CreatingStringListBox(UserData.WeightChanges);
                ListAdapter adapter = new ListAdapter(getActivity(), StringList  , UserData.DateChanges);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void UpdateValue(Double Weight){
        mDatabase = FirebaseDatabase.getInstance().getReference("Account").child(UserName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(currentDate);
                UserData = snapshot.getValue(Todo.class);

                List<String> DateList = new ArrayList<>(UserData.DateChanges);
                DateList.add(formattedDate);

                List<Double> WeightList = new ArrayList<Double>(UserData.WeightChanges);
                WeightList.add(Weight);
                mDatabase.child("WeightChanges").setValue(WeightList);
                mDatabase.child("DateChanges").setValue(DateList);
                List<String> StringList = CreatingStringListBox(UserData.WeightChanges);
                ListAdapter adapter = new ListAdapter(getActivity(), StringList  , UserData.DateChanges);
                listView.setAdapter(adapter);
                UpdateBase();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<String> CreatingStringListBox(List<Double> Weight)
    {
        List<String> FullString = new ArrayList<String>();
        for (int i = Weight.size(); i >0; i--)
        {
            String item = (i) + ". " + "Weight: " + Weight.get(i-1);
            FullString.add(item);
        }
        return FullString;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}