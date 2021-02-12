package com.are.vehiclemanager;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account_edit_dailog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account_edit_dailog extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    EditText s_name, s_mail, s_password, s_phone, s_address, s_job_title, s_company_type, s_pincode, s_company_name, confirm_pass;
    MaterialButton submit;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account_edit_dailog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account_edit_dailog.
     */
    // TODO: Rename and change types and number of parameters
    public static Account_edit_dailog newInstance(String param1, String param2) {
        Account_edit_dailog fragment = new Account_edit_dailog();
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
        View v = inflater.inflate(R.layout.fragment_account_edit_dailog, container, false);
        s_company_name = v.findViewById(R.id.et_company_name);
        submit = v.findViewById(R.id.button_update);
        s_name = v.findViewById(R.id.et_name);
        s_address = v.findViewById(R.id.et_address);
        s_phone = v.findViewById(R.id.et_phone);
        s_job_title = v.findViewById(R.id.et_job);
        s_company_type = v.findViewById(R.id.et_company);
        s_pincode = v.findViewById(R.id.et_pincode);
        LinearLayout account_edit = v.findViewById(R.id.account_edit);
        DataDBViewModel dataDBViewModel;
        final String[] mail = {""};
        final long[] time_stamp = new long[1];
        dataDBViewModel = new ViewModelProvider(requireActivity()).get(DataDBViewModel.class);
        dataDBViewModel.getGetRecentData(getContext(), "profile").observe(requireActivity(), new Observer<List<DataDB>>() {
            @Override
            public void onChanged(List<DataDB> dataDBS) {
                Log.d(TAG, "onChanged: " + dataDBS.size());
                for (int i = 0; i < dataDBS.size(); i++) {
                    DataDB dataDB = dataDBS.get(i);
                    String data = dataDB.getData();
                    String[] arr = data.split(",");
                    s_name.setText(Html.fromHtml(arr[1]));
                    s_job_title.setText(Html.fromHtml(arr[3]));
                    s_phone.setText(Html.fromHtml(arr[7]));
                    s_company_type.setText(Html.fromHtml(arr[9]));
                    s_company_name.setText(Html.fromHtml(arr[11]));
                    s_address.setText(Html.fromHtml(arr[13]));
                    s_pincode.setText(Html.fromHtml(arr[15]));
                    mail[0] = arr[5];
                    time_stamp[0] = dataDB.getTimeStamp();
                }
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = s_name.getText().toString().trim();
                String address = s_address.getText().toString().trim();
                String phone = s_phone.getText().toString().trim();
                String job = s_job_title.getText().toString().trim();
                String company = s_company_type.getText().toString().trim();
                String company_name = s_company_name.getText().toString().trim();

                int count = 0;
                if (company_name.length() == 0) {
                    s_company_name.setError("Enter company name");
                    count--;
                }
                String pin = s_pincode.getText().toString().trim();
                if (TextUtils.isEmpty(user_name) || user_name.length() < 4) {
                    s_name.setError("Username should be greater than 4 characters");
                    count--;
                }
                if (address.length() == 0) {
                    s_address.setError("Enter valid address");
                    count--;
                }
                if (phone.length() != 10) {
                    s_phone.setError("Enter Valid mobile number");
                    count--;
                }
                if (job.length() == 0) {
                    s_job_title.setError("Enter job title");
                    count--;
                }
                if (company.length() == 0) {
                    s_company_type.setError("Enter company name");
                    count--;
                }
                if (pin.length() == 0) {
                    s_pincode.setError("Enter pincode");
                    count--;
                }
                if (count == 0) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("user_name", user_name);
                    user.put("mailid", mail[0]);
                    user.put("address", address);
                    user.put("phone", phone);
                    user.put("company", company);
                    user.put("job", job);
                    user.put("pincode", pin);
                    user.put("company_name", company_name);
                    db.collection("users").document(Objects.requireNonNull(mAuth.getUid())).collection("user_details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                document.getReference().update(user);
                                String k = "Name ," +
                                        user_name +
                                        ",Job type ," +
                                        job +
                                        ",Mail ," +
                                        mail[0] +
                                        ",Phone ," +
                                        phone +
                                        ",Company type ," +
                                        company +
                                        ",Company name ," +
                                        company_name +
                                        ",Address ," +
                                        address +
                                        ",Pin code ," +
                                        pin;
                                DataDB dataDB = new DataDB(k, time_stamp[0], "profile", "0", "0");
                                dataDBViewModel.insert(dataDB, getContext());
                                Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error occurred,Data not updated", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }

            }
        });
        return v;
    }
}