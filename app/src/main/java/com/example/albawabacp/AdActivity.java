package com.example.albawabacp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.albawabacp.Adapters.PhotoAdapter;
import com.example.albawabacp.Moduls.Ad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AdActivity extends AppCompatActivity implements View.OnClickListener, PhotoAdapter.OnDeleteButtonListener, PhotoAdapter.OnPhotoListener {
    public static final String AD_TAG = AdActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    private final int CHANGE_IMAGE_REQUEST = 72;
    Ad currentAd;
    String currentId;
    PhotoAdapter photoAdapter;
    TextView titleTextView;
    EditText nameEditText;
    EditText detailedAddressEditText;
    EditText contactEditText;
    EditText rentPriceEditText;
    EditText contentEditText;
    EditText locationEditText;
    SwitchCompat statueSwitchCompat;
    CheckBox poolCheckBox;
    CheckBox goodViewCheckBox;
    CheckBox electricCheckBox;
    CheckBox kidsPoolCheckBox;
    CheckBox closeToTownCheckBox;
    CheckBox waterCheckBox;
    CheckBox tvCheckBox;
    CheckBox parkingPlaceCheckBox;
    CheckBox bedroomsCheckBox;
    CheckBox kitchenCheckBox;
    Button saveButton;
    RecyclerView imagesRecyclerView;
    ImageView mainImageView;
    ImageButton addImageButton;
    ImageView viewChosenImageImageView;
    Button changePhotoButton;
    private Uri filepath;
    private Uri mainFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        titleTextView = findViewById(R.id.titleTextView);
        nameEditText = findViewById(R.id.nameEditText);
        detailedAddressEditText = findViewById(R.id.detailedAddressEditText);
        contactEditText = findViewById(R.id.contactEditText);
        rentPriceEditText = findViewById(R.id.rentPriceEditText);
        contentEditText = findViewById(R.id.contentEditText);
        locationEditText = findViewById(R.id.locationEditText);
        statueSwitchCompat = findViewById(R.id.statueSwitchCompat);
        poolCheckBox = findViewById(R.id.poolCheckBox);
        goodViewCheckBox = findViewById(R.id.goodViewCheckBox);
        electricCheckBox = findViewById(R.id.electricCheckBox);
        kidsPoolCheckBox = findViewById(R.id.kidsPoolCheckBox);
        closeToTownCheckBox = findViewById(R.id.closeToTownCheckBox);
        waterCheckBox = findViewById(R.id.waterCheckBox);
        tvCheckBox = findViewById(R.id.tvCheckBox);
        parkingPlaceCheckBox = findViewById(R.id.parkingPlaceCheckBox);
        bedroomsCheckBox = findViewById(R.id.bedroomsCheckBox);
        kitchenCheckBox = findViewById(R.id.kitchenCheckBox);
        saveButton = findViewById(R.id.saveButton);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);
        mainImageView = findViewById(R.id.mainImageView);
        addImageButton = findViewById(R.id.addImageButton);
        viewChosenImageImageView = findViewById(R.id.viewChosenImageImageView);
        changePhotoButton = findViewById(R.id.changePhotoButton);

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentAd = Parcels.unwrap(getIntent().getParcelableExtra(AD_TAG));
        currentId = getIntent().getStringExtra("id");
        if (currentAd != null) {
            nameEditText.setText(currentAd.getName());
            contactEditText.setText(currentAd.getContact());
            String price = currentAd.getPrice() + "";
            rentPriceEditText.setText(price);
            contentEditText.setText(currentAd.getContent());
            locationEditText.setText(currentAd.getAddress());
            detailedAddressEditText.setText(currentAd.getDetailedAddress());
            statueSwitchCompat.setChecked(currentAd.getFeatures().get(0));
            poolCheckBox.setChecked(currentAd.getFeatures().get(1));
            electricCheckBox.setChecked(currentAd.getFeatures().get(2));
            closeToTownCheckBox.setChecked(currentAd.getFeatures().get(3));
            tvCheckBox.setChecked(currentAd.getFeatures().get(4));
            bedroomsCheckBox.setChecked(currentAd.getFeatures().get(5));
            goodViewCheckBox.setChecked(currentAd.getFeatures().get(6));
            kidsPoolCheckBox.setChecked(currentAd.getFeatures().get(7));
            waterCheckBox.setChecked(currentAd.getFeatures().get(8));
            parkingPlaceCheckBox.setChecked(currentAd.getFeatures().get(9));
            kitchenCheckBox.setChecked(currentAd.getFeatures().get(10));
            if (!currentAd.getMainImageUrl().equals("null")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(currentAd.getMainImageUrl());
                Glide.with(AdActivity.this)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .override(Target.SIZE_ORIGINAL, 700)
                                .placeholder(R.drawable.ic_cached)
                                .error(R.drawable.ic_error_outline)
                                .centerCrop())
                        .load(storageReference)
                        .into(mainImageView);
            } else {
                changePhotoButton.setText("اضافة صورة");
                nameEditText.setText("");
                contactEditText.setText("");
                rentPriceEditText.setText("");
                contentEditText.setText("");
                locationEditText.setText("");
                detailedAddressEditText.setText("");
            }
            photoAdapter = new PhotoAdapter(currentAd.getImagesUrls());
            photoAdapter.setOnDeleteButtonListener(this);
            photoAdapter.setOnPhotoListener(this);
            imagesRecyclerView.setAdapter(photoAdapter);
        }
        saveButton.setOnClickListener(this);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(PICK_IMAGE_REQUEST);
            }
        });
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(CHANGE_IMAGE_REQUEST);
            }
        });
    }

    private void chooseImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "يرجى اختيار الصورة "), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                viewChosenImageImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CHANGE_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mainFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mainFilePath);
                mainImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (currentAd != null) {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ad_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionDelete) {
            FirebaseFirestore.getInstance().collection("houses").document(currentId).delete();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void uploadImage(DocumentReference documentReference) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (filepath != null) {
            String photoPath = "images/" + currentAd.getName() + "/" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(photoPath);
            ref.putFile(filepath);
            currentAd.getImagesUrls().add(photoPath);
            documentReference.update("imagesUrls", currentAd.getImagesUrls());
        }
        if (mainFilePath != null) {
            String mainPhotoPath = "images/" + currentAd.getName() + "/" + UUID.randomUUID().toString();
            StorageReference reference = storageReference.child(mainPhotoPath);
            reference.putFile(mainFilePath);
            documentReference.update("mainImageUrl", mainPhotoPath);
        }
    }

    @Override
    public void onPhotoClicked(String photoUrl) {

    }

    @Override
    public void onBackPressed() {

        if (nameEditText.getText().toString().equals("") || contactEditText.getText().toString().equals("") || rentPriceEditText.getText().toString().equals("") ||
                contentEditText.getText().toString().equals("") || locationEditText.getText().toString().equals("") || locationEditText.getText().toString().equals("") ||
                currentAd.getMainImageUrl().equals("null") || currentAd.getImagesUrls().size() == 0) {
            Toast.makeText(AdActivity.this, "يرجى تعبئة جميع البيانات المطلوبة", Toast.LENGTH_LONG).show();
        } else {
            updateAd();
            super.onBackPressed();
        }
    }

    @Override
    public void onDeleteButtonClicked(String photoUrl, int position) {
        currentAd.getImagesUrls().remove(photoUrl);
        if (photoAdapter != null) {
            photoAdapter.notifyItemRemoved(position);
        }
        FirebaseStorage.getInstance().getReference().child(photoUrl).delete();
        FirebaseFirestore.getInstance().collection("houses").document(currentId).update("imagesUrls", currentAd.getImagesUrls());
    }

    private void updateAd() {
        ArrayList<Boolean> features = new ArrayList<>();
        features.add(statueSwitchCompat.isChecked());
        features.add(poolCheckBox.isChecked());
        features.add(electricCheckBox.isChecked());
        features.add(closeToTownCheckBox.isChecked());
        features.add(tvCheckBox.isChecked());
        features.add(bedroomsCheckBox.isChecked());
        features.add(goodViewCheckBox.isChecked());
        features.add(kidsPoolCheckBox.isChecked());
        features.add(waterCheckBox.isChecked());
        features.add(parkingPlaceCheckBox.isChecked());
        features.add(kitchenCheckBox.isChecked());
        if (!currentId.equals("null")) {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("houses").document(currentId);
            documentReference.update("name", nameEditText.getText().toString());
            documentReference.update("address", locationEditText.getText().toString());
            documentReference.update("content", contentEditText.getText().toString());
            documentReference.update("contact", contactEditText.getText().toString());
            documentReference.update("price", Long.valueOf(rentPriceEditText.getText().toString()));
            documentReference.update("detailedAddress", detailedAddressEditText.getText().toString());
            documentReference.update("features", features);
            uploadImage(documentReference);
        } else {
            currentAd.setFeatures(features);
            currentAd.setName(nameEditText.getText().toString());
            currentAd.setAddress(locationEditText.getText().toString());
            currentAd.setContent(contentEditText.getText().toString());
            currentAd.setContact(contactEditText.getText().toString());
            currentAd.setPrice(Long.valueOf(rentPriceEditText.getText().toString()));
            currentAd.setDetailedAddress(detailedAddressEditText.getText().toString());
            currentAd.setFeatures(features);
            FirebaseFirestore.getInstance().collection("houses").add(currentAd).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    DocumentReference documentReference = task.getResult();
                    uploadImage(documentReference);
                }
            });
        }
    }

}
