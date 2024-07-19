package com.example.doanthuexe.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanthuexe.R;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackUser;
import com.example.doanthuexe.until.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;

    Button btnLogin, btnSigup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_Email);
        edtPassword = findViewById(R.id.edt_Password);
        btnLogin = findViewById(R.id.btn_login);
        btnSigup= findViewById(R.id.btn_sigup);
        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SigupActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us = edtEmail.getText().toString();
                String pass = edtPassword.getText().toString();
                if(us.equals("Admin") && pass .equals("Admin")){
                    Constant.isAdmin = true;
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    finish();
                }
                else {
                    User user = new User(us, pass, us, us, "", "", "");
                    callApiLogin(user, new CallbackUser() {
                        @Override
                        public void onCallbackUser(User user) {
                            Constant.user_current = user;
//                        System.out.println(Constant.user_current);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void callApiLogin(User user, CallbackUser callback) {
        ApiService.apiService.login(user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User u1 = response.body();
                            callback.onCallbackUser(u1);
                            //mới map sang main chưa truyền data
                        } else {
                            //Toast.makeText(LoginActivity.this, "username or password is incorrect !", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                       // Toast.makeText(LoginActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                        Log.e("API Response", "Request URL: " + t.getMessage());

                    }
                });


    }
}