package com.example.doanthuexe.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanthuexe.Adapter.PhotoViewPager2Adapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityRentCarBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.models.photo_car;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackCar;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentCar extends AppCompatActivity {
    private static User user;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCricleIndicator3;
    private List<photo_car> mListPhoto;
    private Trip trip;
    ImageView img_close;
    private double days;
    private ActivityRentCarBinding binding;

    @Override
    protected void onResume() {

        super.onResume();
    }

    TextView txt_owner_car, txt_change_ST, txt_start_time, txt_change_ET, txt_end_time;

    LinearLayout layout_rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_rent_car);
        binding = ActivityRentCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txt_owner_car = findViewById(R.id.txt_owner_car);
        txt_start_time = findViewById(R.id.txt_start_time);
        txt_end_time = findViewById(R.id.txt_end_time);
        txt_change_ST = findViewById(R.id.txt_change_ST);
        txt_change_ET = findViewById(R.id.txt_change_ET);
        img_close = findViewById(R.id.img_close);
        trip = new Trip();
        txt_change_ST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(new IchangeTime() {
                    @Override
                    public void onChangeTime(String str) {
                        txt_start_time.setText(formattedDateTime);
                        trip.setStartTime(formattedDateTime);
                    }
                });

            }
        });

        binding.txtOwnerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Intent intent = new Intent(RentCar.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        txt_change_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(new IchangeTime() {
                    @Override
                    public void onChangeTime(String str) throws ParseException {
                        System.out.println(formattedDateTime);
                        txt_end_time.setText(formattedDateTime);
                        trip.setEndTime(formattedDateTime);
                        Date startTime = Constant.dateFormat.parse(binding.txtStartTime.getText() + "");

                        // Thời gian kết thúc
                        Date endTime = Constant.dateFormat.parse(binding.txtEndTime.getText() + "");

                        // Tính sự khác biệt giữa hai mốc thời gian (tính bằng mili giây)
                        long differenceInMillis = Math.abs(endTime.getTime() - startTime.getTime());

                        // Số mili giây trong một ngày
                        long millisInDay = 24 * 60 * 60 * 1000;

                        // Tính số ngày
                        days = (double) differenceInMillis / millisInDay;
                        double roundedDays = Math.round(days * 10.0) / 10.0;
                        binding.soNgayThue.setText(roundedDays + "");
                        double sum = Constant.car_current.getPrice() * roundedDays;
                        trip.setSumMoney((int) sum);
                        binding.txtSumPrice.setText(Constant.currencyFormat.format(sum));

                    }
                });

            }
        });
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+user.getPhoneNumber()));
                startActivity(intent);
            }
        });
        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(Intent.ACTION_VIEW);

                // Thiết lập URI để mở ứng dụng tin nhắn với số điện thoại cụ thể
                Uri smsUri = Uri.parse("sms:" + user.getPhoneNumber());

                // Thiết lập URI cho Intent
                messageIntent.setData(smsUri);

                // Thêm nội dung tin nhắn vào Intent
                messageIntent.putExtra("sms_body", "Xin chào, tôi liên hệ với bạn từ ứng dụng GoCar");
                startActivity(messageIntent);
            }
        });
        Constant.getUserByUserId(Constant.car_current.getOwnerId(), new CallbackUser() {
            @Override
            public void onCallbackUser(User user1) {
                user = user1;
                System.out.println(user);
                txt_owner_car.setText(user.getUserName());
            }
        });

        Constant.getCarByCarId(Constant.car_current.getCarId(), new CallbackCar() {
            @Override
            public void onCallbackCar(Car car) {
                Constant.car_current = car;
                binding.txtNameCar.setText(car.getModel() + " "+car.getYear());
                binding.txtModel.setText(car.getModel());
                binding.txtHang.setText(car.getCompany());
                binding.txtChoNgoi.setText(car.getNumberOfSeats() + "");
                binding.txtDongCo.setText(car.getEngine());
                binding.txtHopSo.setText(car.getGear());
                binding.txtPrice.setText(Constant.currencyFormat.format(car.getPrice()) + "");
                trip.setCarId(car.getCarId());
                trip.setOwnerId(car.getOwnerId());
                trip.setCarName(Constant.car_current.getModel() + Constant.car_current.getYear());
                trip.setFirstImage(Constant.car_current.getFirstImage());
                binding.txtLocation.setText(car.getLocation());

            }
        });
        binding.txtOwnerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Intent intent = new Intent(RentCar.this, ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        mViewPager2 = findViewById(R.id.view_pager_2);
        mCricleIndicator3 = findViewById(R.id.circle_indicator_3);
        mListPhoto = getListPhoto();
        PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(mListPhoto);
        mViewPager2.setAdapter(adapter);
        mCricleIndicator3.setViewPager(mViewPager2);


        layout_rent = findViewById(R.id.layout_rent);
        layout_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtStartTime.getText() == "") {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời gian bắt đầu", Toast.LENGTH_LONG).show();
                } else if (binding.txtEndTime.getText() == "") {
                    Toast.makeText(getApplicationContext(), "Chưa chọn thời gian kết thúc", Toast.LENGTH_LONG).show();
                } else {
                    Date currentDate = new Date();
                    // Định dạng ngày giờ hiện tại
                    String formattedDate = Constant.dateFormat.format(currentDate);
                    trip.setCarId(Constant.car_current.getCarId());
                    trip.setCarName(Constant.car_current.getModel() + Constant.car_current.getYear());
                    trip.setState(0);
                    trip.setClientId(Constant.user_current.getUserId());
                    trip.setCreateTime(formattedDate);
                    trip.setNotification(0);
                    trip.setTripId(0);
                    trip.setCode(0);
                    System.out.println(trip);
                    System.out.println(trip);
                    insertTrip(trip);
                }

            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void insertTrip(Trip trip) {
        ApiService.apiService.insertTrip(trip)
                .enqueue(new Callback<Trip>() {
                    @Override
                    public void onResponse(Call<Trip> call, Response<Trip> response) {
                        if (response.isSuccessful()) {
                            Log.e("API response", "success");
                            Toast.makeText(RentCar.this, "Đã gửi yêu cầu!", Toast.LENGTH_LONG).show();
                            // callback.onCallbackCar(car);
                        } else {
                            Toast.makeText(RentCar.this, "Fail!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Trip> call, Throwable t) {
                        Log.e("API Response", t.getMessage());
                        Toast.makeText(RentCar.this, "Call api error", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private interface IchangeTime {
        void onChangeTime(String str) throws ParseException;
    }

    private List<photo_car> getListPhoto() {
        List<photo_car> list = new ArrayList<>();
        list.add(new photo_car(Constant.car_current.getFirstImage()));
        list.add(new photo_car(Constant.car_current.getEndImage()));
        list.add(new photo_car(Constant.car_current.getLeftImage()));
        list.add(new photo_car(Constant.car_current.getRightImage()));
        return list;
    }

    private Calendar selectedDateTime;
    private String formattedDateTime;

    private void showDatePickerDialog(IchangeTime ichangeTime) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.SpinnerDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Lưu ngày đã chọn
                        selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, monthOfYear, dayOfMonth);

                        // Hiển thị TimePickerDialog để chọn thời gian
                        showTimePickerDialog(ichangeTime);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(IchangeTime ichangeTime) {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.SpinnerTimePickerDialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Lưu thời gian đã chọn
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        // Định dạng dữ liệu thành chuỗi

                        formattedDateTime = Constant.dateFormat.format(selectedDateTime.getTime());

                        // In ra chuỗi ngày giờ đã chọn
                        System.out.println("Selected date time: " + formattedDateTime);
                        try {
                            ichangeTime.onChangeTime(formattedDateTime);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        ;
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

}