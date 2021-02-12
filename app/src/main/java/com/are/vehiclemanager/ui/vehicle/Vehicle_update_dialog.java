package com.are.vehiclemanager.ui.vehicle;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Vehicle_update_dialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Vehicle_update_dialog extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static DataDB dataDB = null;
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    EditText machine_name, model_num, serial_num, year, reg_num;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Vehicle_update_dialog(DataDB dataDB) {
        Vehicle_update_dialog.dataDB = dataDB;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vehicle_update_dialog.
     */
    // TODO: Rename and change types and number of parameters
    public static Vehicle_update_dialog newInstance(String param1, String param2) {
        Vehicle_update_dialog fragment = new Vehicle_update_dialog(dataDB);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View prompt = inflater.inflate(R.layout.fragment_vehicle_update_dialog, container, false);
        ExtendedFloatingActionButton vehicle_add = prompt.findViewById(R.id.equipment_add);
        dataDBViewModel = new DataDBViewModel((Application) getContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        machine_name = prompt.findViewById(R.id.machine_name);
        model_num = prompt.findViewById(R.id.model_num);
        serial_num = prompt.findViewById(R.id.serial_num);
        year = prompt.findViewById(R.id.year);
        reg_num = prompt.findViewById(R.id.reg_num);
        String[] arr = dataDB.getData().split(",");
        long timestmp = dataDB.getTimeStamp();
        machine_name.setText(arr[1]);
        model_num.setText(arr[3]);
        serial_num.setText(arr[5]);
        year.setText(arr[7]);
        reg_num.setText(arr[9]);
        vehicle_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = true;
                String s_machine_name = machine_name.getText().toString().trim().length() > 0 ? machine_name.getText().toString().trim() : "";
                String s_model_num = model_num.getText().toString().trim().length() > 0 ? model_num.getText().toString().trim() : "";
                String s_serial_num = serial_num.getText().toString().trim().length() > 0 ? serial_num.getText().toString().trim() : "";
                String s_year = year.getText().toString().trim().length() > 0 ? year.getText().toString().trim() : "";
                String s_reg_num = reg_num.getText().toString().trim().length() > 0 ? reg_num.getText().toString().trim() : "";
                int count = 0;
                if (s_machine_name.length() <= 0) {
                    machine_name.setError("Please fill out the field!");
                    count++;
                }
                if (s_model_num.length() <= 0) {
                    model_num.setError("Please fill out the field!");
                    count++;
                }
                if (s_year.length() <= 0) {
                    year.setError("Please fill out the field!");
                    count++;
                }
                if (s_serial_num.length() <= 0) {
                    serial_num.setError("Please fill out the field!");
                    count++;
                }
                if (s_reg_num.length() <= 0) {
                    reg_num.setError("Please fill out the field!");
                    count++;
                }
                if (count == 0) {
                    Map<String, Object> vehicles = new HashMap<>();
                    vehicles.put("machine_name", s_machine_name);
                    vehicles.put("model_num", s_model_num);
                    vehicles.put("serial_num", s_serial_num);
                    vehicles.put("year", s_year);
                    vehicles.put("reg_num", s_reg_num);
                    String k = "Brand name :," +
                            s_machine_name +
                            ",Model number :," +
                            s_model_num +
                            ",Serial number :," +
                            s_serial_num +
                            ",Year :," +
                            s_year +
                            ",Unique number :," +
                            s_reg_num;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DataDB data = new DataDB(k, timestmp, "vehicle", "0", "date");
                    dataDBViewModel.insert(data, getContext());
                    dataViewAdapter.notifyDataSetChanged();
//                    db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("vehicles").add(vehicles).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            dismiss();
//                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getContext(), "Vehicle updated Successfully", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                        }
//                    });
                }
            }
        });
        return prompt;
    }
}