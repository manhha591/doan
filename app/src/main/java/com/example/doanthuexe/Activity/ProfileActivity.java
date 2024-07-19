package com.example.doanthuexe.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivitiProfileBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.User;
import com.example.doanthuexe.until.Constant;

public class ProfileActivity  extends AppCompatActivity {
    ActivitiProfileBinding binding;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitiProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
      //  setContentView(R.layout.activiti_profile);
        user = Constant.user_current;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            if (user != null) {

                binding.btnChangePassword.setVisibility(View.INVISIBLE);
                binding.btnSave.setVisibility(View.INVISIBLE);
            }
        }
        binding.edtName.setText(user.getUserName());
        binding.edtEmail.setText(user.getEmail());
        binding.edtGender.setText(user.getGender());
        binding.edtPhone.setText(user.getPhoneNumber());
        binding.edtAddress.setText(user.getAddress());
        binding.textViewShowWelcome.setText(user.getUserName());
    }


    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
