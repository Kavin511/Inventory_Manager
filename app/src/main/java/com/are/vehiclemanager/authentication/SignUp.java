package com.are.vehiclemanager.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.Activities.navigation_drawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class SignUp extends Fragment {
    public ViewPager mViewPager;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    EditText s_name, s_mail, s_password, s_phone, s_address, s_job_title, s_company_type, s_pincode, s_company_name, confirm_pass;
    MaterialButton submit;
    ProgressBar progressBar;
    //    FirebaseApp firebaseApp=FirebaseApp.initializeApp(requireContext());
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SignUp() {
    }

    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        s_company_name = v.findViewById(R.id.et_company_name);
        s_password = v.findViewById(R.id.et_password);
        s_mail = v.findViewById(R.id.et_email);
        submit = v.findViewById(R.id.button_register);
        s_name = v.findViewById(R.id.et_name);
        s_address = v.findViewById(R.id.et_address);
        s_phone = v.findViewById(R.id.et_phone);
        progressBar = v.findViewById(R.id.progressBar);
        s_job_title = v.findViewById(R.id.et_job);
        s_company_type = v.findViewById(R.id.et_company);
        s_pincode = v.findViewById(R.id.et_pincode);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = s_name.getText().toString().trim();
                String pass = s_password.getText().toString().trim();
                String mailid = s_mail.getText().toString().trim();
                String address = s_address.getText().toString().trim();
                String phone = s_phone.getText().toString().trim();
                String job = s_job_title.getText().toString().trim();
                String company = s_company_type.getText().toString().trim();
                String company_name = s_company_name.getText().toString().trim();

                if (company_name.length() == 0)
                    s_company_name.setError("Enter company name");
                String pin = s_pincode.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                boolean flag = false;
                if (TextUtils.isEmpty(user_name) || user_name.length() < 4) {
                    s_name.setError("Username should be greater than 4 characters");
                }
                if (pass.length() < 5 || TextUtils.isEmpty(pass)) {
                    s_password.setError("Password should be greater than 5 characters");
                }
                if (address.length() == 0) {
                    s_address.setError("Enter valid address");
                }
                if (phone.length() != 10) {
                    s_phone.setError("Enter Valid mobile number");
                }

                if (job.length() == 0)
                    s_job_title.setError("Enter job title");
                if (company.length() == 0)
                    s_company_type.setError("Enter company name");
                if (pin.length() == 0)
                    s_pincode.setError("Enter pincode");
                String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                if (mailid.matches(Expn) && !TextUtils.isEmpty(mailid) && mailid.length() != 0) {
                    flag = true;
                } else {
                    s_mail.setError("Enter valid mail");
                }
                if (flag && user_name.length() >= 4
                        && pass.length() >= 5 && address.length() > 0 && phone.length() == 10) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(mailid, pass).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");

//                                       FirebaseUser user = mAuth.getCurrentUser();
                                progressBar.setVisibility(View.INVISIBLE);
                                String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                       DocumentReference documentReference=firebaseFirestore.collection("users").document(userid).collection("user_details");
                                Map<String, Object> user = new HashMap<>();
                                user.put("user_name", user_name);
                                user.put("mailid", mailid);
                                user.put("address", address);
                                user.put("phone", phone);
                                user.put("company", company);
                                user.put("job", job);
                                user.put("pincode", pin);
                                user.put("company_name", company_name);
                                user.put("timestamp", "" + Calendar.getInstance().getTimeInMillis());
                                db.collection("users").document(userid).collection("user_details").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Snackbar snackbar = Snackbar
                                                .make(requireView(), "Account Sucessfully ! ", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        Intent startactivity = new Intent(getContext(), navigation_drawer.class);
                                        startActivity(startactivity);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Process Failed !", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else
                                Toast.makeText(getContext(), "Mail id already registered !", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Invalid credentials ! ", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        return v;
    }
}
