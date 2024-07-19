package com.example.doanthuexe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Activity.NotificatoinActivity;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Trip> mListTrip;
    private Context mContext;

    public NotificationAdapter(Context context,List<Trip> mListTrip) {
        this.mListTrip = mListTrip;
        this.mContext = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifi, parent, false);
        return new NotificationViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Trip mtrip = mListTrip.get(position);
        Date currentDate = new Date();
        // Định dạng ngày giờ hiện tại


        long diffInMillies = 0;
        try {
            Date timeOld = Constant.dateFormat.parse(mtrip.getCreateTime());
            diffInMillies = Math.abs(currentDate.getTime() - timeOld.getTime());
            long diffMinutes = diffInMillies / (60 * 1000);
            long diffHours = diffInMillies / (60 * 60 * 1000);
            long diffDays = diffInMillies / (24 * 60 * 60 * 1000);

            // Tạo thông báo dựa trên khoảng cách thời gian
            if (diffDays > 0) {
                holder.txt_time.setText(diffDays + " ngày");
            } else if (diffHours > 0) {
                holder.txt_time.setText(diffHours + " giờ");
            } else {
                holder.txt_time.setText(diffMinutes + " phút");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (mtrip.getState() == 0) {
                Constant.getUserByUserId(mtrip.getClientId(), new CallbackUser() {
                @Override
                public void onCallbackUser(User user) {
                    holder.txt_title.setText("Yêu cầu thuê xe");
                    holder.txt_content.setText(user.getUserName() + " yêu cầu thuê xe của bạn");

                }
            });
        }
        if (mtrip.getState() == 1 && Constant.user_current.getUserId() == mtrip.getClientId()) {
            holder.txt_title.setText("Thuê xe");
            holder.txt_content.setText(" Yêu cầu thuê xe của bạn bị từ chối");

        }

        if (mtrip.getState() == 2 && Constant.user_current.getUserId() == mtrip.getClientId()) {
            holder.txt_title.setText(" Thuê xe");
            holder.txt_content.setText(" Yêu cầu thuê xe của bạn được chấp nhận");
            //holder.txt_time.setText(mtrip.getCreateTime());
            holder.txt_code.setText("Mã code: " + mtrip.getCode() + " ");

        }
        if (mtrip.getNotification() == 0) {
            holder.rl_centrel.setBackgroundColor(Color.parseColor("#F3F3F3"));
        }
        holder.rl_centrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = 0;
                mtrip.setNotification(1);

                if (mtrip.getState() == 0) {
                    state = 1;
                } else {
                    state = 2;
                }
                System.out.println("hello");
                Context context = view.getContext();
                if (context instanceof NotificatoinActivity) {
                    ((NotificatoinActivity) context).clickOnListItem(mListTrip.get(position), state);
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


    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_time;
        TextView txt_content;
        RelativeLayout rl_centrel;
        TextView txt_code;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_content = itemView.findViewById(R.id.txt_content);
            rl_centrel = itemView.findViewById(R.id.rl_centrel);
            txt_code = itemView.findViewById(R.id.txt_code);
        }
    }
}
