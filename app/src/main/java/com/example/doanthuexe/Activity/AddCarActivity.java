package com.example.doanthuexe.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.doanthuexe.R;
import com.example.doanthuexe.databinding.ActivityAddCarBinding;
import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.service.ApiService;
import com.example.doanthuexe.until.Constant;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {
    ImageButton imgFirst, imgEnd, imgL, imgR;
    Uri mUri;
    Bitmap bitmap;
    private boolean isCloudinaryInitialized = false;
    private ImageButton clickedImageButton;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;

                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        uploadImage(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        } catch (IOException e) {

                        }
                    }

                }
            }


    );

    private Toolbar toolbar;
    ActivityAddCarBinding binding;
    private static Car mCar;

    String userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_add_car);
        mCar = new Car();
        initConfig();
        imgFirst = findViewById(R.id.btn_firstImg);
        imgEnd = findViewById(R.id.btn_endImg);
        imgR = findViewById(R.id.btn_rightImg);
        imgL = findViewById(R.id.btn_leftImg);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mCar = (Car) bundle.getSerializable("car");
            System.out.println(mCar);
            if (mCar != null) {
                binding.btnThemxe.setVisibility(View.GONE);
                binding.btnCapNhat.setVisibility(View.VISIBLE);
                binding.txtTitle.setText("Chỉnh sửa xe");
                binding.txtHangXe.setText(mCar.getCompany());
                binding.txtModel.setText(mCar.getModel());
                binding.txtLocation.setText(mCar.getLocation());
                binding.txtNumberPlate.setText(mCar.getNumberPlate());
                binding.txtGear.setText(mCar.getGear());
                binding.txtNumOfSeats.setText(mCar.getNumberOfSeats() + "");
                binding.txtYear.setText(mCar.getYear() + "");
                binding.txtEngine.setText(mCar.getEngine());
                binding.txtPrice.setText(Constant.currencyFormat.format(mCar.getPrice()));
                Glide.with(AddCarActivity.this).load(mCar.getFirstImage()).into(binding.btnFirstImg);
                Glide.with(AddCarActivity.this).load(mCar.getLeftImage()).into(binding.btnLeftImg);
                Glide.with(AddCarActivity.this).load(mCar.getRightImage()).into(binding.btnRightImg);
                Glide.with(AddCarActivity.this).load(mCar.getEndImage()).into(binding.btnEndImg);
            }
        }
        imgFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImageButton = imgFirst;
                onClickrequestPermission();


            }
        });
        imgEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImageButton = imgEnd;
                onClickrequestPermission();


            }
        });
        imgL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImageButton = imgL;
                onClickrequestPermission();

            }
        });
        imgR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedImageButton = imgR;
                onClickrequestPermission();

            }
        });
//        RelativeLayout rlcompany = findViewById(R.id.rl_Company);
//        rlcompany.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showInputDialog("Hãng xe");
//            }
//        });
        binding.rlCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("Hãng xe", new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtHangXe.setText(input); // Xử lý dữ liệu nhập vào ở đây
                        mCar.setCompany(binding.txtHangXe.getText().toString());
                    }
                }, AddCarActivity.this);
            }
        });

        binding.rlEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showInputDialog("Động cơ", new InputDialogListener() {
//                    @Override
//                    public void onInputEntered(String input) {
//                        binding.txtEngine.setText(input); // Xử lý dữ liệu nhập vào ở đây
//                        mCar.setEngine(binding.txtHangXe.getText().toString());
//                    }
//                },getApplicationContext());
                showEngineSelectionDialog(new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        mCar.setEngine(input);
                        binding.txtEngine.setText(input);
                    }
                });
            }
        });
        binding.rlGear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGearSelectionDialog(new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        mCar.setGear(input);
                        binding.txtGear.setText(input);
                    }
                });

            }
        });
        binding.rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompletePlace();


            }
        });

        binding.rlModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("Mẫu xe", new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtModel.setText(input); // Xử lý dữ liệu nhập vào ở đây
                        mCar.setModel(binding.txtModel.getText().toString());
                    }
                }, AddCarActivity.this);


            }
        });

        binding.rlNumberPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("Biển số xe", new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtNumberPlate.setText(input); // Xử lý dữ liệu nhập vào ở đây
                        mCar.setNumberPlate(binding.txtNumberPlate.getText().toString());
                    }
                }, AddCarActivity.this);

            }
        });

        binding.rlNumOfSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showInputDialog("Chỗ ngồi", new InputDialogListener() {
//                    @Override
//                    public void onInputEntered(String input) {
//                        binding.txtNumOfSeats.setText(input); // Xử lý dữ liệu nhập vào ở đây
//                        mCar.setNumberOfSeats(Integer.parseInt(binding.txtNumOfSeats.getText().toString()));
//                    }
//                },AddCarActivity.this);
                showNumOfStateSelectionDialog(new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtNumOfSeats.setText(input);
                        mCar.setNumberOfSeats(Integer.parseInt(input));
                    }
                });


            }
        });

        binding.rlPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog("Giá thuê", new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtPrice.setText(Constant.currencyFormat.format(Float.parseFloat(input))); // Xử lý dữ liệu nhập vào ở đây
                        mCar.setPrice(Integer.parseInt(input));
                    }
                }, AddCarActivity.this);

            }
        });
        binding.rlYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearSelectionDialog(new InputDialogListener() {
                    @Override
                    public void onInputEntered(String input) {
                        binding.txtYear.setText(input);
                        mCar.setYear(Integer.parseInt(input));
                    }
                });
            }
        });

        binding.btnThemxe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCar.getYear() == 0 || mCar.getNumberOfSeats() == 0 || mCar.getCompany() == null || mCar.getModel() == null || mCar.getEngine() == null || mCar.getGear() == null || mCar.getNumberPlate() == null || mCar.getLocation() == null) {
                    Toast.makeText(AddCarActivity.this, "Các trường không được để trống", Toast.LENGTH_LONG).show();
                } else {
                    mCar.setOwnerId(Constant.user_current.getUserId());
                    mCar.setCarId(0);
                    mCar.setState(0);
                    System.out.println(mCar);
                    callApiAddCar(mCar);

                }
            }
        });

        binding.btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(mCar);
                Constant.updateCar(mCar);
                finish();


            }
        });
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void onClickrequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {

            ActivityCompat.requestPermissions(AddCarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }

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
         //   Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_menu) {
            startActivity(new Intent(this, InforActitivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadImage(Uri uri) {
        MediaManager.get().upload(uri).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.e("Upload Image", "onStart");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.e("Upload Image", "onProgress");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.e("Upload Image", "onSuccess");
                // Lấy URL của hình ảnh từ resultData
                String imageUrl = (String) resultData.get("secure_url");
                // Sử dụng imageUrl cho mục đích của bạn (ví dụ: hiển thị hình ảnh)

                Log.e("Image URL", imageUrl);
                Glide.with(AddCarActivity.this).load(imageUrl).into(clickedImageButton);
                if (clickedImageButton == imgFirst) {
                    mCar.setFirstImage(imageUrl);
                }
                if (clickedImageButton == imgEnd) {
                    mCar.setEndImage(imageUrl);
                }
                if (clickedImageButton == imgL) {
                    mCar.setLeftImage(imageUrl);
                }
                if (clickedImageButton == imgR) {
                    mCar.setRightImage(imageUrl);
                }
            }


            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.e("Upload Image", "onError");
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.e("Upload Image", "onReschedule");
            }
        }).dispatch();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static void showInputDialog(String title, InputDialogListener listener, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(context);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    // Gọi callback khi người dùng nhập liệu và click OK
                    listener.onInputEntered(userInput);
                } else {
                    // Hiển thị thông báo yêu cầu nhập dữ liệu
                    Toast.makeText(context, "Vui lòng nhập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public interface InputDialogListener {
        void onInputEntered(String input);
    }

    private static boolean isMediaManagerInitialized = false;

    public void initConfig() {
        if (!isMediaManagerInitialized) {
            Map config = new HashMap();
            config.put("cloud_name", "dp0ctmdlu");
            config.put("api_key", "613553174187298");
            config.put("api_secret", "XWgIP8bV_n-P0M65kt2_FukPiwA");
            config.put("secure", true);
            MediaManager.init(this, config);
            isMediaManagerInitialized = true;
        }
    }

    private void openAutocompletePlace() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Retrieve the place result
                Place place = Autocomplete.getPlaceFromIntent(data);
                // Handle the selected place
              //  Toast.makeText(AddCarActivity.this, place.getAddress(), Toast.LENGTH_LONG).show();
                binding.txtLocation.setText(place.getAddress()); // Xử lý dữ liệu nhập vào ở đây
                mCar.setLocation(binding.txtLocation.getText().toString());
                String placeName = place.getName();
                String placeAddress = place.getAddress();
//                System.out.println(placeAddress);
//                Log.e("addcar","succes");
                // You can use this placeName and placeAddress as required
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error
//                Log.e("addcar","fail");
//                Toast.makeText(AddCarActivity.this,"Loi", Toast.LENGTH_LONG).show();
//                assert data != null;
//                Log.e("Autocomplete Error", "Error retrieving place: " + Autocomplete.getStatusFromIntent(data).getStatus());

                Status e = Autocomplete.getStatusFromIntent(data).getStatus();
                System.out.println(e);

            }
        }
    }

    private void callApiAddCar(Car car) {
        ApiService.apiService.insertCar(car).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    Car car = response.body();
                   // Toast.makeText(AddCarActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                    // callback.onCallbackCar(mCar);
                } else {
               //     Toast.makeText(AddCarActivity.this, "Fail!", Toast.LENGTH_LONG).show();
                    Log.e("API Response", "Request URL: " + call.request().url());
                    Log.e("API Response", "Response Code: " + response.code());
                    Log.e("API Response", "Response Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
           //     Toast.makeText(AddCarActivity.this, "Call api error", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void showEngineSelectionDialog(InputDialogListener listener) {
        final String[] engines = {"Xăng", "Dầu", "Hybrid", "Điện"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn loại động cơ").setItems(engines, (dialog, which) -> listener.onInputEntered(engines[which])).setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
        mCar.setEngine(binding.txtEngine.getText().toString());
    }

    private void showNumOfStateSelectionDialog(InputDialogListener listener) {
        final String[] NumOfState = {"2", "4", "7", "9", "11", "16", "29", "35", "45"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chỗ ngồi").setItems(NumOfState, (dialog, which) ->listener.onInputEntered(NumOfState[which])).setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();

    }
    private void showGearSelectionDialog(InputDialogListener listener) {
        final String[] engines = {"Số sàn", "Số tự động"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn hộp số").setItems(engines, (dialog, which) -> listener.onInputEntered(engines[which])).setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
        mCar.setEngine(binding.txtEngine.getText().toString());
    }

    private void showYearSelectionDialog(InputDialogListener listener) {
        final String[] Year = {
                "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015",
                "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Năm sản xuất").setItems(Year, (dialog, which) ->listener.onInputEntered(Year[which])).setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();

    }
}