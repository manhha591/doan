package com.example.doanthuexe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Adapter.CarAdapter;
import com.example.doanthuexe.Adapter.TripAdapter;
import com.example.doanthuexe.Adapter.UserAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityAdminBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallBackListCar;
import com.example.doanthuexe.service.CallbackListTrip;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    RecyclerView rv;
    ActivityAdminBinding binding;
    LinearLayoutManager layoutManager;
    UserAdapter userAdapter;
    TripAdapter tripAdapter;
    CarAdapter carAdapter;
    private Toolbar toolbar;
    private  int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.myTooBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        type =1;
        // Hide the title
//        getSupportActionBar().setTitle("All");

        // Set transparent background

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        rv = findViewById(R.id.rv_user);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        getUser(new callBackListUser() {
            @Override
            public void onCallBackListUser(List<User> users) {
                setCarAdapter(users);
            }
        });


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user) {
            type =1;
            getUser(new callBackListUser() {
                @Override
                public void onCallBackListUser(List<User> users) {
                    userAdapter = new UserAdapter(AdminActivity.this, users);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rv_car);
                    rv.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.trip) {
            type = 2;
           getAllTrip(new CallbackListTrip() {
               @Override
               public void onCallbackkListTrip(List<Trip> trips) {
                   tripAdapter = new TripAdapter(AdminActivity.this,trips);
                   rv.setAdapter(tripAdapter);
                   tripAdapter.notifyDataSetChanged();
               }
           });
        } else if (id == R.id.car) {
            type =3;
             getAllCar(new CallBackListCar() {
                 @Override
                 public void onCallBackListCar(List<Car> carList) {
                     carAdapter = new CarAdapter(AdminActivity.this,carList, 3);
                     rv.setAdapter(carAdapter);
                     carAdapter.notifyDataSetChanged();
                 }
             });
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCarAdapter(List<User> mUser) {

        userAdapter = new UserAdapter(this, mUser);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rv_car);
        rv.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItem exit = menu.findItem(R.id.btn_exit);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Không thực hiện tìm kiếm khi nhấn submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Kiểm tra biến type và gọi phương thức filter tương ứng
                switch (type) {
                    case 1:
                        if (userAdapter != null) {
                            userAdapter.filter(newText);
                        }
                        break;
                    case 2:

                            carAdapter.filter(newText);

                        break;
                    case 3:
                        if (tripAdapter != null) {
                            tripAdapter.filter(newText);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.notification) {
            Intent intent = new Intent(this, NotificationCar.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else if (id == R.id.btn_exit) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public interface callBackListUser {
        void onCallBackListUser(List<User> users);
    }

    private void getUser(callBackListUser callBack) {

        ApiService.apiService.getListUser()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {

                            List<User> users = response.body();

                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callBack.onCallBackListUser(users);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }
    private void getAllCar(CallBackListCar callback) {

        ApiService.apiService.getListCar()
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.isSuccessful()) {

                            List<Car> cars = response.body();

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
    private void getAllTrip(CallbackListTrip callback) {

        ApiService.apiService.getListTrip()
                .enqueue(new Callback<List<Trip>>() {
                    @Override
                    public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                        if (response.isSuccessful()) {

                            List<Trip> trips = response.body();

                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallbackkListTrip(trips);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Trip>> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }
}