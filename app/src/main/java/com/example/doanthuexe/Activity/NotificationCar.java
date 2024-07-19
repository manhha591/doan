package com.example.doanthuexe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanthuexe.Adapter.NotificationAdapter;
import com.example.doanthuexe.Adapter.RegisterCarAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityNotificationCarBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallBackListCar;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.until.Constant;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationCar extends AppCompatActivity {
    private LinearLayoutManager layoutManager;
    List<Car> cars ;
    RecyclerView rv_notifi;
    ImageView img_close;
    protected void onResume() {

        super.onResume();
    }
    ActivityNotificationCarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notificatoin);
        rv_notifi = findViewById(R.id.rv_notification);
        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cars = null;
        getRegisterCar( new CallBackListCar() {
            @Override
            public void onCallBackListCar (List<Car> mCars) {
                cars = mCars;

                setCarAdapter();
            }
        });
    }


    private void setCarAdapter() {
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_notifi.setLayoutManager(layoutManager);
        Collections.reverse(cars);
        RegisterCarAdapter adapter = new RegisterCarAdapter(NotificationCar.this,cars);
        rv_notifi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getRegisterCar(CallBackListCar callback) {

        ApiService.apiService.GetRegisterCar()
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.isSuccessful()) {

                            List<Car> cars = response.body();

                        Toast.makeText(NotificationCar.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallBackListCar(cars);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }
}