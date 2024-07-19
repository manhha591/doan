package com.example.doanthuexe.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanthuexe.Adapter.CarAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallBackListCar;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.until.Constant;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {



    static final int SEARCH_ACTIVITY_REQUEST_CODE = 123;
    protected LinearLayoutManager layoutManager;
    public RecyclerView rv_car;
    private GoogleMap mMap;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private Toolbar toolbar;
    private PlacesClient placesClient;
    private List<Car> cars;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private View notificationView;
    private List<MarkerOptions> markerList;
    private   LinearSnapHelper snapHelper;
    protected void onResume() {

        Menu menu = toolbar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.notification);

        Constant.getNotifi(Constant.user_current.getUserId(), new CallbackListTrip() {
            @Override
            public void onCallbackkListTrip(List<Trip> mTrip) {
                boolean check =true;
                for (Trip trip : mTrip){
                    if(trip.getNotification() ==0){
                        check = false;

                        break;
                    }
                }
                if(!check){
                    if (menuItem != null) {
                        Drawable icon = menuItem.getIcon();
                        if (icon != null) {
                            icon = DrawableCompat.wrap(icon);
                            DrawableCompat.setTint(icon, Color.RED); // Thay đổi màu biểu tượng thành màu đỏ
                            menuItem.setIcon(icon);
                        }
                    }
                }else {
                    if (menuItem != null) {
                        Drawable icon = menuItem.getIcon();
                        if (icon != null) {
                            icon = DrawableCompat.wrap(icon);
                            DrawableCompat.setTint(icon, Color.WHITE); // Thay đổi màu biểu tượng thành màu đỏ
                            menuItem.setIcon(icon);
                        }
                    }
                }
            }
        });
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationView = findViewById(R.id.notification);
        toolbar = findViewById(R.id.myTooBar);
        markerList = new ArrayList<MarkerOptions>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Places.initialize(getApplicationContext(), "AIzaSyBOD5-j2ElNi1GuIbPEZntT1iNLHKassW4");
        placesClient = com.google.android.libraries.places.api.Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle the selected place
                LatLng latLng = place.getLatLng();
                zoomOnMap(latLng);
              //  Toast.makeText(getApplicationContext(), place.getAddress(), Toast.LENGTH_LONG).show();
              //  addMarkerForAddress(place.getAddress());
//                addNumberMarker(latLng,4);
                //Toast.makeText(MainActivity.this, "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle errors
               // Toast.makeText(MainActivity.this, "An error occurred: " + status, Toast.LENGTH_SHORT).show();
                Log.i("TAG", String.valueOf(status));
            }
        });

        // Khởi tạo và thiết lập MapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rv_car = findViewById(R.id.rv_car);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setLocationCurent();
        }

        cars = null;
        getAllCar(Constant.user_current.getUserId(), new CallBackListCar() {
            @Override
            public void onCallBackListCar(List<Car> carList) {
                cars = carList;
                setCarAdapter(cars);

                // Lặp qua từng địa chỉ và thêm marker
                for (Car car : carList) {
                    addMarkerForAddress(car.getLocation(), car.getNumberOfSeats());
                }
            }
        });
        Constant.getNotifi(Constant.user_current.getUserId(), new CallbackListTrip() {
            @Override
            public void onCallbackkListTrip(List<Trip> mTrip) {

                for (Trip trip : mTrip){
                    if(trip.getNotification() ==0){
                        Menu menu = toolbar.getMenu();
                        MenuItem menuItem = menu.findItem(R.id.notification);

                        if (menuItem != null) {
                            Drawable icon = menuItem.getIcon();
                            if (icon != null) {
                                icon = DrawableCompat.wrap(icon);
                                DrawableCompat.setTint(icon, Color.RED); // Thay đổi màu biểu tượng thành màu đỏ
                                menuItem.setIcon(icon);
                            }
                        }
                        break;
                    }
                }
            }
        });
        Menu menu = toolbar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.notification);

        Constant.getNotifi(Constant.user_current.getUserId(), new CallbackListTrip() {
            @Override
            public void onCallbackkListTrip(List<Trip> mTrip) {
                boolean check =true;
                for (Trip trip : mTrip){
                    if(trip.getNotification() ==0){
                        check = false;

                        break;
                    }
                }
                if(!check){
                    if (menuItem != null) {
                        Drawable icon = menuItem.getIcon();
                        if (icon != null) {
                            icon = DrawableCompat.wrap(icon);
                            DrawableCompat.setTint(icon, Color.RED); // Thay đổi màu biểu tượng thành màu đỏ
                            menuItem.setIcon(icon);
                        }
                    }
                }else {
                    if (menuItem != null) {
                        Drawable icon = menuItem.getIcon();
                        if (icon != null) {
                            icon = DrawableCompat.wrap(icon);
                            DrawableCompat.setTint(icon, Color.WHITE); // Thay đổi màu biểu tượng thành màu đỏ
                            menuItem.setIcon(icon);
                        }
                    }
                }
            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_car.setLayoutManager(layoutManager);
         snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv_car);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Xác định xem người dùng đã cấp quyền hay không
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocationCurent();
            }
        }
    }

    private void setLocationCurent() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Lưu vị trí hiện tại
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        addOrUpdateCurrentLocationMarker(currentLocation);
                        // Zoom vào vị trí hiện tại trên bản đồ
                        zoomOnMap(currentLocation);

                    }
                })
                .addOnFailureListener(this, e -> {
                  //  Toast.makeText(getApplicationContext(), "faile", Toast.LENGTH_LONG).show();
                });

    }

    private void    setCarAdapter(List<Car> mCars) {
        CarAdapter adapter = new CarAdapter(MainActivity.this,mCars, 2);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rv_car);
        rv_car.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rv_car.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(layoutManager);
                    int position = layoutManager.getPosition(centerView);
                    zoomOnMap(markerList.get(position).getPosition());
                }
            }
        });

    }

    private void zoomOnMap(LatLng latLng) {
        // Tạo một CameraUpdate để zoom vào vị trí được chọn
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13f);
        // Di chuyển camera đến vị trí được chọn
        mMap.animateCamera(cameraUpdate);
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
            Intent intent = new Intent(this, NotificatoinActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else if (id == R.id.btn_menu) {
            Intent intent = new Intent(this, InforActitivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        else if(id == R.id.search){
            Intent intent = new Intent(this, SearchActivity.class);;
            Bundle bundle = new Bundle();
            bundle.putSerializable("listCar", (Serializable) cars);
            intent.putExtras(bundle);
            startActivityForResult(intent,SEARCH_ACTIVITY_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra xem requestCode có phải là của hoạt động mà bạn gửi đi không
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                // Xử lý kết quả ở đây
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Car> selectedCars = (List<Car>) bundle.getSerializable("listCarResulf");
                        setCarAdapter(selectedCars);
                        // Xử lý danh sách các Car được chọn ở đây
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Xử lý khi người dùng hủy bỏ hoạt động
            }
        }
    }
    private List<Car> getListCar() {
        List<Car> list = new ArrayList<>();
        Car car = new Car(
                1,                // ownerId
                123,              // carId
                2024,  // carName (giả sử car_name là một string resource trong resources của bạn)
                5,                // numberOfSeats
                "V6",             // engine
                "Civic",          // model
                "Honda",          // company
                1,                // state
                25000,            // price
                "https://static.danhgiaxe.com/data/201525/designed-in-house-at-ferrari-laferrari-is-a-bold-melange-of-classic-elements-from-maranellos-supercars-of-yore-its-lines-are-sleek-evocative-of-a-space-ship-yielding-the-ultimate-ferrari-hypercar_7007.jpg",

                "Automatic"       // gear
        );
        list.add(car);
        list.add(car);
        list.add(car);
        list.add(car);
        list.add(car);
        list.add(car);
        return list;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Handle marker click event here
                // For example, display a toast message
               //Toast.makeText(MainActivity.this, marker.getId(), Toast.LENGTH_SHORT).show();
                rv_car.smoothScrollToPosition(Integer.parseInt(marker.getId().substring(1)));
                return false; // Return true if you have consumed the event, false otherwise
            }
        });

//        String[] addresses = {"Hà Nội", "Huế", "Đà Nẵng"};
//
//        // Lặp qua từng địa chỉ và thêm marker
//        for (String address : addresses) {
//            addMarkerForAddress(address);
//        }


    }

    private void smoothScrollToCenter(RecyclerView recyclerView) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }

            @Override
            protected int getHorizontalSnapPreference() {
                return SNAP_TO_END;
            }
        };

    }


    private void getAllCar(int userId, CallBackListCar callback) {

        ApiService.apiService.getAllCar(userId)
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.isSuccessful()) {
                            List<Car> cars = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }
                            for (Car car : cars) {
                                System.out.println(car);
                            }
                            //  Toast.makeText(MainActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallBackListCar(cars);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
//                            Log.e("API Response", "Request URL: " + call.request().url());
//                            Log.e("API Response", "Response Code: " + response.code());
//                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }

    public void clickOnListItem(Car car) {
        Constant.car_current = car;
        startActivity(new Intent(MainActivity.this, RentCar.class));
    }

    private Marker currentMarker;

    // Khi nhận được vị trí hiện tại, thêm hoặc cập nhật marker trên bản đồ
    private void addNumberMarker(LatLng position, int number) {
        // Create a bitmap with transparent background
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw a circle
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(50, 50, 50, circlePaint);

        // Draw text (number) in the center of the circle
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(number), 50, 65, textPaint);

        // Add marker to the map
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

    }

    private void addOrUpdateCurrentLocationMarker(LatLng latLng) {
        // Kiểm tra xem currentMarker đã được khởi tạo chưa

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).contentDescription("1");
       mMap.addMarker(markerOptions);

    }

    private void addMarkerForAddress(String addressString, int numberOfState) {
        Bitmap customMarkerBitmap = Constant.createCustomMarker(String.valueOf(numberOfState));
        // Perform geocoding
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                // Tạo LatLng object từ tọa độ
                LatLng location = new LatLng(latitude, longitude);

                // Tạo marker options
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title(addressString);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap));
                markerList.add(markerOptions);
                // Thêm marker vào bản đồ
                mMap.addMarker(markerOptions);

                // Di chuyển camera đến vị trí marker đầu tiên
                if (mMap.getCameraPosition().target == null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                }
            } else {
                showToast("Không tìm thấy địa chỉ: " + addressString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private static final long DOUBLE_BACK_PRESS_INTERVAL = 2000; // 2 seconds
    private long lastBackPressTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (lastBackPressTime == 0 || currentTime - lastBackPressTime > DOUBLE_BACK_PRESS_INTERVAL) {
            // First back press or interval too long, display message
            Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
            lastBackPressTime = currentTime;
        } else {
            // Second back press within interval, reset lastBackPressTime and exit app
            lastBackPressTime = 0;
            super.onBackPressed();
        }
    }

}

