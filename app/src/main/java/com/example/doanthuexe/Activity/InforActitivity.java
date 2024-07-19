package com.example.doanthuexe.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.doanthuexe.R;

public class InforActitivity extends AppCompatActivity {

    ImageView imgclose;
    RelativeLayout rl_acc, rl_my_car,rl_trip, rl_my_wallet, rl_instruct,rl_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_actitivity);
        getFormWidgets();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void getFormWidgets() {
        imgclose  = findViewById(R.id.img_close    );
        rl_acc  = findViewById(R.id.rl_acc);
        rl_my_car = findViewById(R.id.rl_my_car);
        rl_trip = findViewById(R.id.rl_my_trip);
        rl_my_wallet = findViewById(R.id.rl_my_wallet);
        rl_instruct = findViewById(R.id.rl_instruct);
        rl_logout = findViewById(R.id.rl_logout);
        rl_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InforActitivity.this, TripActivity.class));
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        rl_my_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InforActitivity.this, MyCarActivity.class));
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InforActitivity.this,ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InforActitivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}