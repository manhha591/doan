package com.example.doanthuexe.until;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackCar;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.service.CallbackUser;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Constant {

    public static User user_current;
    public static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dp0ctmdlu",
            "api_key", "613553174187298",
            "api_secret", "XWgIP8bV_n-P0M65kt2_FukPiwA",
            "secure", true));
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH'h':mm, dd/MM/yyyy", Locale.getDefault());
    public static boolean isAdmin = false;
    public static Car car_current;
    public static Trip trip_current;
    public static Locale localeVN = new Locale("vi", "VN");
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localeVN);

    public static void getUserByUserId(int userId, CallbackUser callback) {

        ApiService.apiService.getUserByUserId(userId)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }

                            //    Toast.makeText(InfoCarActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallbackUser(user);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Toast.makeText(, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }

    public static void updateCar(Car car) {
        ApiService.apiService.updateCar(car.getCarId(), car).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    // callback.onCallbackCar(mCar);
                } else {
                    // Toast.makeText(AddCarActivity.this, "Thất bai!", Toast.LENGTH_SHORT).show();
                    Log.e("API Response", "Request URL: " + call.request().url());
                    Log.e("API Response", "Response Code: " + response.code());
                    Log.e("API Response", "Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //  Toast.makeText(AddCarActivity.this, "Call api error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void getCarByCarId(int carId, CallbackCar callback) {
        ApiService.apiService.getCarByCarid(carId)
                .enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(Call<Car> call, Response<Car> response) {
                        if (response.isSuccessful()) {
                            Car car = response.body();
//                            for (Documents gp : documents) {
//                                System.out.println(gp);
//                            }

                            //    Toast.makeText(InfoCarActivity.this, "Call thành công!", Toast.LENGTH_LONG).show();
                            // Process the list of images as needed
                            callback.onCallbackCar(car);
                        } else {
                            // Toast.makeText(CropDocumentActivity.this, "Đăng kí thất bại!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Car> call, Throwable t) {
                        //  Toast.makeText(InfoCarActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("CALL API", t.getMessage());

                    }
                });
    }

    public static void updateTrip(Trip trip) {
        ApiService.apiService.UpdateTrip(trip.getTripId(), trip)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            //Toast.makeText(, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                            // callback.onCallbackCar(mCar);
                        } else {
                            // Toast.makeText(AddCarActivity.this, "Thất bai!", Toast.LENGTH_SHORT).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //Toast.makeText(AddCarActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static void getNotifi(int userId, CallbackListTrip callback) {

        ApiService.apiService.getNotifi(userId)
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

    public static  Bitmap createCustomMarker(String text) {
        // Kích thước của bitmap
        int width = 80;
        int height = 150; // Phần nhô xuống bên dưới
        int cornerRadius = 15; // Bán kính bo góc

        // Tạo bitmap trống
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Tạo canvas để vẽ lên bitmap
        Canvas canvas = new Canvas(bitmap);

        // Vẽ hình chữ nhật với phần nhô xuống và bo góc
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.WHITE); // Màu nền của hình chữ nhật
        rectPaint.setStyle(Paint.Style.FILL);

        // Hình chữ nhật bo góc
        RectF rectF = new RectF(0, 0, width, height - 20);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, rectPaint);

        // Phần nhô xuống
        Path path = new Path();
        path.moveTo(width / 2 - 10, height - 20);
        path.lineTo(width / 2, height);
        path.lineTo(width / 2 + 10, height - 20);
        path.close();
        canvas.drawPath(path, rectPaint);

        // Vẽ số
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(139, 69, 19)); // Màu nâu
        textPaint.setTextSize(40); // Kích thước chữ
        textPaint.setAntiAlias(true);

        // Tính toán vị trí để vẽ số ở giữa hình chữ nhật
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = (width - bounds.width()) / 2;
        int y = (height - 20 + bounds.height()) / 2;

        canvas.drawText(text, x, y, textPaint);

        return bitmap;
    }
}
