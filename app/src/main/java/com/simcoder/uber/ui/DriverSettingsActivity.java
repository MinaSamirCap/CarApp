package com.simcoder.uber.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.simcoder.uber.R;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DriverSettingsActivity extends AppCompatActivity {

    private MaterialEditText nameEditText, phoneEditText, carTypeEditText;
    private Button cancelButton, confirmButton;
    private ImageView profileImageView, adsImageView;
    private RadioGroup radioGroup;
    private RotateLoading rotateLoading;

    private FirebaseAuth mAuth;
    private DatabaseReference mDriverDatabase;

    private String userIdString, nameString, phoneString,
            carTypeString, serviceTypeString, profileImageUrlSting;
    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        rotateLoading = findViewById(R.id.rotate_loading);
        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        carTypeEditText = findViewById(R.id.car);

        adsImageView = findViewById(R.id.ads_image_view);
        profileImageView = findViewById(R.id.profile_image_view);

        radioGroup = findViewById(R.id.radio_group);
        cancelButton = findViewById(R.id.cancel_button);
        confirmButton = findViewById(R.id.confirm_button);

        mAuth = FirebaseAuth.getInstance();
        userIdString = mAuth.getCurrentUser().getUid();
        mDriverDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userIdString);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUserInfo();
        loadAds();
    }

    private void getUserInfo() {

        rotateLoading.start();

        mDriverDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        nameString = map.get("name").toString();
                        nameEditText.setText(nameString);
                    }
                    if (map.get("phone") != null) {
                        phoneString = map.get("phone").toString();
                        phoneEditText.setText(phoneString);
                    }
                    if (map.get("car") != null) {
                        carTypeString = map.get("car").toString();
                        carTypeEditText.setText(carTypeString);
                    }
                    if (map.get("service") != null) {
                        serviceTypeString = map.get("service").toString();
                        switch (serviceTypeString) {
                            case "One":
                                radioGroup.check(R.id.one_radio_button);
                                break;
                            case "2 - 3":
                                radioGroup.check(R.id.two_radio_button);
                                break;
                            case "Group":
                                radioGroup.check(R.id.group_radio_button);
                                break;
                        }
                    }
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrlSting = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(profileImageUrlSting).into(profileImageView);
                    }
                }
                rotateLoading.stop();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveUserInformation() {

        rotateLoading.start();

        nameString = nameEditText.getText().toString();
        phoneString = phoneEditText.getText().toString();
        carTypeString = carTypeEditText.getText().toString();

        int selectId = radioGroup.getCheckedRadioButtonId();

        final RadioButton radioButton = findViewById(selectId);

        if (radioButton.getText() == null) {
            return;
        }

        serviceTypeString = radioButton.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", nameString);
        userInfo.put("phone", phoneString);
        userInfo.put("car", carTypeString);
        userInfo.put("service", serviceTypeString);
        mDriverDatabase.updateChildren(userInfo);

        if (resultUri != null) {

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userIdString);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    rotateLoading.stop();
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    mDriverDatabase.updateChildren(newImage);
                    rotateLoading.stop();
                    finish();
                }
            });
        } else {
            rotateLoading.stop();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImageView.setImageURI(resultUri);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void loadAds() {
        Glide.with(getApplication())
                .load(getString(R.string.driver_ad))
                .into(adsImageView);
    }

}