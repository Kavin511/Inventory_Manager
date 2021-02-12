package com.are.vehiclemanager.ui.equipment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.CSVWriter;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataRepository;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class history extends Fragment {


    String type = "equipment";
    RecyclerView recyclerView;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    DataViewAdapter dataViewAdapter;
    DataDBViewModel dataDBViewModel;
    SearchView searchView;
    TextView filter_button;
    DataRepository dataRepository;

    public history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment history.
     */

    public static history newInstance(String param1, String param2) {
        history fragment = new history();
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
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataViewAdapter = new DataViewAdapter(getContext());
        recyclerView.setAdapter(dataViewAdapter);
        extendedFloatingActionButton = v.findViewById(R.id.dowload_data);
        filter_button = v.findViewById(R.id.filter_button);
        searchView = v.findViewById(R.id.search_view);
        searchView.setElevation(2);
        dataRepository = new DataRepository();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    extendedFloatingActionButton.extend();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    extendedFloatingActionButton.shrink();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(DataDBViewModel.class);
        dataDBViewModel.getGetRecentData(getContext(), type).observe(requireActivity(), dataViewAdapter::setData);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getFilterFromDb(query);
                searchView.setElevation(2);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getFilterFromDb(newText);
                searchView.setElevation(5);
                return true;
            }

            private void getFilterFromDb(String searchText) {
                searchText = "%" + searchText + "%";
                dataDBViewModel
                        .getFilteredData(getContext(), searchText, type).observe(requireActivity(), dataViewAdapter::setData);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataDBViewModel.getGetRecentData(getContext(), type).observe(requireActivity(), dataViewAdapter::setData);
                searchView.setElevation(2);
                Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), filter_button, Gravity.RIGHT);
                popupMenu.inflate(R.menu.sort_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.old) {
//                            dataRepository.getOldData(getContext());
                            dataDBViewModel.getGetOldData(getContext(), "equipment").observe(requireActivity(), dataViewAdapter::setData);
//                            Toast.makeText(getContext(), "Ordered based on old data", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.recent) {
                            dataDBViewModel.getGetRecentData(getContext(), type).observe(requireActivity(), dataViewAdapter::setData);
//                            Toast.makeText(getContext(), "Ordered based on recent data", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(extendedFloatingActionButton.getRootView(), "Processing file import", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.show();
                File exportDir = new File(Environment.getExternalStorageDirectory(), "/ARE/");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                File file = new File(exportDir, "equipment_inspection_report.csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    dataRepository.getRecentData(getContext(), type).observe(requireActivity(), new Observer<List<DataDB>>() {
                        @Override
                        public void onChanged(List<DataDB> dataDBS) {
                            int k = 0;
                            for (int i = 0; i < dataDBS.size(); i++) {
                                DataDB dataDB1 = dataDBS.get(i);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                String[] col2 = dataDB1.getData().split("[,]");
                                csvWrite.writeNext(dataDB1.getTime(), col2);
                                k++;
                            }
                            try {
                                if (k == dataDBS.size()) {
                                    csvWrite.close();
                                    snackbar.setDuration(BaseTransientBottomBar.LENGTH_SHORT);
                                    snackbar.setText("File downloaded in ARE folder").show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();

                                snackbar.setDuration(BaseTransientBottomBar.LENGTH_SHORT);
                                snackbar.setText("File import failed :( ").show();
                            }
                            Log.d("dataDBS", "onChanged: " + dataDBS.size());
                        }
                    });
                } catch (Exception sqlEx) {
                    Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                    snackbar.setDuration(BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setText("File import failed :( ").show();
                }
            }
        });
    }
}
