package com.are.vehiclemanager.ui.equipment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link graph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class graph extends Fragment {

    BarChart BarChart;
    AppCompatSpinner spinner;
    DataDBViewModel dataDBViewModel;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    public graph() {
        // Required empty public constructor
    }

    public static graph newInstance(String param1, String param2) {
        graph fragment = new graph();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        spinner = v.findViewById(R.id.spinner);
        BarChart barChart = v.findViewById(R.id.line_chart);
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(DataDBViewModel.class);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    dataDBViewModel.getGetRecentData(getContext(), "equipment").observe(requireActivity(), new Observer<List<DataDB>>() {
                        @Override
                        public void onChanged(List<DataDB> dataDBS) {
                            ArrayList<BarEntry> datavalue = new ArrayList<>();
                            barEntries = new ArrayList<>();
                            int n = 0;
                            float n1 = 0f;
                            for (DataDB dataDB : dataDBS) {
                                datavalue.add(new BarEntry(n, Float.parseFloat(dataDB.getPrice())));
                                n++;
                            }
                            if (n == dataDBS.size()) {
                                BarDataSet barDataSet = new BarDataSet(datavalue, "Cost");
                                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                                dataSets.add(barDataSet);
                                BarData data = new BarData(dataSets);
                                barChart.setNoDataText("No data found ,add data");
                                barChart.setDrawGridBackground(false);
                                barChart.setDrawBorders(false);
                                barChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
                                barChart.setBorderWidth(0);
                                Description description = new Description();
                                description.setText("Equipment graph");
                                barChart.setDescription(description);
                                barChart.setDrawGridBackground(false);
                                barDataSet.setBarBorderColor(getResources().getColor(R.color.bar_highlight));
                                barDataSet.setBarShadowColor(getResources().getColor(R.color.white));
                                barDataSet.setColor(getResources().getColor(R.color.bar_inside_color));
                                barChart.animate();
                                barDataSet.setBarBorderWidth(1);
                                barChart.setDrawBarShadow(false);
                                barChart.setDrawValueAboveBar(true);
                                barChart.setPinchZoom(false);
                                barChart.setDrawGridBackground(false);
                                XAxis xl = barChart.getXAxis();
                                xl.setDrawGridLines(false);
                                YAxis leftAxis = barChart.getAxisLeft();
                                barDataSet.setValueTextSize(10);
                                barDataSet.setValueTypeface(Typeface.DEFAULT);
                                barChart.getXAxis().setCenterAxisLabels(false);
                                leftAxis.setTextSize(16);
                                leftAxis.setCenterAxisLabels(false);
                                xl.setCenterAxisLabels(false);
                                leftAxis.setDrawGridLines(false);
                                barChart.getAxisRight().setEnabled(false);
                                barChart.setDrawBarShadow(false);
                                barChart.setVisibleXRangeMaximum(5);
                                barChart.setData(data);
                                barChart.invalidate();
                            }
                        }
                    });
                } else {
                    dataDBViewModel.getGetOldData(getContext(), "equipment").observe(requireActivity(), new Observer<List<DataDB>>() {
                        @Override
                        public void onChanged(List<DataDB> dataDBS) {
                            ArrayList<BarEntry> datavalue = new ArrayList<>();
                            barEntries = new ArrayList<>();
                            int n = 0;
                            float n1 = 0f;
                            for (DataDB dataDB : dataDBS) {
                                datavalue.add(new BarEntry(n, Float.parseFloat(dataDB.getPrice())));
                                n++;
                            }
                            if (n == dataDBS.size()) {
                                BarDataSet barDataSet = new BarDataSet(datavalue, "Cost");
                                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                                dataSets.add(barDataSet);
                                BarData data = new BarData(dataSets);
                                barChart.setNoDataText("No data found ,add data");
                                barChart.setDrawGridBackground(false);
                                barChart.setDrawBorders(false);
                                barChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.white));
                                barChart.setBorderWidth(0);
                                Description description = new Description();
                                description.setText("Equipment graph");
                                barChart.setDescription(description);
                                barChart.setDrawGridBackground(false);
                                barDataSet.setBarBorderColor(getResources().getColor(R.color.bar_highlight));
                                barDataSet.setBarShadowColor(getResources().getColor(R.color.white));
                                barDataSet.setColor(getResources().getColor(R.color.bar_inside_color));
                                barChart.animate();
                                barDataSet.setBarBorderWidth(1);
                                barChart.setDrawBarShadow(false);
                                barChart.setDrawValueAboveBar(true);
                                barChart.setPinchZoom(false);
                                barChart.setDrawGridBackground(false);
                                XAxis xl = barChart.getXAxis();
                                xl.setDrawGridLines(false);
                                xl.setCenterAxisLabels(false);
                                YAxis leftAxis = barChart.getAxisLeft();
                                leftAxis.setCenterAxisLabels(false);
                                barDataSet.setValueTextSize(10);
                                barDataSet.setValueTypeface(Typeface.DEFAULT);
                                barChart.getXAxis().setCenterAxisLabels(false);
                                leftAxis.setTextSize(16);
                                leftAxis.setDrawGridLines(false);
                                barChart.getAxisRight().setEnabled(false);
                                barChart.setDrawBarShadow(false);
                                barChart.setVisibleXRangeMaximum(5);
                                barChart.setData(data);
                                barChart.invalidate();
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        List<String> categories = new ArrayList<String>();
        categories.add("Sort by recent data entry");
        categories.add("Sort by old data entry");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        return v;

    }
}
