package com.example.doanthuexe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanthuexe.Activity.TripActivity;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.until.Constant;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.tripViewHolder> {
    private List<Trip> mListTrip;
    private Context mContext;
    private List<Trip> filteredList;
    public TripAdapter(Context context,List<Trip> mListTrip) {
        this.mListTrip = mListTrip;
        this.mContext = context;
        this.filteredList = new ArrayList<>(mListTrip);
    }

    @NonNull
    @Override
    public tripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new tripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tripViewHolder holder, int position) {
            Trip mtrip = mListTrip.get(position);
            if (mtrip == null)
                return;
            Glide.with(holder.itemView.getContext())
                    .load(mtrip.getFirstImage())
                    .into(holder.img_avatar_car);
            holder.txt_name_car.setText(mtrip.getCarName());
            holder.txt_start_time.setText("Bắt đầu: " + (CharSequence) mtrip.getStartTime());
            holder.txt_end_time.setText("Kết thúc: " + (CharSequence) mtrip.getEndTime());
            holder.txt_tongtien.setText("Tổng tiền: " + Constant.currencyFormat.format(mtrip.getSumMoney()) + " ");
            holder.rl_centrel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (context instanceof TripActivity) {
                        ((TripActivity) context).clickOnListItem(mListTrip.get(position),1);
                    }
                }
            });
            holder.txt_code.setText("Mã code: "+mtrip.getCode());
            holder.rl_vitri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (context instanceof TripActivity) {
                        ((TripActivity) context).clickOnListItem(mListTrip.get(position),2);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        if (mListTrip != null) {
            return mListTrip.size();
        }
        return 0;
    }
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(mListTrip);
        } else {
            text = text.toLowerCase();
            for (Trip trip : mListTrip) {
                if (trip.getCarName().toLowerCase().contains(text) ||
                        trip.getFirstImage().toLowerCase().contains(text) ||
                        trip.getStartTime().toLowerCase().contains(text) ||
                        trip.getEndTime().toLowerCase().contains(text) ||
                        trip.getCreateTime().toLowerCase().contains(text) ||
                        String.valueOf(trip.getOwnerId()).contains(text) ||
                        String.valueOf(trip.getClientId()).contains(text) ||
                        String.valueOf(trip.getSumMoney()).contains(text) ||
                        String.valueOf(trip.getState()).contains(text) ||
                        String.valueOf(trip.getNotification()).contains(text) ||
                        String.valueOf(trip.getCode()).contains(text)) {
                    filteredList.add(trip);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class tripViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name_car;
        ImageView img_avatar_car;
        TextView txt_start_time;
        TextView txt_end_time;
        TextView txt_tongtien;
        RelativeLayout rl_centrel;
        TextView txt_code;
        RelativeLayout rl_vitri;

        public tripViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_car = itemView.findViewById(R.id.txt_name_car);
            img_avatar_car = itemView.findViewById(R.id.img_avatar_car);
            txt_start_time = itemView.findViewById(R.id.txt_start_time);
            txt_end_time = itemView.findViewById(R.id.txt_end_time);
            txt_tongtien = itemView.findViewById(R.id.txt_tongtien);
            rl_centrel = itemView.findViewById(R.id.rl_centrel);
            txt_code = itemView.findViewById(R.id.txt_code);
            rl_vitri = itemView.findViewById(R.id.rl_vitri);
        }
    }
}
