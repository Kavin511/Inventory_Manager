package com.are.vehiclemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.are.vehiclemanager.Activities.DataActivity;
import com.are.vehiclemanager.ui.stock.Sample;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reports extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reports.
     */
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    MaterialButton equipment_report, filter_report, stock_report;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reports() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Reports newInstance(String param1, String param2) {
        Reports fragment = new Reports();
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
        if (container != null)
            container.removeAllViews();
        View v = inflater.inflate(R.layout.fragment_reports, container, false);
        equipment_report = v.findViewById(R.id.equipment_report);
        filter_report = v.findViewById(R.id.filter_report);
        stock_report = v.findViewById(R.id.stock_report);
        Intent intent = new Intent(getContext(), DataActivity.class);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        equipment_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar snackbar = Snackbar
//                        .make(v, "Processing request ! ", Snackbar.LENGTH_LONG);
//                snackbar.show();
                Toast.makeText(getContext(), "Processing request!", Toast.LENGTH_SHORT).show();
                intent.putExtra("Type", "equipment");
                startActivity(intent);
//                getChildFragmentManager().beginTransaction().replace(R.id.reports,new equipmentFragment()).addToBackStack("root_fragment").commit();
            }
        });
        filter_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar snackbar = Snackbar
//                        .make(v, "Processing request ! ", Snackbar.LENGTH_LONG);
//                snackbar.show();
                Toast.makeText(getContext(), "Processing request!", Toast.LENGTH_SHORT).show();
                intent.putExtra("Type", "filter");
                startActivity(intent);
//                getChildFragmentManager().beginTransaction().replace(R.id.reports,new fillerFragment()).commit();
            }
        });
        stock_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar snackbar = Snackbar
//                        .make(v, "Processing request ! ", Snackbar.LENGTH_LONG);
//                snackbar.show();
                Toast.makeText(getContext(), "Processing request!", Toast.LENGTH_SHORT).show();
                intent.putExtra("Type", "stock");
                startActivity(intent);
//                getChildFragmentManager().beginTransaction().replace(R.id.reports,new Stock_data_entry()).addToBackStack("root_fragment").commit();
            }
        });
        return v;
    }
}