package com.example.doanthuexe.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.photo_car;

import java.util.List;

public class PhotoViewPager2Adapter extends RecyclerView.Adapter<PhotoViewPager2Adapter.photoViewHolder> {
    private List<photo_car>  mListPhoto;

    public PhotoViewPager2Adapter(List<photo_car> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public photoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_car,parent,false);
        return new photoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull photoViewHolder holder, int position) {
        photo_car photo = mListPhoto.get(position);
        if (photo == null){
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(photo.getUrl())
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public class photoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        public photoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo_car);
        }
    }
}
