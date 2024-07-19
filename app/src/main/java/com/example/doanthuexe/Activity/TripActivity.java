package com.example.doanthuexe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Adapter.TripAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.until.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripActivity extends AppCompatActivity {
    protected LinearLayoutManager layoutManager;
    public RecyclerView rv_trip;
    Toolbar toolbar;
    List<Trip> trips;
    ImageView img_close;
    private User user;
    @Override
    protected void onResume() {

        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        rv_trip = findViewById(R.id.rv_trip);
        img_close = findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       user = Constant.user_current;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
        }
        trips = null;
        getTrip(user.getUserId(), new CallbackListTrip() {
            @Override
            public void onCallbackkListTrip(List<Trip> mTrip) {
                trips = mTrip;
                setCarAdapter();
            }
        });
    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private List<Trip> getListTrip() {
        List<Trip> list = new ArrayList<>();
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
//        list.add(new Trip("honda", "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg", "05/04/2024",
//                "05/05/2024", 9000000));
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    private void setCarAdapter() {
        layoutManager = new LinearLayoutManager(TripActivity.this, RecyclerView.VERTICAL, false);
        rv_trip.setLayoutManager(layoutManager);

        TripAdapter adapter = new TripAdapter(TripActivity.this,trips);
        rv_trip.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    public void clickOnListItem(Trip trip, int type) {
        Constant.trip_current = trip;
        if(type ==1){
            startActivity(new Intent(TripActivity.this, InfoTripActivity.class));

        }else {
            startActivity(new Intent(TripActivity.this, LocationActivity.class));

        }
    }

    private void getTrip(int userId, CallbackListTrip callback) {

        ApiService.apiService.getTripSuccess(userId)
                .enqueue(new Callback<List<Trip>>() {
                    @Override
                    public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                        if (response.isSuccessful()) {
                            List<Trip> mtrip = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }
//                            for (Car car : cars){
//                                System.out.println(car);
//                            }
                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallbackkListTrip(mtrip);
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