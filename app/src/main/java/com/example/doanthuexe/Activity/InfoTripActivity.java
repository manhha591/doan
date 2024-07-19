package com.example.doanthuexe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanthuexe.Adapter.PhotoViewPager2Adapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityInfoCarOfTripBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.models.photo_car;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackCar;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoTripActivity extends AppCompatActivity {


    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCricleIndicator3;
    private List<photo_car> mListPhoto;
    private Trip trip;
    private User userOwner;
    private User userClient;

    @Override
    protected void onResume() {

        super.onResume();
    }

    ActivityInfoCarOfTripBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoCarOfTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setContentView(R.layout.activity_info_car_of_trip);

        mViewPager2 = findViewById(R.id.view_pager_2);
        mCricleIndicator3 = findViewById(R.id.circle_indicator_3);
        Constant.getUserByUserId(Constant.trip_current.getOwnerId(), new CallbackUser() {
            @Override
            public void onCallbackUser(User user) {
                userOwner = user;
                binding.txtOwnerCar.setText(user.getUserName());
            }
        });
        Constant.getUserByUserId(Constant.trip_current.getClientId(), new CallbackUser() {
            @Override
            public void onCallbackUser(User user) {
                userClient = user;
                binding.txtClient.setText(user.getUserName());
            }
        });
        Constant.getCarByCarId(Constant.trip_current.getCarId(), new CallbackCar() {
            @Override
            public void onCallbackCar(Car car) {
                Constant.car_current = car;
                System.out.println(car);
                getListPhoto(Constant.car_current);
                mListPhoto = getListPhoto(Constant.car_current);
                PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(mListPhoto);
                mViewPager2.setAdapter(adapter);
                mCricleIndicator3.setViewPager(mViewPager2);
                binding.txtChoNgoi.setText(car.getNumberOfSeats() + " ");
                binding.txtHang.setText(car.getCompany());
                binding.txtModel.setText(car.getModel());
                binding.txtDongCo.setText(car.getEngine());
                binding.txtHopSo.setText(car.getGear());
                binding.txtGiaThue.setText(Constant.currencyFormat.format(car.getPrice()) + "/ngày");
            }
        });

        binding.txtOwnerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userOwner);
                Intent intent = new Intent(InfoTripActivity.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        binding.txtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userClient);
                Intent intent = new Intent(InfoTripActivity.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        binding.txtStartTime.setText("Ngày đi: " + Constant.trip_current.getStartTime());
        binding.txtEndTime.setText("Ngày về: " + Constant.trip_current.getEndTime());
        binding.txtTongChiPhi.setText(Constant.currencyFormat.format(Constant.trip_current.getSumMoney()));
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int state = (int) bundle.getSerializable("state");
            if (state == 1) {
                binding.llBottom.setVisibility(View.VISIBLE);
            }

        }
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.trip_current.setState(1);

                Date currentDate = new Date();
                // Định dạng ngày giờ hiện tại
                String formattedDate = Constant.dateFormat.format(currentDate);

                Constant.trip_current.setCreateTime(formattedDate);

                Constant.updateTrip(Constant.trip_current);

                finish();
            }
        });
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Date currentDate = new Date();
                // Định dạng ngày giờ hiện tại
                String formattedDate = Constant.dateFormat.format(currentDate);
                Constant.trip_current.setState(2);
                Constant.trip_current.setCreateTime(formattedDate);
                Constant.trip_current.setCode(Integer.parseInt((String.valueOf(Constant.trip_current.getTripId()) + String.valueOf(
                        Constant.trip_current.getClientId()) + String.valueOf(Constant.trip_current.getOwnerId()))));
                System.out.println(Constant.trip_current);

                Constant.updateTrip(Constant.trip_current);
                finish();
            }
        });
    }

    private void updateSateOfTrip(int tripId, int newState) {

        Call<Void> call = ApiService.apiService.updateTripState(tripId, newState);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private List<photo_car> getListPhoto(Car car_current) {
        List<photo_car> list = new ArrayList<>();
        list.add(new photo_car(car_current.getFirstImage()));
        list.add(new photo_car(car_current.getEndImage()));
        list.add(new photo_car(car_current.getRightImage()));
        list.add(new photo_car(car_current.getLeftImage()));
        return list;
    }


}