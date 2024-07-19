package com.example.doanthuexe.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Adapter.CarAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallBackListCar;
import com.example.doanthuexe.until.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCarActivity extends AppCompatActivity {
    protected LinearLayoutManager layoutManager;
    public RecyclerView rv_car;
    Toolbar toolbar;
    private List<Car> cars;
    private  User user;
    ImageView img_close;
    @Override
    protected void onResume() {
        cars = null;
        user = Constant.user_current;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }
        getCar(user.getUserId(), new CallBackListCar() {
            @Override
            public void onCallBackListCar(List<Car> carList) {
                cars = carList;
                setCarAdapter();
            }
        });
        super.onResume();
    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void setCarAdapter() {
        layoutManager = new LinearLayoutManager(MyCarActivity.this, RecyclerView.VERTICAL, false);
        rv_car.setLayoutManager(layoutManager);
        CarAdapter adapter = new CarAdapter(MyCarActivity.this,cars,1);
        rv_car.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_list_car);


        rv_car = findViewById(R.id.rv_car);

        Button addCar = findViewById(R.id.btn_addCar);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddCarActivity.class));
            }
        });
        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(Constant.isAdmin){
            addCar.setVisibility(View.GONE);
        }
    }

    private List<Car> getListCar() {
        List<Car> list = new ArrayList<>();
//        Car car = new Car(
//                1,                // ownerId
//                123,              // carId
//                "honda 2024",  // carName (giả sử car_name là một string resource trong resources của bạn)
//                5,                // numberOfSeats
//                "V6",             // engine
//                "Civic",          // model
//                "Honda",          // company
//                1,                // state
//                25000,            // price
//                "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg",
//
//                "Automatic"       // gear
////        );
//        list.add(car);
//        list.add(car);   list.add(car);   list.add(car);   list.add(car);   list.add(car);
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notification) {
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_menu) {
            startActivity(new Intent(this, InforActitivity.class));
        }
        return super.onOptionsItemSelected(item);
    }







    private void getCar(int userId, CallBackListCar callback) {

        ApiService.apiService.getCar(userId)
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.isSuccessful()) {
                            List<Car> cars = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }
                            for (Car car : cars){
                                System.out.println(car);
                            }
                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
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