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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetDialog_equipment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetDialog_equipment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BottomSheetDialog_equipment() {
        // Required empty public constructor
    }

    public static BottomSheetDialog_equipment newInstance(String param1, String param2) {
        BottomSheetDialog_equipment fragment = new BottomSheetDialog_equipment();
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
        View prompt = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        ExtendedFloatingActionButton vehicle_add = prompt.findViewById(R.id.equipment_add);
        dataDBViewModel = new DataDBViewModel((Application) getContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        vehicle_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText machine_name, model_num, serial_num, year, reg_num;
                machine_name = prompt.findViewById(R.id.machine_name);
                model_num = prompt.findViewById(R.id.model_num);
                serial_num = prompt.findViewById(R.id.serial_num);
                year = prompt.findViewById(R.id.year);
                reg_num = prompt.findViewById(R.id.reg_num);
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
                    String timeStamp = "" + Calendar.getInstance().getTimeInMillis();
                    vehicles.put("timestamp", timeStamp);
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
                    DataDB dataDB = new DataDB(k, Long.parseLong(timeStamp), "vehicle", "0", "date");
                    dataDBViewModel.insert(dataDB, getContext());
                    db.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("vehicle_details").add(vehicles);
                    dataViewAdapter.notifyDataSetChanged();
                    dismiss();
//                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("vehicles").add(vehicles).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(getContext(), "Vehicle Added Successfully", Toast.LENGTH_LONG).show();
//                                    dismiss();
//                                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                int v = 0;
//                                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                                    String k = "Registration number : " + document.getData().get("reg_num").toString() + "\nMachine name : " + document.getData().get("machine_name").toString() + "\nModel number : " + document.getData().get("model_num").toString() + "\nSerial number : " + document.getData().get("serial_num") + "\nYear : " + document.getData().get("year");
//                                                    TextView textView = new TextView(getContext());
//                                                    int padding = getResources().getDimensionPixelOffset(R.dimen.padding);
//                                                    textView.setPadding(padding, padding, padding, padding);
//                                                    textView.setText(k);
////                                                    textView.setOnLongClickListener(new View.OnLongClickListener() {
////                                                        @Override
////                                                        public boolean onLongClick(View v) {
////                                                            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(getActivity());
////                                                            alertDialogBuilder.setTitle("Are you sure to update? ");
////                                                            alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
////                                                                @Override
////                                                                public void onClick(DialogInterface dialog, int which) {
////                                                                    dialog.dismiss();
////                                                                    AlertDialog.Builder updateDialog=new AlertDialog.Builder(getActivity());
////                                                                    View prompt=getLayoutInflater().inflate(R.layout.fragment_vehicle_fragment,null);
////                                                                    updateDialog.setView(prompt);
////                                                                    updateDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
////                                                                        @Override
////                                                                        public void onClick(DialogInterface dialog, int which) {
////                                                                            dialog.cancel();
////                                                                        }
////                                                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                                                                        @Override
////                                                                        public void onClick(DialogInterface dialog, int which) {
////                                                                            dialog.cancel();
////                                                                        }
////                                                                    });
////                                                                    final AlertDialog update=updateDialog.create();
////                                                                    update.show();
////                                                                    EditText machine_name,model_num,serial_num,year,reg_num;
////                                                                    String s_machine_name=document.get("machine_name").toString();
////                                                                    String s_model_num=document.get("model_num").toString();
////                                                                    String s_serial_num=document.get("serial_num").toString();
////                                                                    String s_year=document.get("year").toString();
////                                                                    String s_reg_num=document.get("reg_num").toString();
////                                                                    machine_name=prompt.findViewById(R.id.machine_name);
////                                                                    machine_name.setText(s_machine_name);
////                                                                    model_num=prompt.findViewById(R.id.model_num);
////                                                                    model_num.setText(s_model_num);
////                                                                    serial_num=prompt.findViewById(R.id.serial_num);
////                                                                    serial_num.setText(s_serial_num);
////                                                                    year=prompt.findViewById(R.id.year);
////                                                                    year.setText(s_year);
////                                                                    reg_num=prompt.findViewById(R.id.reg_num);
////                                                                    reg_num.setText(s_reg_num);
////                                                                    update.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
////                                                                        @Override
////                                                                        public void onClick(View v) {
////                                                                            String s_machine_name=machine_name.getText().toString().trim().length()>0?machine_name.getText().toString().trim():"";
////                                                                            String s_model_num=model_num.getText().toString().trim().length()>0?model_num.getText().toString().trim():"";
////
////                                                                            String s_serial_num=serial_num.getText().toString().trim().length()>0?serial_num.getText().toString().trim():"";
////
////                                                                            String s_year=year.getText().toString().trim().length()>0?year.getText().toString().trim():"";
////
////                                                                            String s_reg_num=reg_num.getText().toString().trim().length()>0?reg_num.getText().toString().trim():"";
////                                                                            Map<String,Object> vehicles=new HashMap<>();
////                                                                            int  count=0;
////                                                                            if(s_machine_name.length()<=0)
////                                                                            {
////                                                                                machine_name.setError("Please fill out the field !");
////                                                                                count++;
////                                                                            }
////                                                                            if(s_model_num.length()<=0)
////                                                                            {
////                                                                                model_num.setError("Please fill out the field !");
////                                                                                count++;
////                                                                            }
////                                                                            if(s_year.length()<=0)
////                                                                            {
////                                                                                year.setError("Please fill out the field !");
////                                                                                count++;
////
////                                                                            }
////                                                                            if(s_serial_num.length()<=0)
////                                                                            {
////                                                                                serial_num.setError("Please fill out the field !");
////                                                                                count++;
////                                                                            }
////                                                                            if(s_reg_num.length()<=0)
////                                                                            {
////                                                                                reg_num.setError("Please fill out the field !");
////                                                                                count++;
////                                                                            }
////                                                                            if(count==0)
////                                                                            {
////                                                                                vehicles.put("machine_name",s_machine_name);
////                                                                                vehicles.put("model_num",s_model_num);
////                                                                                vehicles.put("serial_num",s_serial_num);
////                                                                                vehicles.put("year",s_year);
////                                                                                vehicles.put("reg_num",s_reg_num);
//////                                                                                progressDialog.show();
//////                                                                                Bundle args = getParentFragment().getArguments();
//////                                                                                if (args == null) {
//////                                                                                    args = new Bundle();
//////                                                                                }
//////
//////                                                                                if (mMyObject != null) {
//////                                                                                    args.putSerializable(Constants.Args.MY_OBJECT, mMyObject);
//////                                                                                }
////                                                                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("vehicles").document(document.getId()).update(vehicles);
////
//////                                                                                fragmentManager.beginTransaction().replace(R.id.fragment_container,new accountFragment()).commit();
//////                                                                                progressDialog.dismiss();
////                                                                                update.dismiss();
////                                                                            }
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            });
                }
            }
        });
        return prompt;
    }
}