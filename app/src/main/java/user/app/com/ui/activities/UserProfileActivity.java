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

import com.squareup.picasso.Picasso;

import java.io.IOException;

import user.app.com.R;
import user.app.com.databinding.ActivityUserProfileBinding;
import user.app.com.models.User;
import user.app.com.network.DataProvider;
import user.app.com.ui.fragment.BottomSheetEdit;
import user.app.com.utils.Constants;
import user.app.com.utils.StringUtils;

public class UserProfileActivity extends AppCompatActivity
        implements BottomSheetEdit.BottomSheetListener {


    private ActivityUserProfileBinding binding;
    private Uri mSelectedUri = null;
    private static final int PICK_FROM_GALLERY = 1;
    private  User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentValues();

        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetEdit bottomSheetEditName = new BottomSheetEdit(user.getName(), Constants.NAME);
                bottomSheetEditName.show(getSupportFragmentManager() , "bottomSheet");
            }
        });

        binding.tvBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetEdit bottomSheetEditBio = new BottomSheetEdit(user.getBiography(), Constants.BIO);
                bottomSheetEditBio.show(getSupportFragmentManager() , "bottomSheet");
            }
        });

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(UserProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserProfileActivity.this,
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

    private void getIntentValues() {
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        if (user != null) {
            updateUI(user);
        }
    }

    private void updateUI(User user) {
        binding.tvName.setText(user.getName());
        binding.tvBio.setText(user.getBiography());

        if (!StringUtils.isEmpty(user.getImgUrl())) {
            Picasso.get().load(user.getImgUrl()).into(binding.imgProfile);
        }

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
                    Toast.makeText(UserProfileActivity.this,
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
            uploadImage();
        } catch (IOException e) {

        }
    }

    private void uploadImage() {
        DataProvider provider = new DataProvider();
        provider.fileUpload(mSelectedUri, new DataProvider.ResponseListener() {
            @Override
            public void onSuccess(Object response) {
                updateImage(response.toString());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void updateImage(String url) {
        DataProvider provider = new DataProvider();
        provider.updateUserImg(user.getId(), url, new DataProvider.ResponseListener() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void onSaveButtonClicked(String text, int type) {
        DataProvider provider = new DataProvider();

        if (type == Constants.NAME) {
            provider.updateUserName(user.getId(), text, new DataProvider.ResponseListener() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(UserProfileActivity.this,
                            response.toString(),
                            Toast.LENGTH_SHORT).show();
                    binding.tvName.setText(text);
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(UserProfileActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (type == Constants.BIO) {
            provider.updateUserBio(user.getId(), text, new DataProvider.ResponseListener() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(UserProfileActivity.this,
                            response.toString(),
                            Toast.LENGTH_SHORT).show();
                    binding.tvBio.setText(text);
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(UserProfileActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}