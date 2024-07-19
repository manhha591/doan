package com.example.doanthuexe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanthuexe.Activity.InfoTripActivity;
import com.example.doanthuexe.Activity.InforCarActivity;
import com.example.doanthuexe.Activity.MyCarActivity;
import com.example.doanthuexe.Activity.TripActivity;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.AdminViewHolder> {
    private List<User> mList;
    private List<User> filteredList;
    private Context mContext;

    public UserAdapter(Context context, List<User> list) {
        this.mContext = context;
        this.mList = list;
        this.filteredList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        User user = filteredList.get(position);
        if(!user.getAvatar().equals("")){
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatar())
                    .into(holder.img_avatar_user);
        }
        holder.txtUserName.setText(user.getUserName());
        holder.txtPhoneNumber.setText(user.getPhoneNumber());
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(mList);
        } else {
            text = text.toLowerCase();
            for (User user : mList) {
                if (user.getUserName().toLowerCase().contains(text) || user.getPhoneNumber().toLowerCase().contains(text)) {
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_car) {
                    // Xử lý chức năng thêm
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mList.get(position));
                    Intent intent = new Intent(mContext, MyCarActivity.class);
                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else if (id == R.id.action_trip) {
                    // Xử lý chức năng sửa
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mList.get(position));
                    Intent intent = new Intent(mContext, TripActivity.class);
                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtPhoneNumber;
        ImageView imgMore,img_avatar_user;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMore = itemView.findViewById(R.id.img_more);
            txtUserName = itemView.findViewById(R.id.txt_name);
            img_avatar_user = itemView.findViewById(R.id.img_avatar_user);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phoneNumber);
        }
    }
}
