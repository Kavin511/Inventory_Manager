package com.example.vehiclemanager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class history extends Fragment {


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
    // TODO: Rename and change types and number of parameters
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
        View v=inflater.inflate(R.layout.fragment_history, container, false);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        String userid=firebaseAuth.getCurrentUser().getUid();
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progressDialog.show();
        LinearLayout linearLayout=v.findViewById(R.id.equipment_history);
        db.collection("users").document(userid).collection("Equipment_details").orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    progressDialog.cancel();
                    progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    int v=0;
                    for(QueryDocumentSnapshot document:task.getResult())
                    {
                        String k="\uD835\uDE3F\uD835\uDE56\uD835\uDE69\uD835\uDE5A : "+document.get("date").toString()+"\n\uD835\uDE4F\uD835\uDE5E\uD835\uDE62\uD835\uDE5A : "+document.get("time").toString()+"\n\uD835\uDE51\uD835\uDE5A\uD835\uDE5D\uD835\uDE5E\uD835\uDE58\uD835\uDE61\uD835\uDE5A \uD835\uDE63\uD835\uDE56\uD835\uDE62\uD835\uDE5A : "+document.get("vehiclename")+"\n\uD835\uDE48\uD835\uDE64\uD835\uDE59\uD835\uDE5A\uD835\uDE61 : "+document.get("model")+"\n\uD835\uDE44\uD835\uDE63\uD835\uDE68\uD835\uDE65\uD835\uDE5A\uD835\uDE58\uD835\uDE69\uD835\uDE5E\uD835\uDE64\uD835\uDE63 \uD835\uDE4D\uD835\uDE5A\uD835\uDE65\uD835\uDE64\uD835\uDE67\uD835\uDE69 : "+document.get("inspectionReport")+"\n\uD835\uDE3F\uD835\uDE5A\uD835\uDE68\uD835\uDE58\uD835\uDE67\uD835\uDE5E\uD835\uDE65\uD835\uDE69\uD835\uDE5E\uD835\uDE64\uD835\uDE63 : "+document.get("desc")+"\n\uD835\uDE4B\uD835\uDE56\uD835\uDE67\uD835\uDE69 \uD835\uDE49\uD835\uDE6A\uD835\uDE62\uD835\uDE57\uD835\uDE5A\uD835\uDE67 : "+document.get("partnum")+"\n\uD835\uDE4C\uD835\uDE6A\uD835\uDE56\uD835\uDE63\uD835\uDE69\uD835\uDE5E\uD835\uDE69\uD835\uDE6E : "+document.get("quantity")+"\n\uD835\uDE3E\uD835\uDE64\uD835\uDE68\uD835\uDE69 : "+document.get("cost")+"\n\uD835\uDE47\uD835\uDE64\uD835\uDE58\uD835\uDE56\uD835\uDE69\uD835\uDE5E\uD835\uDE64\uD835\uDE63 : "+document.get("location")+"\n\uD835\uDE4D\uD835\uDE5A\uD835\uDE62\uD835\uDE56\uD835\uDE67\uD835\uDE60\uD835\uDE68 : "+document.get("remark")+"\n\uD835\uDE3C\uD835\uDE58\uD835\uDE69\uD835\uDE5E\uD835\uDE64\uD835\uDE63 : "+document.get("action");
                        TextView textView = new TextView(getActivity());

                        int padding = getResources().getDimensionPixelOffset(R.dimen.padding);
                        textView.setPadding(padding,padding,padding,padding);
                        textView.setText(k);
                        textView.setBackground(getResources().getDrawable(R.drawable.text_vehicle));
                        v++;
                        if(v%2==0)
                            textView.setBackground(getResources().getDrawable(R.drawable.oddtext));
                        linearLayout.addView(textView);
                    }
                    if(v==0)
                    {
                        TextView textView = new TextView(getActivity());
                        int padding = getResources().getDimensionPixelOffset(R.dimen.padding);
                        textView.setPadding(padding,padding,padding,padding);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        textView.setText("No Datas Found");
                        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                        linearLayout.addView(textView);
                    }
                }
                else {
                    progressDialog.cancel();
                    progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getActivity(),"Problem occured try after some time",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getContext(),"Add Equipment details",Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}