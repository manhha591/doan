package com.example.doanthuexe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanthuexe.Adapter.PhotoViewPager2Adapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityInforCarBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.models.photo_car;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class InforCarActivity extends AppCompatActivity {
    private User user;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCricleIndicator3;
    private List<photo_car> mListPhoto;
    ActivityInforCarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInforCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewPager2 = binding.viewPager2;
        mCricleIndicator3 = binding.circleIndicator3;
        mListPhoto = getListPhoto();
        PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(mListPhoto);
        mViewPager2.setAdapter(adapter);
        mCricleIndicator3.setViewPager(mViewPager2);
        Constant.getUserByUserId(Constant.car_current.getOwnerId(), new CallbackUser() {
            @Override
            public void onCallbackUser(User user1) {
                user = user1;
                System.out.println(user);
                binding.txtOwnerCar.setText(user.getUserName());
            }
        });
        Car car = Constant.car_current;
        binding.txtNameCar.setText(car.getModel() + " " + car.getYear());
        binding.txtModel.setText(car.getModel());
        binding.txtHang.setText(car.getCompany());
        binding.txtChoNgoi.setText(car.getNumberOfSeats() + "");
        binding.txtDongCo.setText(car.getEngine());
        binding.txtHopSo.setText(car.getGear());
        binding.txtPrice.setText(Constant.currencyFormat.format(car.getPrice()) + "/ ngày");
        binding.txtLocation.setText(car.getLocation());


        if(Constant.isAdmin){
            if(car.getState()==0){
                binding.llBottom.setVisibility(View.VISIBLE);
            }
        }
        binding.txtOwnerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Intent intent = new Intent(InforCarActivity.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car.setState(3);
                Constant.updateCar(car);
                finish();
            }
        });
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car.setState(1);
                Constant.updateCar(car);
                finish();
            }
        });
    }

    private List<photo_car> getListPhoto() {
        List<photo_car> list = new ArrayList<>();
        list.add(new photo_car(Constant.car_current.getFirstImage()));
        list.add(new photo_car(Constant.car_current.getEndImage()));
        list.add(new photo_car(Constant.car_current.getLeftImage()));
        list.add(new photo_car(Constant.car_current.getRightImage()));
        return list;
    }
}