package com.example.albawabacp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albawabacp.Moduls.Ad;
import com.example.albawabacp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdAdapter extends FirestoreRecyclerAdapter<Ad, AdAdapter.AdHolder> {
    private static final String TAG = AdAdapter.class.getSimpleName();
    private AdClickListener adClickListener;

    public AdAdapter(@NonNull FirestoreRecyclerOptions<Ad> options) {
        super(options);
    }

    public static AdAdapter get() {
        Query query = FirebaseFirestore.getInstance()
                .collection("houses");
        FirestoreRecyclerOptions<Ad> options = new FirestoreRecyclerOptions.Builder<Ad>()
                .setQuery(query, Ad.class)
                .build();
        return new AdAdapter(options);
    }

    public AdClickListener getAdClickListener() {
        if (adClickListener == null) {
            adClickListener = new AdClickListener() {
                @Override
                public void onAdClick(Ad ad, int position) {
                    Log.e(TAG, "you need to call setAdClickListener() to set the click listener of AdAdapter ");
                }
            };
        }
        return adClickListener;
    }

    public void setAdClickListener(AdClickListener adClickListener) {
        this.adClickListener = adClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull AdHolder holder, int position, @NonNull Ad ad) {
        holder.bind(ad, position);
    }

    @NonNull
    @Override
    public AdHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new AdHolder(itemView);
    }

    public interface AdClickListener {
        void onAdClick(Ad ad, int position);
    }

    class AdHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView houseNameTextView;

        public AdHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            houseNameTextView = itemView.findViewById(R.id.houseNameTextView);
        }

        private void bind(final Ad ad, final int position) {
            houseNameTextView.setText(ad.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdClickListener().onAdClick(ad, position);
                }
            });
        }
    }
}
