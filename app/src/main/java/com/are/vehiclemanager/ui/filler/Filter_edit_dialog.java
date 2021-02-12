package com.are.vehiclemanager.ui.filler;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Filter_edit_dialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filter_edit_dialog extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static DataDB dataDB = null;
    static int n1 = 0, n2 = 0;
    EditText hydraulicoil, engineoil, transmissionoil, gearoil, coolantoil, filter, partnumm, estimated_cost, ending_reading, starting_reading, model_num, filter2, partnum, spinner;
    Button add_filler, add_button;
    ExtendedFloatingActionButton description_add;
    LinearLayout linearLayout;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout description_layout;
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Filter_edit_dialog(DataDB dataDB) {
        Filter_edit_dialog.dataDB = dataDB;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Filter_edit_dialog.
     */
    // TODO: Rename and change types and number of parameters
    public static Filter_edit_dialog newInstance(String param1, String param2) {
        Filter_edit_dialog fragment = new Filter_edit_dialog(dataDB);
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
        View v = inflater.inflate(R.layout.fragment_filter_edit_dialog, container, false);
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
        spinner = v.findViewById(R.id.spinner);
        Map<String, Object> filler = new HashMap<>();
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        dataDBViewModel = new DataDBViewModel((Application) requireContext().getApplicationContext());
        dataViewAdapter = new DataViewAdapter(getContext());
        List<View> str = new ArrayList<>();
        description_layout = v.findViewById(R.id.description_layout);
//        String[] arr = dataDB.getData().split(",");
        long timestmp = dataDB.getTimeStamp();
//        Spinner spinner=v.findViewById(R.id.spinner);
//        dataDBViewModel=new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(DataDBViewModel.class);
//        dataDBViewModel.getGetRecentData(getActivity().getApplicationContext(),"vehicle").observe(getActivity(), new Observer<List<DataDB>>() {
//            @Override
//            public void onChanged(List<DataDB> dataDBS) {
//                List<String> categories = new ArrayList<String>();
//                int n=0;
//                for (DataDB dataDB:dataDBS)
//                {
//                    String[] val=dataDB.getData().split(",");
//                    categories.add(val[1]);
//                    n++;
//                }
//                if (dataDBS.size()==0)
//                {
//                    Toast.makeText(getActivity().getApplicationContext(), "No equipments added, add equipments to select equipments", Toast.LENGTH_SHORT).show();
//                }
//                if (n==dataDBS.size()) {
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner.setAdapter(dataAdapter);
//                }
//            }
//        });
//        final String[] spinner_item = {""};
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                spinner_item[0] = (String) adapterView.getItemAtPosition(i);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        if (arr.length==22)
//        {
//            model_num.setText(arr[1]);
//            filter.setText(arr[3]);
//            partnumm.setText(arr[5]);
//            engineoil.setText(arr[7]);
//            hydraulicoil.setText(arr[9]);
//            transmissionoil.setText(arr[11]);
//            gearoil.setText(arr[13]);
//            coolantoil.setText(arr[15]);
//            starting_reading.setText(arr[17]);
//            ending_reading.setText(arr[19]);
//            estimated_cost.setText(dataDB.getPrice());
//        }
//        else if (arr.length>22)
//        {
//            int extra=arr.length-22;
//            int cons=6;
//            model_num.setText(arr[1]);
//            filter.setText(arr[3]);
//            partnumm.setText(arr[5]);
//            engineoil.setText(arr[7+extra]);
//            hydraulicoil.setText(arr[9+extra]);
//            transmissionoil.setText(arr[11+extra]);
//            gearoil.setText(arr[13+extra]);
//            coolantoil.setText(arr[15+extra]);
//            starting_reading.setText(arr[17+extra]);
//            ending_reading.setText(arr[19+extra]);
//            estimated_cost.setText(dataDB.getPrice());
//            Log.d("filter", "onCreateView: "+
//                    Arrays.toString(arr));
//            long timeStmp=dataDB.getTimeStamp();
//            for (int i = 1; i < extra; i+=4) {
//                View desc_v=inflater.inflate(R.layout.filter_description_layout,null);
//                EditText part_num=desc_v.findViewById(R.id.filter);
//                EditText part_nam=desc_v.findViewById(R.id.partnum);
//                part_num.setText(arr[cons+i]);
//                part_nam.setText(arr[cons+i+2]);
//                ExtendedFloatingActionButton delete=desc_v.findViewById(R.id.delete);
//                str.add(desc_v);
//                description_layout.addView(desc_v);
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        description_layout.removeView(desc_v);
//                        str.remove(desc_v);
//                    }
//                });
//            }
//
//        }
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
        add_filler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hydraulic = hydraulicoil.getText().toString().trim().length() > 0 ? hydraulicoil.getText().toString().trim() : "N/A";
                hydraulicoil.setText("");
                String spinnerText = spinner.getText().toString().trim().length() > 0 ? spinner.getText().toString().trim() : "N/A";
                spinner.setText("");
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
                StringBuilder filter_details = new StringBuilder(filter_ + partnumm_);
                for (View temp : str) {
                    l++;
                    EditText filter = temp.findViewById(R.id.filter);
                    EditText partnum = temp.findViewById(R.id.partnum);
                    filter_details.append(",Filter type ").append(l).append(":,").append(filter.getText().toString().trim().length() > 0 ? filter.getText().toString().trim() : "N/A").append(",Filter Part number").append(l).append(":,").append(partnum.getText().toString().trim().length() > 0 ? partnum.getText().toString().trim() : "N/A");
                    filter.setText("");
                    partnum.setText("");
                }
                filler.put("hydraulic", hydraulic);
                filler.put("engine_oil", engine);
                filler.put("transmission", transmission);
                filler.put("gearoil", gear);
                filler.put("coolant_oil", coolant);
                filler.put("date", date);
                filler.put("time", time);
                filler.put("cost", cost);
                filler.put("starting", starting);
                filler.put("ending", ending);
                filler.put("model_num", model);
                filler.put("equipment", spinnerText);
                filler.put("filter", filter_details.toString());
                String k = "Equipment : ," + spinnerText + ",Model number :," + model + filter_details + ",Engine oil :," + engine + ",Hydraulic oil :," + hydraulic + ",Transmission oil :," + transmission + ",Gear oil :," + gear + ",Coolant oil :," + coolant + ",Starting reading :," + starting + ",Ending reading :," + ending + ",Estimated cost :," + cost + ",";
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                DataDB data = new DataDB(k, timestmp, "filter", cost, date);
                dataDBViewModel.update(data, requireActivity().getApplicationContext());
                dismiss();
//                db.collection("users").document(userid).collection("filter_details").document().update(filler).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Filter data updated successfully", Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Error occurred,Data not updated in cloud", Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                });
            }
        });
        return v;
    }
}