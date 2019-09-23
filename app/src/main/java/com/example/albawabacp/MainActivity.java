package com.example.albawabacp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albawabacp.Adapters.AdAdapter;
import com.example.albawabacp.Moduls.Ad;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdAdapter.AdClickListener, View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    RecyclerView recyclerView;
    FloatingActionButton addFloatingActionButton;
    AdAdapter adAdapter;
    FirebaseAuth mFirebaseAuth;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        addFloatingActionButton = findViewById(R.id.addFloatingActionButton);
        mFirebaseAuth = FirebaseAuth.getInstance();
        adAdapter = AdAdapter.get();
        adAdapter.setAdClickListener(this);
        addFloatingActionButton.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adAdapter.stopListening();
    }

    @Override
    public void onAdClick(Ad ad, int position) {
        String id = adAdapter.getSnapshots().getSnapshot(position).getId();
        Intent intent = new Intent(MainActivity.this, AdActivity.class);
        intent.putExtra(AdActivity.AD_TAG, Parcels.wrap(ad));
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        ArrayList<Boolean> features = new ArrayList<>();
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        features.add(false);
        FirebaseFirestore.getInstance().collection("houses").add(new Ad(new ArrayList<String>(), "null", "null", "اضغط لاضافة المعلومات", "null", "null", "null", 0, features, "null"));
    }
}
