package com.example.doanthuexe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Activity.InforCarActivity;
import com.example.doanthuexe.Activity.TripActivity;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import java.util.List;

public class RegisterCarAdapter  extends RecyclerView.Adapter<RegisterCarAdapter.RegisterCarViewHolder>{
    private List<Car> mCar;
    private Context mContext;

    public RegisterCarAdapter(Context context,List<Car> carList) {
        this.mCar = carList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RegisterCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifi, parent, false);
        return new RegisterCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterCarViewHolder holder, int position) {
        Car car = mCar.get(position);
        Constant.getUserByUserId(car.getOwnerId(), new CallbackUser() {
            @Override
            public void onCallbackUser(User user) {
                holder.txt_title.setText("Đăng xe");
                holder.txt_content.setText(user.getUserName() + " đã đăng xe");

            }
        });
        holder.rl_centrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.car_current = car;
                Intent intent = new Intent(mContext, InforCarActivity.class);
                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCar != null) {
            return mCar.size();
        }
        return 0;
    }

    public class  RegisterCarViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_content;
        RelativeLayout rl_centrel;


        public  RegisterCarViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);

            txt_content = itemView.findViewById(R.id.txt_content);
            rl_centrel = itemView.findViewById(R.id.rl_centrel);

        }
    }
}
