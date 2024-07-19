package com.example.doanthuexe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.doanthuexe.Adapter.NotificationAdapter;
import com.example.doanthuexe.Adapter.TripAdapter;
import com.example.doanthuexe.R;
import com.example.doanthuexe.models.Trip;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackListTrip;
import com.example.doanthuexe.until.Constant;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificatoinActivity extends AppCompatActivity {
    private LinearLayoutManager layoutManager;
    List<Trip> trips ;
    RecyclerView rv_notifi;
    ImageView img_close;
    protected void onResume() {
        trips = null;
        Constant.getNotifi(Constant.user_current.getUserId(), new CallbackListTrip() {
            @Override
            public void onCallbackkListTrip(List<Trip> mTrip) {
                trips = mTrip;
                System.out.println(trips);
                setCarAdapter();
                for (Trip trip : mTrip){
                    if(trip.getNotification() ==0){

                    }
                }
            }
        });
        super.onResume();
    }
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
    }


    private void setCarAdapter() {
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_notifi.setLayoutManager(layoutManager);
        Collections.reverse(trips);
        NotificationAdapter adapter = new NotificationAdapter(NotificatoinActivity.this,trips);
        rv_notifi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void clickOnListItem(Trip trip , int state) {
        Constant.updateTrip(trip);
        Constant.trip_current = trip;
        Bundle bundle = new Bundle();
        bundle.putSerializable("state", state);
        Intent intent = new Intent(this, InfoTripActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}