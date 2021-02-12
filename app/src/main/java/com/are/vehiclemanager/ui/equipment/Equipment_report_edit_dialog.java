package com.are.vehiclemanager.ui.equipment;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Equipment_report_edit_dialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Equipment_report_edit_dialog extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static DataDB dataDB = null;
    EditText vehiclename, model, inspectionReport, desc, partnum, quantity, cost, action, location, remark, name, spinner;
    Button Add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Equipment_report_edit_dialog(DataDB dataDB) {
        Equipment_report_edit_dialog.dataDB = dataDB;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Equipment_report_edit_dialog.
     */
    // TODO: Rename and change types and number of parameters
    public static Equipment_report_edit_dialog newInstance(String param1, String param2) {
        Equipment_report_edit_dialog fragment = new Equipment_report_edit_dialog(dataDB);
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
        View v = inflater.inflate(R.layout.fragment_equipment_report_edit_dialog, container, false);
        vehiclename = v.findViewById(R.id.vehiclename);
        name = v.findViewById(R.id.name_of);
        model = v.findViewById(R.id.model);
        inspectionReport = v.findViewById(R.id.inspectionReport);
        desc = v.findViewById(R.id.desc);
        partnum = v.findViewById(R.id.partnum);
        quantity = v.findViewById(R.id.quantity);
        cost = v.findViewById(R.id.cost);
        location = v.findViewById(R.id.location);
        remark = v.findViewById(R.id.remark);
        action = v.findViewById(R.id.action);
        Add = v.findViewById(R.id.add_eq_report);
        spinner = v.findViewById(R.id.spinner);
        Log.d("dialog", "onCreateView: " + dataDB.getData());
        String[] str = dataDB.getData().split("[,]");
//        vehiclename.setText(str[1]);
//        name.setText(str[3]);
//        inspectionReport.setText(str[1]);
//        desc.setText("");
//        partnum.setText("");
//        quantity.setText(str[15]);
//        cost.setText("");
//        location.setText(str[21]);
//        remark.setText(str[23]);
//        model.setText("");
//        action.setText("");
        dataDBViewModel = new DataDBViewModel((Application) getContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        long time_ = dataDB.getTimeStamp();
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
                String spinnerText = spinner.getText().toString().trim().length() > 0 ? spinner.getText().toString() : "N/A";
                Map<String, Object> equipment = new HashMap<>();
//                ProgressDialog progressDialog=new ProgressDialog(getActivity());
//                progressDialog.setContentView(R.layout.progressdialog);
//                progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                String date = simpleDateFormat.format(calendar.getTime());
                String time = currentTime.toString().trim();
                equipment.put("date", date);
                equipment.put("time", time);
                equipment.put("Serial number", spinnerText);
                String k = "Serial number : ," + spinnerText + ",Name of the engineer/incharge :," + incharge + ",Equipment name :," + S_vehiclename
                        + ",Model number :," + S_model
                        + ",Inspection details :," + S_inspectionReport
                        + ",Description :," + S_desc
                        + ",Part number :," + S_partnum
                        + ",Quantity :," + S_quantity
                        + ",Approximate cost :," + S_cost
                        + ",Action taken :," + S_action
                        + ",Location :," + S_location
                        + ",Remark :," + S_remark;
                DataDB data = new DataDB(k, time_, "equipment", S_cost, date);
                dataDBViewModel.insert(data, getContext());
                dataViewAdapter.notifyDataSetChanged();
                dismiss();
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                db.collection("users").document(userid).collection("Equipment_details").document().update(equipment).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Error Occurred,Data not uploaded to cloud", Toast.LENGTH_LONG).show();
//
//                    }
//                });
            }
        });
        return v;
    }
}