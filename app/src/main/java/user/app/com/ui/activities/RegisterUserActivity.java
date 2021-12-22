package user.app.com.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import user.app.com.R;
import user.app.com.databinding.ActivityMainBinding;
import user.app.com.databinding.ActivityRegisterUserBinding;
import user.app.com.models.User;
import user.app.com.network.DataProvider;
import user.app.com.utils.StringUtils;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;
    private Uri mSelectedUri = null;
    private static final int PICK_FROM_GALLERY = 1;
    DataProvider provider = new DataProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

    }

    private void checkFields() {
        String name = binding.edName.getText().toString();
        String bio = binding.edBio.getText().toString();

        if (StringUtils.isEmpty(name)) {
            binding.edName.requestFocus();
            binding.textInputName.setErrorEnabled(true);
            binding.textInputName.setError("Name must not be empty");
        } else if (StringUtils.isEmpty(bio)) {
            binding.edBio.requestFocus();
            binding.textInputBio.setErrorEnabled(true);
            binding.textInputBio.setError("Bio must not be empty");
        } else {
            binding.textInputName.setErrorEnabled(false);
            binding.textInputBio.setErrorEnabled(false);
            binding.progressCircular.setVisibility(View.VISIBLE);
            binding.btnSave.setVisibility(View.GONE);

            if (mSelectedUri != null) {
                provider.fileUpload(mSelectedUri, new DataProvider.ResponseListener() {
                    @Override
                    public void onSuccess(Object response) {
                        createUser(name, bio, response.toString());
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(RegisterUserActivity.this,
                                message, Toast.LENGTH_SHORT).show();
                        createUser(name, bio,"" );
                    }
                });
            } else {
                createUser(name, bio,"" );
            }

        }
    }

    private void createUser(String name, String bio, String imgUrl) {
       User user = new User(UUID.randomUUID().toString(), name, imgUrl, bio);
       provider.createUser(user, new DataProvider.ResponseListener() {
           @Override
           public void onSuccess(Object response) {
               Toast.makeText(RegisterUserActivity.this, "User added successfully",
                       Toast.LENGTH_SHORT).show();
               finish();
           }

           @Override
           public void onFailure(String message) {
               Toast.makeText(RegisterUserActivity.this, "an error occurred:"+message,
                       Toast.LENGTH_SHORT).show();
               binding.progressCircular.setVisibility(View.GONE);
               binding.btnSave.setVisibility(View.VISIBLE);
           }
       });
    }

    private void checkPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(RegisterUserActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RegisterUserActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PICK_FROM_GALLERY);
            } else {
                selectPhoto();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                mSelectedUri = data.getData();
                setupBitmapImage();
            } else {
                Toast.makeText(this, "  ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhoto();
                } else {
                    Toast.makeText(RegisterUserActivity.this,
                            "didn`t allow the app to access gallery",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setupBitmapImage() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
            binding.imgProfile.setImageDrawable(new BitmapDrawable(bitmap));
        } catch (IOException e) {

        }
    }


}