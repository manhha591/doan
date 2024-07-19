package com.example.doanthuexe.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityLocationBinding;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.TripLocation;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallBackListLocation;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.until.Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private List<LatLng> coordinates;
    ActivityLocationBinding binding;
    private  List<TripLocation> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Thay đổi giá trị coordinates thành danh sách tọa độ thực tế của bạn
        coordinates = new ArrayList<>();
        coordinates.add(new LatLng(10.7769, 106.7009)); // Example coordinate 1
        coordinates.add(new LatLng(10.8231, 106.6297)); // Example coordinate 2
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Thêm các tọa độ khác vào danh sách
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Vẽ lộ trình trên bản đồ
        drawRoute();
    }

    private void drawRoute() {
        getLocation(Constant.trip_current.getTripId(), new CallBackListLocation() {
            @Override
            public void onCallBackListLocation(List<TripLocation> locations) {
                for (TripLocation location : locations){
                    coordinates.add(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                if (googleMap != null && coordinates != null && !coordinates.isEmpty()) {
                    // Tạo đường dẫn dựa trên danh sách tọa độ
                    PolylineOptions options = new PolylineOptions()
                            .color(R.color.brown)
                            .width(15);

                    for (LatLng coordinate : coordinates) {
                        options.add(coordinate);
                    }

                    // Vẽ đường dẫn trên bản đồ
                    Polyline polyline = googleMap.addPolyline(options);

                    // Điều chỉnh tầm nhìn của bản đồ để hiển thị toàn bộ lộ trình
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 10));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getLocation(int tripId, CallBackListLocation callback) {

        ApiService.apiService.GetLocation(tripId)
                .enqueue(new Callback<List<TripLocation>>() {
                    @Override
                    public void onResponse(Call<List<TripLocation>> call, Response<List<TripLocation>> response) {
                        if (response.isSuccessful()) {
                            List<TripLocation> mtrip = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }
//                            for (Car car : cars){
//                                System.out.println(car);
//                            }
                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallBackListLocation(mtrip);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripLocation>> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }

}
