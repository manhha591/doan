package com.example.doanthuexe.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivitySearchBinding;
import com.example.doanthuexe.models.Car;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    private Car mCar;
    private List<Car> carList;
    float minYears =0,maxYears=0, minValue =0,maxValue=0;
    private NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        carList = (List<Car>) bundle.getSerializable("listCar");
        System.out.println(carList);
        mCar = new Car();
        binding.slidePrice.setLabelFormatter(new LabelFormatter() {
            @Override
            public @NonNull String getFormattedValue(float value) {

                format.setMaximumFractionDigits(0);
                return format.format((double) value);
            }
        });
        binding.slidePrice.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                 minValue = slider.getValues().get(0);

                // Lấy giá trị tối đa của thanh slider
                 maxValue = slider.getValues().get(1);
                System.out.println(minValue);
                System.out.println(maxValue);
                mCar.setPrice((int) value);
                binding.txtPrice.setText(format.format(value));
            }
        });
        binding.slideYears.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                 minYears = slider.getValues().get(0);
                 maxYears = slider.getValues().get(1);
                binding.txtYears.setText(String.valueOf((int) value));
                mCar.setYear((int) value);
            }
        });

        binding.rlCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCarActivity.showInputDialog("Hãng xe", new AddCarActivity.InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtCompany.setText(input); // Xử lý dữ liệu nhập vào ở đây
                        mCar.setCompany(binding.txtCompany.getText().toString());
                    }
                }, SearchActivity.this);
            }
        });
        binding.rlNumOfSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumOfStateSelectionDialog();
            }
        });
        binding.rgHopSo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                if(i == R.id.rb_so_san){
                    mCar.setGear( binding.rbSoSan.getText().toString());
                   // Toast.makeText(getApplicationContext(), binding.rbSoSan.getText().toString(), Toast.LENGTH_SHORT).show();
                }else {
                    mCar.setGear( binding.rbSoTuDong.getText().toString());
                  //  Toast.makeText(getApplicationContext(), binding.rbSoTuDong.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.rgDongCo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                if(i == R.id.rb_xang){
                    mCar.setEngine( binding.rbXang.getText().toString());
              //      Toast.makeText(getApplicationContext(), binding.rbXang.getText().toString(), Toast.LENGTH_SHORT).show();
                }else if(i == R.id.rb_dau){
                    mCar.setEngine( binding.rbDau.getText().toString());

                    //Toast.makeText(getApplicationContext(), binding.rbSoTuDong.getText().toString(), Toast.LENGTH_SHORT).show();
                }else if(i == R.id.rb_hybrid){
                    mCar.setEngine(binding.rbHybrid.getText().toString());
                }else {
                    mCar.setEngine(binding.rbDien.getText().toString());
                }
            }
        });
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch(mCar);
            }
        });
        binding.imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

    }
    private void showNumOfStateSelectionDialog() {
        final String[] NumOfState = {"2", "4", "7", "9","11","16","29", "35","45"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chỗ ngồi")
                .setItems(NumOfState, (dialog, which) -> binding.txtNumOfSeats.setText(NumOfState[which]))
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void performSearch(Car searchModel) {
        List<Car> filteredCars = new ArrayList<>();
        System.out.println(searchModel);
        for (Car car : carList) {
            // Check each property for nullability and match with search criteria
            if ((searchModel.getCompany() == null || car.getCompany().toLowerCase().contains(searchModel.getCompany().toLowerCase())) &&
                    (searchModel.getYear() == 0 || (car.getYear() >= minYears&& car.getYear() <= maxYears))&&
                    (searchModel.getPrice() == 0 || (car.getPrice() >= minValue && car.getPrice() <= maxValue)) &&
                    (searchModel.getGear() == null || car.getGear().toLowerCase().contains(searchModel.getGear().toLowerCase())) &&
                    (searchModel.getEngine() == null || car.getEngine().toLowerCase().contains(searchModel.getEngine().toLowerCase())) &&
                    (searchModel.getNumberOfSeats() == 0 || searchModel.getNumberOfSeats() == car.getNumberOfSeats())){
                filteredCars.add(car);
                System.out.println("hello");
                System.out.println(minYears +" "+ maxYears);
            }
        }
        System.out.println(filteredCars);
        Intent resultIntent = new Intent();
// Đặt dữ liệu kết quả (nếu cần)
        resultIntent.putExtra("listCarResulf", (Serializable) filteredCars);
// Đặt mã kết quả (ví dụ: RESULT_OK, RESULT_CANCELED)
        setResult(RESULT_OK, resultIntent);
        finish();
        // Update RecyclerView with filtered data

    }

}