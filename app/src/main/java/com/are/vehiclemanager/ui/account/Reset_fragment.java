package com.are.vehiclemanager.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.are.vehiclemanager.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reset_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reset_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Reset_fragment fragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reset_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reset_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Reset_fragment newInstance(String param1, String param2) {
        fragment = new Reset_fragment();
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

        View v = inflater.inflate(R.layout.fragment_reset_fragment, container, false);
        EditText first_pass, second_pass;
        MaterialButton reset_password;
        TextView back;
        first_pass = v.findViewById(R.id.first_pass);
        second_pass = v.findViewById(R.id.second_pass);
        reset_password = v.findViewById(R.id.reset_button);
        back = v.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().beginTransaction().replace(R.id.reset_frag, new accountFragment()).commit();
            }
        });
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 3;
                String first_pass_ = first_pass.getText().toString().trim().length() > 5 ? first_pass.getText().toString().trim() : "";
                String second_pass_ = second_pass.getText().toString().trim().length() > 5 ? second_pass.getText().toString().trim() : "";
                if (first_pass_.length() < 5) {
                    first_pass.setError("Enter password of length >5");
                    count--;
                }
                if (second_pass_.length() < 5) {
                    second_pass.setError("Enter password of length >5");
                    count--;
                }
                if (!second_pass_.equals(first_pass_)) {
                    first_pass.setError("Password mismatch");
                    second_pass.setError("Password mismatch");
                    count--;
                }
                if (count == 3) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updatePassword(first_pass_);
                }
            }
        });

        return v;
    }
}