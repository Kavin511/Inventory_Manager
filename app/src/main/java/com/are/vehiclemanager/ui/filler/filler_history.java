package com.are.vehiclemanager.ui.filler;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class filler_history extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static int un = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    DataViewAdapter dataViewAdapter;
    DataDBViewModel dataDBViewModel;
    SearchView searchView;
    TextView filter_button;
    DataRepository dataRepository;

    // TODO: Rename and change types of parameters
    public filler_history() {

    }

    //  TODO: Rename and change types and number of parameters
    public static filler_history newInstance(String param1, String param2) {
        filler_history fragment = new filler_history();
        Bundle args = new Bundle();
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
        View v = inflater.inflate(R.layout.fragment_filler_history, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataViewAdapter = new DataViewAdapter(getContext());
        recyclerView.setAdapter(dataViewAdapter);
        extendedFloatingActionButton = v.findViewById(R.id.dowload_data);
        filter_button = v.findViewById(R.id.filter_button);
        searchView = v.findViewById(R.id.search_view);
        searchView.setElevation(2);
        dataRepository = new DataRepository();
        if (dataViewAdapter.getItemCount() == 0)
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
        dataDBViewModel.getGetRecentData(getContext(), "filter").observe(requireActivity(), dataViewAdapter::setData);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getFilterFromDb(query);
                Log.d("Stock_history", "onsubmit: ");
                searchView.setElevation(2);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Stock_history", "textChange: ");
                getFilterFromDb(newText);
                searchView.setElevation(5);
                return true;
            }

            private void getFilterFromDb(String searchText) {
                searchText = "%" + searchText + "%";
                dataDBViewModel
                        .getFilteredData(getContext(), searchText, "filter").observe(requireActivity(), dataViewAdapter::setData);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataDBViewModel.getGetRecentData(getContext(), "filter").observe(requireActivity(), dataViewAdapter::setData);
                searchView.setElevation(2);
                Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
                Log.d("Stock_history", "onClose: ");
                return true;
            }
        });
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Stock_history", "menu click:");
                PopupMenu popupMenu = new PopupMenu(getContext(), filter_button, Gravity.LEFT);
                popupMenu.inflate(R.menu.sort_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.old) {
                            dataDBViewModel.getGetOldData(getContext(), "filter").observe(requireActivity(), dataViewAdapter::setData);
                        } else if (item.getItemId() == R.id.recent) {
                            dataDBViewModel.getGetRecentData(getContext(), "filter").observe(requireActivity(), dataViewAdapter::setData);
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
                File file = new File(exportDir, "filter_inspection_report.csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    dataRepository.getRecentData(getContext(), "filter").observe(requireActivity(), new Observer<List<DataDB>>() {
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


