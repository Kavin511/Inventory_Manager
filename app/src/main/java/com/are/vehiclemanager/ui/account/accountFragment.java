package com.are.vehiclemanager.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.are.vehiclemanager.Account_edit_dailog;
import com.are.vehiclemanager.R;
import com.are.vehiclemanager.Reset_password;
import com.are.vehiclemanager.authentication.MainActivity;
import com.are.vehiclemanager.db.DataDB;
import com.are.vehiclemanager.db.DataDBViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


import static android.content.ContentValues.TAG;

public class accountFragment extends Fragment {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static accountFragment fragment;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    TextView name,
            job_type,
            mail,
            phone,
            company_type,
            company_name,
            address,
            pincode,
            reset_password,
            edit_profile,
            logout;
    CircularImageView img_plus, profile_image;
    String uid = firebaseAuth.getUid();
    FirebaseFirestore firebaseFirestore;
    Button add;
    StorageReference ref;
    private SlideshowViewModel slideshowViewModel;
    private StorageReference mStorageRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        name = root.findViewById(R.id.name);
        job_type = root.findViewById(R.id.job_type);
        mail = root.findViewById(R.id.mail);
        phone = root.findViewById(R.id.phone);
        company_type = root.findViewById(R.id.company_type);
        company_name = root.findViewById(R.id.company_name);
        address = root.findViewById(R.id.address);
        pincode = root.findViewById(R.id.pincode);
        reset_password = root.findViewById(R.id.reset_password);
        edit_profile = root.findViewById(R.id.edit_profile);
        logout = root.findViewById(R.id.logout);
        DataDBViewModel dataDBViewModel;
        img_plus = root.findViewById(R.id.img_plus);
        profile_image = root.findViewById(R.id.img_profile);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mStorageRef.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profile_image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_user));
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });
        dataDBViewModel = new ViewModelProvider((ViewModelStoreOwner) requireActivity()).get(DataDBViewModel.class);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure to logout?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
//                                dataDBViewModel.delete(getContext());
                                startActivity(new Intent(getContext(), MainActivity.class));
                                requireActivity().finish();
                            }
                        }).create().show();
            }
        });
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Reset_password.class));
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Account_edit_dailog bt = new Account_edit_dailog();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                bt.show(activity.getSupportFragmentManager(), "a");
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreateView: " + mAuth.getUid());
        Log.d(TAG, "onCreateView: " + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("user_details").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: ");
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        String name = document.getString("user_name");
                        String job_type = document.getString("job");
                        String mail = document.getString("mailid");
                        String phone = document.getString("phone");
                        String company_type = document.getString("company");
                        String company_name = document.getString("company_name");
                        String address = document.getString("address");
                        String pincode = document.getString("pincode");
                        String time_stamp = document.getString("timestamp");
                        String k = "Name ," +
                                name +
                                ",Job type ," +
                                job_type +
                                ",Mail ," +
                                mail +
                                ",Phone ," +
                                phone +
                                ",Company type ," +
                                company_type +
                                ",Company name ," +
                                company_name +
                                ",Address ," +
                                address +
                                ",Pin code ," +
                                pincode;
                        assert time_stamp != null;
                        DataDB dataDB = new DataDB(k, Long.parseLong(time_stamp), "profile", "0", "0");
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

        Log.d(TAG, "onCreateView: " + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        dataDBViewModel.getGetRecentData(getContext(), "profile").observe(requireActivity(), new Observer<List<DataDB>>() {
            @Override
            public void onChanged(List<DataDB> dataDBS) {
                Log.d(TAG, "onChanged: " + dataDBS.size());
                if (dataDBS == null) {
                    Log.d(TAG, "onComplete: ");
                } else
                    for (int i = 0; i < dataDBS.size(); i++) {
                        DataDB dataDB = dataDBS.get(i);
                        String data = dataDB.getData();
                        String[] arr = data.split(",");
                        name.setText(Html.fromHtml(arr[1]));
                        job_type.setText(Html.fromHtml(arr[3]));
                        mail.setText(Html.fromHtml("<b>" + arr[4] + "</b><br></br>\n" + arr[5]));
                        phone.setText(Html.fromHtml("<b>" + arr[6] + "</b><br></br>\n" + arr[7]));
                        company_type.setText(Html.fromHtml("<b>" + arr[8] + "</b><br></br>\n" + arr[9]));
                        company_name.setText(Html.fromHtml("<b>" + arr[10] + "</b><br></br>\n" + arr[11]));
                        address.setText(Html.fromHtml("<b>" + arr[12] + "</b><br></br>\n" + arr[13]));
                        pincode.setText(Html.fromHtml("<b>" + arr[14] + "</b><br></br>\n" + arr[15]));
                    }
            }
        });
        return root;
    }

    public void updating_equpments() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    profile_image.setImageURI(imageUri);
                    uploadImageToFireabse(imageUri);
                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: ");
                }

            }

        }
    }

    private void uploadImageToFireabse(Uri imageUri) {

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Picasso.get().load(R.drawable.ic_profile).into(profile_image);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}