package com.example.doanthuexe.service;

import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.models.TripLocation;
import com.example.doanthuexe.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {


    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.105.241:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @POST("api/Users/Register")
    Call<User> registerUser(@Body User user);

    @POST("api/Users/Login")
    Call<User> login(@Body User user);

    @GET("api/Users/{userId}")
    Call<User> getUserByUserId(@Path("userId") int userId);

    @GET("api/Users/listUser")
    Call<List<User>> getListUser();

    @GET("api/Cars/GetListCar")
    Call<List<Car>> getListCar();
    @GET("api/Cars/GetRegisterCar")
    Call<List<Car>> GetRegisterCar();


    @GET("api/Trips/getListTrip")
    Call<List<Trip>> getListTrip();


    @POST("api/Cars/InsertCar")
    Call<Car> insertCar(@Body Car car);

    @GET("api/Cars/GetCar/{userId}")
    Call<List<Car>> getCar(@Path("userId") int userId);


    @GET("api/Cars/GetAllCar/{userId}")
    Call<List<Car>> getAllCar(@Path("userId") int userId);


    @GET("api/Cars/GetCarByCarID/{carId}")
    Call<Car> getCarByCarid(@Path("carId") int carId);

    @DELETE("api/Cars/DeleteCar/{carId}")
    Call<Void> deleteCar(@Path("carId") int carId);
    @PUT("api/Cars/UpdateCar/{carId}")
    Call<Void> updateCar(@Path("carId") int carId, @Body Car updatedCar);

    @POST("api/Trips/InsertTrip")
    Call<Trip> insertTrip(@Body Trip trip);

    @GET("  api/Trips/getTripSuccess/{userId}")
    Call<List<Trip>> getTripSuccess(@Path("userId") int userId);

    @GET("  api/Trips/getNotifi/{userId}")
    Call<List<Trip>> getNotifi(@Path("userId") int userId);

    @PUT("api/Trips/UpdateTripState/{tripId}")
    Call<Void> updateTripState(@Path("tripId") int tripId, @Body int newState);

    @PUT("api/Trips/UpdateTrip/{tripId}")
    Call<Void> UpdateTrip(@Path("tripId") int tripId, @Body Trip updateTrip);

    @GET("  api/TripLocations/getLocation/{tripId}")
    Call<List<TripLocation>> GetLocation(@Path("tripId") int tripId);

}
