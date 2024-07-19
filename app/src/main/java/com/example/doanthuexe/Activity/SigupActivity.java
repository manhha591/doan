package com.example.doanthuexe.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanthuexe.R;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.service.CallbackUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigupActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword, edtPhoneNumber;
    Button btnSigup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName = findViewById(R.id.edt_sig_name);
        edtEmail = findViewById(R.id.edt_sig_email);
        edtPassword = findViewById(R.id.edt_signup_password);
        edtPhoneNumber = findViewById(R.id.edt_phone);
        btnSigup = findViewById(R.id.btn_sigup);
        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();
                String phoneNumber = edtPhoneNumber.getText().toString();
//
//
//
                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(SigupActivity.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(txt_pass.length() != txt_repass.length()){
//                    showAlertDialog("Lỗi", "Mật khẩu không khớp");
//                    return;
//                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showAlertDialog("Lỗi", "Email không hợp lệ");
                    return;
                }

                if (phoneNumber.length() != 10) {
                    showAlertDialog("Lỗi", "Số điện thoại phải có 10 chữ số");
                    return;
                }

                User user = new User(username, password, email, phoneNumber, "", "", "");

                callApiRegister(user, new CallbackUser() {
                    @Override
                    public void onCallbackUser(User user) {

                        finish();
                    }
                });
            }
        });

    }
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SigupActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void callApiRegister(User user, CallbackUser callback) {
        ApiService.apiService.registerUser(user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User u1 = response.body();
                            Toast.makeText(SigupActivity.this, "Success!", Toast.LENGTH_LONG).show();
                            callback.onCallbackUser(u1);
                        } else {
                            Toast.makeText(SigupActivity.this, "Fail!", Toast.LENGTH_LONG).show();
                            Log.e("API Response", "Request URL: " + call.request().url());
                            Log.e("API Response", "Response Code: " + response.code());
                            Log.e("API Response", "Response Message: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(SigupActivity.this, "Call api error", Toast.LENGTH_LONG).show();
                    }
                });
    }
}