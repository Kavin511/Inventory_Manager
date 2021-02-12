package com.are.vehiclemanager.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.Activities.navigation_drawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends Fragment {

    FirebaseAuth mAuth;
    EditText mail, password;
    MaterialButton submit;
    private String mParam1;
    private String mParam2;

    public Login() {

    }

    //    AppCompatSpinner spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ImageView imageView;
        if (container != null)
            container.removeAllViews();

        mail = v.findViewById(R.id.et_email);
        password = v.findViewById(R.id.et_password);
        submit = v.findViewById(R.id.btn_login);
        imageView = v.findViewById(R.id.app_titile);
//        spinner=v.findViewById(R.id.spinner);
        mAuth = FirebaseAuth.getInstance();
        TextView forgot = v.findViewById(R.id.forgot_password);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.login, new forgot_password()).commit();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setContentView(R.layout.progressdialog);
                progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                progressDialog.show();
                String email = mail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (email.length() == 0) {
                    mail.setError("Enter mail");
                    progressDialog.dismiss();
//                Toast.makeText(getActivity(),"lojgg",Toast.LENGTH_LONG);
                }
                if (TextUtils.isEmpty(pass) || pass.length() < 5) {
                    progressDialog.dismiss();
                    password.setError("Enter valid password");
                }
                if (email.length() > 0 && pass.length() > 0)
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Logged in succesfully", Toast.LENGTH_LONG).show();
                                Intent startactivity = new Intent(getActivity(), navigation_drawer.class);
                                startActivity(startactivity);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Enter valid details", Toast.LENGTH_SHORT).show();


                            }
                        }


                    });
            }
        });
        return v;
    }
}