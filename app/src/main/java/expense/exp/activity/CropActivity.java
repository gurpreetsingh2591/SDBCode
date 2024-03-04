package expense.exp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import expense.exp.R;
import expense.exp.databinding.ActivityChangePasswordBinding;
import expense.exp.databinding.ActivityCropBinding;

public class CropActivity extends AppCompatActivity {

    String image;
    ImageView cropImageView;
    private Uri mCropImageUri;
    ActivityCropBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_crop);
        binding = ActivityCropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        image = getIntent().getStringExtra("image");

        Glide.with(this).load(image).dontAnimate().placeholder(R.drawable.user_profile_icon).into(binding.cropImageView);

        startCropImageActivity(Uri.parse(image));
    }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            // handle result of pick image chooser
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = Uri.parse(image);

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri);
                }
            }

            // handle result of CropImageActivity
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    binding.cropImageView.setImageURI(result.getUri());
                    Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Start crop image activity for the given image.
         */
        private void startCropImageActivity(Uri imageUri) {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        }
    }

