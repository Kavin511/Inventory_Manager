package com.are.vehiclemanager.ui.vehicle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.are.vehiclemanager.db.DataViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Equipments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Equipments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ExtendedFloatingActionButton add;
    RecyclerView recyclerView;
    DataDBViewModel dataDBViewModel;
    DataViewAdapter dataViewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Equipments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Equipments.
     */
    // TODO: Rename and change types and number of parameters
    public static Equipments newInstance(String param1, String param2) {
        Equipments fragment = new Equipments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        super.onViewStateRestored(savedInstanceState);
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
        View v = inflater.inflate(R.layout.fragment_equipments, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        add = v.findViewById(R.id.add_vehicle);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataViewAdapter = new DataViewAdapter(getContext());
        recyclerView.setAdapter(dataViewAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog_equipment bottomSheet = new BottomSheetDialog_equipment();
                bottomSheet.show(getChildFragmentManager(), "ModelBottomSheet");
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("vehicle_details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String s_machine_name = document.getString("machine_name");
                        String s_model_num = document.getString("model_num");
                        String s_serial_num = document.getString("serial_num");
                        String s_year = document.getString("year");
                        String s_reg_num = document.getString("reg_num");
                        String timeStamp = document.getString("timestamp");
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
                        DataDB dataDB = new DataDB(k, Long.parseLong(timeStamp), "vehicle", "0", "0");
                        dataDBViewModel.insert(dataDB, getContext());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(DataDBViewModel.class);
        dataDBViewModel.getGetRecentData(getContext(), "vehicle").observe(requireActivity(), dataViewAdapter::setData);
        return v;

    }
}