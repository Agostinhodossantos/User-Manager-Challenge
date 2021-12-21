package user.app.com.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import user.app.com.ui.fragment.BottomSheetEdit;
import user.app.com.utils.Constants;
import user.app.com.utils.StringUtils;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private Uri mSelectedUri = null;
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
                BottomSheetEdit bottomSheetEditBio = new BottomSheetEdit(user.getName(), Constants.BIO);
                bottomSheetEditBio.show(getSupportFragmentManager() , "bottomSheet");
            }
        });

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
                setupImage();
            } else {
                Toast.makeText(this, "  ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupImage() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
            binding.imgProfile.setImageDrawable(new BitmapDrawable(bitmap));
            uploadImage();
        } catch (IOException e) {

        }
    }

    private void uploadImage() {

    }

}