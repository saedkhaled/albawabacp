package com.example.albawabacp.Adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.albawabacp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private static final String TAG = PhotoAdapter.class.getSimpleName();
    private ArrayList<String> photosUrls;
    private OnPhotoListener onPhotoListener;
    private OnDeleteButtonListener onDeleteButtonListener;

    public PhotoAdapter(ArrayList<String> photosUrls) {
        this.photosUrls = photosUrls;
    }

    private OnPhotoListener getOnPhotoListener() {
        if (onPhotoListener == null) {
            return new OnPhotoListener() {
                @Override
                public void onPhotoClicked(String photoUrl) {
                    Log.e(TAG, "Error: you must set a listener before start using it!!");

                }
            };
        }
        return onPhotoListener;
    }

    public void setOnPhotoListener(OnPhotoListener onPhotoListener) {
        this.onPhotoListener = onPhotoListener;
    }

    private OnDeleteButtonListener getOnDeleteButtonListener() {
        if (onDeleteButtonListener == null) {
            return new OnDeleteButtonListener() {
                @Override
                public void onDeleteButtonClicked(String photoUrl, int position) {
                    Log.e(TAG, "Error: you must set a listener before start using it!!");

                }
            };
        }
        return onDeleteButtonListener;
    }

    public void setOnDeleteButtonListener(OnDeleteButtonListener onDeleteButtonListener) {
        this.onDeleteButtonListener = onDeleteButtonListener;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PhotoHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_photo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder photoHolder, int i) {
        String photoUrl = photosUrls.get(i);
        photoHolder.bind(photoUrl, i);
    }

    @Override
    public int getItemCount() {
        return photosUrls.size();
    }

    public interface OnPhotoListener {
        void onPhotoClicked(String photoUrl);
    }

    public interface OnDeleteButtonListener {
        void onDeleteButtonClicked(String photoUrl, int position);
    }

    class PhotoHolder extends RecyclerView.ViewHolder {
        View itemView;

        ImageView imagesImageView;
        ImageButton deleteImageButton;

        PhotoHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imagesImageView = itemView.findViewById(R.id.imagesImageView);
            deleteImageButton = itemView.findViewById(R.id.deleteImageButton);
        }

        void bind(final String photoUrl, final int position) {
            if (!photoUrl.equals("null")) {
                StorageReference photoReference = FirebaseStorage.getInstance().getReference().child(photoUrl);
                Glide.with(itemView.getContext())
                        .applyDefaultRequestOptions(new RequestOptions()
                                .override(Target.SIZE_ORIGINAL, 700)
                                .placeholder(R.drawable.ic_cached)
                                .error(R.drawable.ic_error_outline)
                                .centerCrop())
                        .load(photoReference)
                        .into(imagesImageView);
            }
            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnDeleteButtonListener().onDeleteButtonClicked(photoUrl, position);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnPhotoListener().onPhotoClicked(photoUrl);
                }
            });
        }
    }
}