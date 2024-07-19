package com.example.doanthuexe.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanthuexe.Activity.AddCarActivity;
import com.example.doanthuexe.Activity.InforCarActivity;
import com.example.doanthuexe.Activity.MainActivity;
import com.example.doanthuexe.Activity.MyCarActivity;
import com.example.doanthuexe.Activity.TripActivity;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.until.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.carViewHolder> {
    private List<Car> mListCar;
    private  int Type;
    private Context mContext;
    private List<Car> filteredList;

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    public CarAdapter(Context context,List<Car> mListCar, int Type) {
        this.mListCar = mListCar;
        this.Type = Type;
        this.mContext = context;
        this.filteredList = new ArrayList<>(mListCar);
    }

    @NonNull
    @Override
    public carViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_car, parent, false);
        return new carViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull carViewHolder holder, int position) {
        Car mCar = mListCar.get(position);
        if (mCar == null)
            return;
        Glide.with(holder.itemView.getContext())
                .load(mCar.getFirstImage())
                .into(holder.img_avatar_car);
//        System.out.println(mCar);

        holder.txt_name_car.setText(mCar.getModel()+" "+mCar.getYear());
        holder.txt_gia_thue.setText(Constant.currencyFormat.format(mCar.getPrice() )+ "/ngày");
        holder.txt_cho_ngoi.setText(mCar.getNumberOfSeats() + " ");
        holder.txt_model.setText(mCar.getModel());
        holder.txt_hop_so.setText(mCar.getGear());
        holder.txt_hang.setText(mCar.getCompany());
        holder.txt_vi_tri.setText(mCar.getLocation());

        if (Type == 1 || Type ==3) {
            holder.rl_centrel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.car_current = mCar;
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Type);
                    Intent intent = new Intent(mContext, InforCarActivity.class);
                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            holder.rl_end.setVisibility(View.VISIBLE);
            if(mCar.getState() ==1){
                holder.txt_state.setText("Đã duyệt");
                holder.txt_state.setBackgroundResource(R.drawable.rounded_corner_approved);
            }
            holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editCar(mListCar.get(position));
                }
            });
            if (Type ==3 ){
                holder.btn_edit.setVisibility(View.INVISIBLE);
            }
            holder.btn_delete_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCar(mListCar.get(position), position);
                }
            });
        } else if (Type ==2){
            holder.rl_end.setVisibility(View.GONE);
            holder.rl_centrel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).clickOnListItem(mListCar.get(position));
                    }
                }
            });
        }

    }
    public void deleteCar(Car car, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xóa");
        builder.setMessage("Xác nhận xóa ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý logic khi người dùng chọn xóa
                // Thêm mã xóa ở đây
                car.setState(3);
                ApiService.apiService.updateCar(car.getCarId(), car)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    //   callback.onDeleteCompleted();
                                    Toast.makeText(mContext, "Delete succes!", Toast.LENGTH_LONG).show();
                                    mListCar.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mListCar.size());

                                } else {
                                    Toast.makeText(mContext, "Fail!", Toast.LENGTH_LONG).show();
                                    Log.e("API Response", "Request URL: " + call.request().url());
                                    Log.e("API Response", "Response Code: " + response.code());
                                    Log.e("API Response", "Response Message: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //Toast.makeText(GroupDocumentActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý logic khi người dùng chọn hủy
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    public void editCar(Car car) {
        Constant.car_current = car;
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", car);
        Intent intent = new Intent(mContext, AddCarActivity.class);
        intent.putExtras(bundle);
        Activity activity = (Activity) mContext;
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public int getItemCount() {
        if (mListCar != null) {
            return mListCar.size();
        }
        return 0;
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(mListCar);
        } else {
            text = text.toLowerCase();
            for (Car car : mListCar) {
                // Kiểm tra các trường của đối tượng Car
                if (String.valueOf(car.getCarId()).toLowerCase().contains(text) ||
                        String.valueOf(car.getOwnerId()).toLowerCase().contains(text) ||
                        String.valueOf(car.getYear()).toLowerCase().contains(text) ||
                        String.valueOf(car.getNumberOfSeats()).toLowerCase().contains(text) ||
                        (car.getEngine() != null && car.getEngine().toLowerCase().contains(text)) ||
                        (car.getModel() != null && car.getModel().toLowerCase().contains(text)) ||
                        (car.getCompany() != null && car.getCompany().toLowerCase().contains(text)) ||
                        String.valueOf(car.getState()).toLowerCase().contains(text) ||
                        String.valueOf(car.getPrice()).toLowerCase().contains(text) ||
                        (car.getGear() != null && car.getGear().toLowerCase().contains(text)) ||
                        (car.getNumberPlate() != null && car.getNumberPlate().toLowerCase().contains(text)) ||
                        (car.getLocation() != null && car.getLocation().toLowerCase().contains(text))) {
                    filteredList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class carViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name_car;
        TextView txt_gia_thue;
        ImageView img_avatar_car;
        TextView txt_dong_co;
        TextView txt_cho_ngoi;
        TextView txt_model;
        TextView txt_hop_so;
        TextView txt_hang;
        TextView txt_vi_tri;
        ImageView btn_edit;
        ImageView btn_delete_car;
        RelativeLayout rl_vitri;
        RelativeLayout rl_centrel, rl_end;
        TextView txt_state;

        public carViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_car = itemView.findViewById(R.id.txt_name_car);
            img_avatar_car = itemView.findViewById(R.id.img_avatar_car);
            txt_gia_thue = itemView.findViewById(R.id.txt_gia_thue);
            txt_dong_co = itemView.findViewById(R.id.txt_dong_co);
            txt_cho_ngoi = itemView.findViewById(R.id.txt_cho_ngoi);
            txt_model = itemView.findViewById(R.id.txt_model);
            txt_hop_so = itemView.findViewById(R.id.txt_hop_so);
            txt_hang = itemView.findViewById(R.id.txt_hang);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete_car = itemView.findViewById(R.id.btn_delete_car);
            rl_vitri = itemView.findViewById(R.id.rl_vitri);
            rl_centrel = itemView.findViewById(R.id.rl_centrel);
            rl_end = itemView.findViewById(R.id.rl_end);
            txt_state = itemView.findViewById(R.id.txt_state);
            txt_vi_tri = itemView.findViewById(R.id.txt_vi_tri);
        }
    }
}
