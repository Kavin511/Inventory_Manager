package com.are.vehiclemanager.ui.stock;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Stock_data_entry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Stock_data_entry extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    EditText serial_number, partnum, part_name, initial_stock, stock_in, stock_out, final_stock, invoice_num, supplier_name, supplier_company, remarks, approximate_price;
    ExtendedFloatingActionButton stock_add, description_add;
    LinearLayout description_layout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Stock_data_entry() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Stock_data_entry.
     */
    // TODO: Rename and change types and number of parameters
    public static Stock_data_entry newInstance(String param1, String param2) {
        Stock_data_entry fragment = new Stock_data_entry();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stock, container, false);
        String TAG = "Stock_data_entry";
        serial_number = v.findViewById(R.id.serial_number);
        partnum = v.findViewById(R.id.partnum);
        part_name = v.findViewById(R.id.part_name);
        initial_stock = v.findViewById(R.id.initial_stock);
        stock_in = v.findViewById(R.id.stock_in);
        stock_out = v.findViewById(R.id.stock_out);
        final_stock = v.findViewById(R.id.final_stock);
        invoice_num = v.findViewById(R.id.invoice_num);
        supplier_name = v.findViewById(R.id.supplier_name);
        supplier_company = v.findViewById(R.id.supplier_company);
        remarks = v.findViewById(R.id.remarks);
        approximate_price = v.findViewById(R.id.approximate_price);
        dataDBViewModel = new DataDBViewModel((Application) getContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        stock_add = v.findViewById(R.id.stock_add);
        description_add = v.findViewById(R.id.description_add);
        description_layout = v.findViewById(R.id.description_layout);
        List<View> str = new ArrayList<>();
        description_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View desc_v = inflater.inflate(R.layout.descripiton_items, null);
                EditText part_num = desc_v.findViewById(R.id.part_num);
                EditText part_nam = desc_v.findViewById(R.id.part_nam);
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreateView: " + mAuth.getUid());
        Log.d(TAG, "onCreateView: " + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("stock").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ");
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                        String name = document.getString("user_name");
//                        String job_type = document.getString("job");
//                        String mail = document.getString("mailid");
//                        String phone = document.getString("phone");
//                        String company_type = document.getString("company");
//                        String company_name = document.getString("company_name");
//                        String address = document.getString("address");
//                        String pincode = document.getString("pincode");
                        String time_stamp = document.getString("timestamp");
                        String serial_number_ = document.getString("serial_number");
                        String part_name_ = document.getString("part_name");
                        String initial_stock_ = document.getString("initial_stock");
                        String final_stock_ = document.getString("final_stock");
                        String stock_in_ = document.getString("stock_in");
                        String stock_out_ = document.getString("stock_out");
                        String invoice_num_ = document.getString("invoice_num");
                        String supplier_name_ = document.getString("supplier_name");
                        String supplier_company_ = document.getString("supplier_company");
                        String approximate_price_ = document.getString("approximate_price");
                        String remarks_ = document.getString("remarks");
                        String time = document.getString("time");
                        String k = "Serial number :," +
                                serial_number_ +
                                part_name_ +
                                ",Initial stock :," +
                                initial_stock_ +
                                ",Stock in :," +
                                stock_in_ +
                                ",Stock out :," +
                                stock_out_ +
                                ",Final stock :," +
                                final_stock_ +
                                ",Invoice number :," +
                                invoice_num_ +
                                ",Supplier name :," +
                                supplier_name_ +
                                ",Supplier company :," +
                                supplier_company_ +
                                ",Approximate price :," +
                                approximate_price_ +
                                ",Remarks :," +
                                remarks_;
                        if (time_stamp != null) {
                            DataDB dataDB = new DataDB(k, Long.parseLong(time_stamp), "stock", approximate_price_, time);
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
        stock_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serial_number_ = serial_number.getText().toString().trim().length() > 0 ? serial_number.getText().toString().trim() : "N/A";
                String part_name_ = ",Part name 1:," + (part_name.getText().toString().trim().length() > 0 ? part_name.getText().toString().trim() : "N/A" + ",Part number 1:," + (partnum.getText().toString().trim().length() > 0 ? partnum.getText().toString().trim() : "N/A"));
                String initial_stock_ = initial_stock.getText().toString().trim().length() > 0 ? initial_stock.getText().toString().trim() : "N/A";
                String stock_in_ = stock_in.getText().toString().trim().length() > 0 ? stock_in.getText().toString().trim() : "N/A";
                String stock_out_ = stock_out.getText().toString().trim().length() > 0 ? stock_out.getText().toString().trim() : "N/A";
                String final_stock_ = final_stock.getText().toString().trim().length() > 0 ? final_stock.getText().toString().trim() : "N/A";
                String invoice_num_ = invoice_num.getText().toString().trim().length() > 0 ? invoice_num.getText().toString().trim() : "N/A";
                String supplier_name_ = supplier_name.getText().toString().trim().length() > 0 ? supplier_name.getText().toString().trim() : "N/A";
                String supplier_company_ = supplier_company.getText().toString().trim().length() > 0 ? supplier_company.getText().toString().trim() : "N/A";
                String remarks_ = remarks.getText().toString().trim().length() > 0 ? remarks.getText().toString().trim() : "N/A";
                String approximate_price_ = approximate_price.getText().toString().trim().length() > 0 ? approximate_price.getText().toString().trim() : "0";
                int l = 1;
                for (View temp : str) {
                    l++;
                    EditText part_nam = temp.findViewById(R.id.part_nam);
                    EditText part_num_v = temp.findViewById(R.id.part_num);
                    part_name_ += (",Part name " + l + ":," + (part_nam.getText().toString().trim().length() > 0 ? part_nam.getText().toString().trim() : "N/A")) + (",Part number" + l + ":," + (part_num_v.getText().toString().trim().length() > 0 ? part_num_v.getText().toString().trim() : "N/A"));
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> stock = new HashMap<>();
                stock.put("serial_number", serial_number_);
                stock.put("part_name", part_name_);
                stock.put("initial_stock", initial_stock_);
                stock.put("stock_in", stock_in_);
                stock.put("stock_out", stock_out_);
                stock.put("final_stock", final_stock_);
                stock.put("invoice_num", invoice_num_);
                stock.put("supplier_name", supplier_name_);
                stock.put("supplier_company", supplier_company_);
                stock.put("remarks", remarks_);
                stock.put("approximate_price", approximate_price_);
                String k = "Serial number :," +
                        serial_number_ +
                        part_name_ +
                        ",Initial stock :," +
                        initial_stock_ +
                        ",Stock in :," +
                        stock_in_ +
                        ",Stock out :," +
                        stock_out_ +
                        ",Final stock :," +
                        final_stock_ +
                        ",Invoice number :," +
                        invoice_num_ +
                        ",Supplier name :," +
                        supplier_name_ +
                        ",Supplier company :," +
                        supplier_company_ +
                        ",Approximate price :," +
                        approximate_price_ +
                        ",Remarks :," +
                        remarks_;
                for (View temp : str) {
                    EditText part_nam = temp.findViewById(R.id.part_nam);
                    EditText part_num_v = temp.findViewById(R.id.part_num);
                    part_nam.setText("");
                    part_num_v.setText("");
                }
                DateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.ENGLISH);
                String time = sdf4.format(Calendar.getInstance().getTimeInMillis());
                String timeStamp = "" + Calendar.getInstance().getTimeInMillis();
                stock.put("timestamp", timeStamp);
                stock.put("time", time);

                DataDB dataDB = new DataDB(k, Long.parseLong(timeStamp), "stock", approximate_price_, time);
                dataDBViewModel.insert(dataDB, getContext());
                dataViewAdapter.notifyDataSetChanged();
                db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("stock").add(stock).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            serial_number.setText("");
                            initial_stock.setText("");
                            stock_in.setText("");
                            stock_out.setText("");
                            final_stock.setText("");
                            invoice_num.setText("");
                            supplier_name.setText("");
                            supplier_company.setText("");
                            remarks.setText("");
                            Toast.makeText(getContext(), "Stock Added Successfully!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Stock not added,Try after some time", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        return v;
    }
}