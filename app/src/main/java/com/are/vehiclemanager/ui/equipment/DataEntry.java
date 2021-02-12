package com.are.vehiclemanager.ui.equipment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataEntry extends Fragment {
    private static final String TAG = "DATAENTRY";
    EditText vehiclename, model, inspectionReport, desc, partnum, quantity, cost, action, location, remark, name;
    Button Add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data_entry, container, false);
        vehiclename = v.findViewById(R.id.vehiclename);
        name = v.findViewById(R.id.name_of);
        model = v.findViewById(R.id.model);
        Log.d(TAG, "onCreateView: ");
        inspectionReport = v.findViewById(R.id.inspectionReport);
        desc = v.findViewById(R.id.desc);
        partnum = v.findViewById(R.id.partnum);
        quantity = v.findViewById(R.id.quantity);
        cost = v.findViewById(R.id.cost);
        location = v.findViewById(R.id.location);
        remark = v.findViewById(R.id.remark);
        action = v.findViewById(R.id.action);
        Add = v.findViewById(R.id.add_eq_report);
        dataDBViewModel = new DataDBViewModel((Application) getContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Spinner spinner = v.findViewById(R.id.spinner);
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(DataDBViewModel.class);
        final String[] spinner_item = {""};
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
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_item[0] = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner_item[0] = adapterView.getItemAtPosition(0).toString();
            }
        });
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("Equipment_details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ");
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String incharge = document.getString("incharge");
                        String vehiclename = document.getString("vehiclename");
                        String model = document.getString("model");
                        String inspectionReport = document.getString("inspectionReport");
                        String desc = document.getString("desc");
                        String partnum = document.getString("partnum");
                        String quantity = document.getString("quantity");
                        String cost = document.getString("cost");
                        String location = document.getString("location");
                        String remark = document.getString("remark");
                        String action = document.getString("action");
                        String equipment = document.getString("equipment");
                        String k = "Equipment :," + equipment + ",Name of the engineer/incharge :," + incharge + ",Equipment name :," + vehiclename
                                + ",Model number :," + model
                                + ",Inspection details :," + inspectionReport
                                + ",Description :," + desc
                                + ",Part number :," + partnum
                                + ",Quantity :," + quantity
                                + ",Approximate cost :," + cost
                                + ",Action taken :," + action
                                + ",Location :," + location
                                + ",Remark :," + remark;
                        String timestamp = document.getString("timestamp");
                        String date = document.getString("date");
                        if (timestamp != null) {
                            DataDB dataDB = new DataDB(k, Long.parseLong(timestamp), "equipment", cost, date);
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
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S_vehiclename = vehiclename.getText().toString().trim().length() > 0 ? (vehiclename.getText().toString()).trim() : "N/A";
                vehiclename.setText("");
                String incharge = name.getText().toString().trim().length() > 0 ? name.getText().toString().trim() : "N/A";
                name.setText("");
                String S_inspectionReport = inspectionReport.getText().toString().trim().length() > 0 ? inspectionReport.getText().toString().trim() : "N/A";
                inspectionReport.setText("");
                String S_desc = desc.getText().toString().trim().length() > 0 ? desc.getText().toString().trim() : "N/A";
                desc.setText("");
                String S_partnum = partnum.getText().toString().trim().length() > 0 ? partnum.getText().toString().trim() : "N/A";
                partnum.setText("");
                String S_quantity = quantity.getText().toString().trim().length() > 0 ? quantity.getText().toString().trim() : "N/A";
                quantity.setText("");
                String S_cost = cost.getText().toString().trim().length() > 0 ? cost.getText().toString().trim() : "0";
                cost.setText("");
                String S_location = location.getText().toString().trim().length() > 0 ? location.getText().toString().trim() : "N/A";
                location.setText("");
                String S_remark = remark.getText().toString().trim().length() > 0 ? remark.getText().toString() : "N/A";
                remark.setText("");
                String S_model = model.getText().toString().trim().length() > 0 ? model.getText().toString().trim() : "N/A";
                model.setText("");
                String S_action = action.getText().toString().trim().length() > 0 ? action.getText().toString() : "N/A";
                action.setText("");
                Map<String, Object> equipment = new HashMap<>();
                Date currentTime = Calendar.getInstance().getTime();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                equipment.put("incharge", incharge);
                equipment.put("vehiclename", S_vehiclename);
                equipment.put("model", S_model);
                equipment.put("inspectionReport", S_inspectionReport);
                equipment.put("desc", S_desc);
                equipment.put("partnum", S_partnum);
                equipment.put("quantity", S_quantity);
                equipment.put("cost", S_cost);
                equipment.put("location", S_location);
                equipment.put("remark", S_remark);
                equipment.put("action", S_action);
                equipment.put("equipment", spinner_item[0]);
                String date = simpleDateFormat.format(calendar.getTime());
                String time = currentTime.toString().trim();
                equipment.put("date", date);
                String timestamp = "" + Calendar.getInstance().getTimeInMillis();
                equipment.put("timestamp", timestamp);
                spinner_item[0] = spinner_item[0].length() > 0 ? spinner_item[0] : "No equipments added";
                String k = "Equipment :," + spinner_item[0] + ",Name of the engineer/incharge :," + incharge + ",Equipment name :," + S_vehiclename
                        + ",Model number :," + S_model
                        + ",Inspection details :," + S_inspectionReport
                        + ",Description :," + S_desc
                        + ",Part number :," + S_partnum
                        + ",Quantity :," + S_quantity
                        + ",Approximate cost :," + S_cost
                        + ",Action taken :," + S_action
                        + ",Location :," + S_location
                        + ",Remark :," + S_remark;
                DataDB dataDB = new DataDB(k, Long.parseLong(timestamp), "equipment", S_cost, date);
                dataDBViewModel.insert(dataDB, getContext());
                dataViewAdapter.notifyDataSetChanged();
                String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                db.collection("users").document(userid).collection("Equipment_details").add(equipment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Equipment Data added sucessfully !", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error Occured!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return v;
    }
}