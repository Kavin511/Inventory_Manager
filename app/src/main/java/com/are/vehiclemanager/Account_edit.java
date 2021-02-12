package com.are.vehiclemanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.are.vehiclemanager.db.DataDBViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Account_edit extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    EditText s_name, s_mail, s_password, s_phone, s_address, s_job_title, s_company_type, s_pincode, s_company_name, confirm_pass;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        s_company_name = findViewById(R.id.et_company_name);
        submit = findViewById(R.id.button_update);
        s_name = findViewById(R.id.et_name);
        s_address = findViewById(R.id.et_address);
        s_phone = findViewById(R.id.et_phone);
        s_job_title = findViewById(R.id.et_job);
        s_company_type = findViewById(R.id.et_company);
        s_pincode = findViewById(R.id.et_pincode);
        LinearLayout account_edit = findViewById(R.id.account_edit);
        DataDBViewModel dataDBViewModel;
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) getApplicationContext()).get(DataDBViewModel.class);
//        dataDBViewModel.getGetRecentData(getApplicationContext(), "profile").observe(requireActivity(), new Observer<List<DataDB>>() {
//            @Override
//            public void onChanged(List<DataDB> dataDBS) {
//                Log.d(TAG, "onChanged: " + dataDBS.size());
//                if (dataDBS == null) {
//                    Log.d(TAG, "onComplete: ");
//                }  else
//                    for (int i = 0; i < dataDBS.size(); i++) {
//                        DataDB dataDB = dataDBS.get(i);
//                        String data = dataDB.getData();
//                        String[] arr = data.split(",");
//                        s_name.setText(Html.fromHtml(arr[1]));
//                        s_job_title.setText(Html.fromHtml(arr[3]));
//                        s_phone.setText(Html.fromHtml(arr[7]));
//                        s_company_type.setText(Html.fromHtml(arr[9]));
//                        s_company_name.setText(Html.fromHtml(arr[11]));
//                        s_address.setText(Html.fromHtml(arr[13]));
//                        s_pincode.setText(Html.fromHtml(arr[15]));
//                    }
//            }
//        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String user_name = s_name.getText().toString().trim();
//                String address = s_address.getText().toString().trim();
//                String phone = s_phone.getText().toString().trim();
//                String job = s_job_title.getText().toString().trim();
//                String company = s_company_type.getText().toString().trim();
//                String company_name = s_company_name.getText().toString().trim();
//                if (company_name.length() == 0)
//                    s_company_name.setError("Enter company name");
//                String pin = s_pincode.getText().toString().trim();
//                boolean flag = false;
//                if (TextUtils.isEmpty(user_name) || user_name.length() < 4) {
//                    s_name.setError("Username should be greater than 4 characters");
//                }
//                if (address.length() == 0) {
//                    s_address.setError("Enter valid address");
//                }
//                if (phone.length() != 10) {
//                    s_phone.setError("Enter Valid mobile number");
//                }
//                if (job.length() == 0)
//                    s_job_title.setError("Enter job title");
//                if (company.length() == 0)
//                    s_company_type.setError("Enter company name");
//                if (pin.length() == 0)
//                    s_pincode.setError("Enter pincode");
//                String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
//                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
//                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
//
//                if (flag && user_name.length() >= 4
//                       && address.length() > 0 && phone.length() == 10) {
//                    String userid = mAuth.getCurrentUser().getUid();
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    Map<String, Object> user = new HashMap<>();
//                    user.put("user_name", user_name);
//                    user.put("address", address);
//                    user.put("phone", phone);
//                    user.put("company", company);
//                    user.put("job", job);
//                    user.put("pincode", pin);
//                    user.put("company_name", company_name);
//                    db.collection("users").document(userid).collection("user_details").document().update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Snackbar snackbar = Snackbar
//                                    .make(account_edit, "Updated Sucessfully ! ", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), "Process Failed !", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getApplicationContext(), "Invalid credentials ! ", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
}