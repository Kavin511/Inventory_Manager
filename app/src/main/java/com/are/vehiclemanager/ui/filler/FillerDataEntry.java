package com.are.vehiclemanager.ui.filler;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;
import java.util.Objects;

public class FillerDataEntry extends Fragment {
    static int n1 = 0, n2 = 0;
    EditText hydraulicoil, engineoil, transmissionoil, gearoil, coolantoil, filter, partnumm, estimated_cost, ending_reading, starting_reading, model_num, filter2, partnum;
    Button add_filler, add_button;
    ExtendedFloatingActionButton description_add;
    LinearLayout linearLayout;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout description_layout;
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filler_data_entry, container, false);
        hydraulicoil = v.findViewById(R.id.hydraulicoil);
        engineoil = v.findViewById(R.id.engineoil);
        transmissionoil = v.findViewById(R.id.transmissionoil);
        gearoil = v.findViewById(R.id.gearoil);
        coolantoil = v.findViewById(R.id.coolantoil);
        filter = v.findViewById(R.id.filter);
        partnumm = v.findViewById(R.id.partnum);
        add_filler = v.findViewById(R.id.add_filter);
        starting_reading = v.findViewById(R.id.starting_reading);
        estimated_cost = v.findViewById(R.id.estimated_cost);
        ending_reading = v.findViewById(R.id.ending_reading);
        model_num = v.findViewById(R.id.model_num);
        add_button = v.findViewById(R.id.add_button);
        linearLayout = v.findViewById(R.id.filterLayout);
        description_add = v.findViewById(R.id.description_add);
        Map<String, Object> filler = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        Spinner spinner = v.findViewById(R.id.spinner);

        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(DataDBViewModel.class);
        dataDBViewModel.getGetRecentData(getContext(), "vehicle").observe(requireActivity(), new Observer<List<DataDB>>() {
            @Override
            public void onChanged(List<DataDB> dataDBS) {
                List<String> categories = new ArrayList<String>();
                int n = 0;
                for (DataDB dataDB : dataDBS) {
                    String[] val = dataDB.getData().split(",");
                    categories.add(val[1]);
                    n++;
                }
                if (dataDBS.size() == 0) {
                    Toast.makeText(getContext(), "No equipments added, add equipments to select equipments", Toast.LENGTH_SHORT).show();
                }
                if (n == dataDBS.size()) {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }
        });
        dataDBViewModel = new DataDBViewModel((Application) requireContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        List<View> str = new ArrayList<>();
        description_layout = v.findViewById(R.id.description_layout);
        description_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View desc_v = inflater.inflate(R.layout.filter_description_layout, null);
                EditText filter = desc_v.findViewById(R.id.filter);
                EditText part_num = desc_v.findViewById(R.id.partnum);
                ExtendedFloatingActionButton delete = desc_v.findViewById(R.id.delete);
                str.add(desc_v);
                description_layout.addView(desc_v);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        description_layout.removeView(desc_v);
                        str.remove(desc_v);
                    }
                });
            }
        });
        final String[] spinner_item = {""};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_item[0] = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("filter_details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String hydraulic = document.getString("hydraulic");
                        String engine_oil = document.getString("engine_oil");
                        String transmission = document.getString("transmission");
                        String gearoil = document.getString("gearoil");
                        String coolant_oil = document.getString("coolant_oil");
                        String date = document.getString("date");
                        String time = document.getString("time");
                        String cost = document.getString("cost");
                        String starting = document.getString("starting");
                        String ending = document.getString("ending");
                        String model_num = document.getString("model_num");
                        String filter = document.getString("filter");
                        String time_stamp = document.getString("timestamp");
                        String equipment = document.getString("equipment");
                        String k = "Equipment :," + equipment + ",Model number :," + model_num + filter + ",Engine oil :," + engine_oil + ",Hydraulic oil :," + hydraulic + ",Transmission oil :," + transmission + ",Gear oil :," + gearoil + ",Coolant oil :," + coolant_oil + ",Starting reading :," + starting + ",Ending reading :," + ending + ",Estimated cost :," + cost + ",";
//                        assert time_stamp != null;
                        if (time_stamp != null) {
                            DataDB dataDB = new DataDB(k, Long.parseLong(time_stamp), "filter", cost, time);
                            dataDBViewModel.insert(dataDB, getContext());
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
        add_filler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hydraulic = hydraulicoil.getText().toString().trim().length() > 0 ? hydraulicoil.getText().toString().trim() : "N/A";
                hydraulicoil.setText("");
                String engine = engineoil.getText().toString().trim().length() > 0 ? engineoil.getText().toString().trim() : "N/A";
                engineoil.setText("");
                String transmission = transmissionoil.getText().toString().trim().length() > 0 ? transmissionoil.getText().toString().trim() : "N/A";
                transmissionoil.setText("");
                String gear = gearoil.getText().toString().trim().length() > 0 ? gearoil.getText().toString().trim() : "N/A";
                gearoil.setText("");
                String coolant = coolantoil.getText().toString().trim().length() > 0 ? coolantoil.getText().toString().trim() : "N/A";
                coolantoil.setText("");
                String ending = ending_reading.getText().toString().trim().length() > 0 ? ending_reading.getText().toString().trim() : "N/A";
                ending_reading.setText("");
                String cost = estimated_cost.getText().toString().trim().length() > 0 ? estimated_cost.getText().toString().trim() : "0";
                estimated_cost.setText("");
                String starting = starting_reading.getText().toString().trim().length() > 0 ? starting_reading.getText().toString().trim() : "N/A";
                starting_reading.setText("");
                String model = model_num.getText().toString().trim().length() > 0 ? model_num.getText().toString().trim() : "N/A";
                model_num.setText("");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.ENGLISH);
                String date = simpleDateFormat.format(calendar.getTime());
                String time = currentTime.toString().trim();
                int l = 1;
                String filter_ = ",Filter type 1:," + (filter.getText().toString().trim().length() > 0 ? filter.getText().toString().trim() : "N/A");
                filter.setText("");
                String partnumm_ = ",Filter part number 1:," + (partnumm.getText().toString().trim().length() > 0 ? partnumm.getText().toString().trim() : "N/A");
                partnumm.setText("");
                String filter_details = filter_ + partnumm_;
                for (View temp : str) {
                    l++;
                    EditText filter = temp.findViewById(R.id.filter);
                    EditText partnum = temp.findViewById(R.id.partnum);
                    filter_details += (",Filter type " + l + ":," + (filter.getText().toString().trim().length() > 0 ? filter.getText().toString().trim() : "N/A")) + (",Filter Part number" + l + ":," + (partnum.getText().toString().trim().length() > 0 ? partnum.getText().toString().trim() : "N/A"));
                    filter.setText("");
                    partnum.setText("");
                }
                filler.put("hydraulic", hydraulic);
                filler.put("engine_oil", engine);
                filler.put("transmission", transmission);
                filler.put("gearoil", gear);
                filler.put("coolant_oil", coolant);
                filler.put("date", date);
                filler.put("time", date);
                filler.put("cost", cost);
                filler.put("starting", starting);
                filler.put("ending", ending);
                filler.put("model_num", model);
                filler.put("filter", filter_details);
                filler.put("equipment", spinner_item[0]);
                String timeStamp = "" + Calendar.getInstance().getTimeInMillis();
                filler.put("timestamp", timeStamp);
                String k = "Equipment : ," + spinner_item[0] + ",Model number :," + model + filter_details + ",Engine oil :," + engine + ",Hydraulic oil :," + hydraulic + ",Transmission oil :," + transmission + ",Gear oil :," + gear + ",Coolant oil :," + coolant + ",Starting reading :," + starting + ",Ending reading :," + ending + ",Estimated cost :," + cost + ",";
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userid = firebaseAuth.getCurrentUser().getUid();
                DataDB dataDB = new DataDB(k, Long.parseLong(timeStamp), "filter", cost, date);
                dataDBViewModel.insert(dataDB, getContext());
                dataViewAdapter.notifyDataSetChanged();
                db.collection("users").document(userid).collection("filter_details").add(filler).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Filter data added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error occurred,Data not added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;

    }
}