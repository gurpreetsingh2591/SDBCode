/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package expense.exp.imagepicker;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import expense.exp.ImageGridActivity;
import expense.exp.R;
import expense.exp.activity.ShowFileActivity;
import gun0912.tedbottompicker.GridSpacingItemDecoration;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
public class ImagePickerDemo extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener, TedBottomPicker.ImageTaken, UCropFragmentCallback {
    private FloatingActionButton cancelFab;
    private static final String TAG = "ImagePickerDemo";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };
    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            -R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;
    private File file;
    private CameraView mCameraView;
    public ImageView cropedImage;
    private Handler mBackgroundHandler;
    private RecyclerView galleryView;
    ArrayList<Uri> selectedUriList;
    TedBottomPicker bottomSheetDialogFragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottomSheetView;
    private String pickerType;
    String f_id;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
//                    startActivityForResult(new Intent(ImagePickerDemo.this, ScanA.class), REQUEST_CODE);
                    break;
                case R.id.cancel_picture:
                    mCameraView.setVisibility(View.VISIBLE);
                    cropedImage.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private Bitmap img;
    private final int REQUEST_PERMISSION_CAMERA = 101;
    private final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 102;
    private final int REQUEST_RECEIPT_CAPTURE = 105;
    private String image_path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_demo);
        mCameraView = (CameraView) findViewById(R.id.camera);
        cropedImage = findViewById(R.id.iv_crop);
        bottomSheetView = (FrameLayout) findViewById(R.id.container);
        // setRecyclerView();
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        if (getIntent() != null) {
            pickerType = getIntent().getStringExtra("picker");
            f_id = getIntent().getStringExtra("f_id");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.take_picture);
        cancelFab = (FloatingActionButton) findViewById(R.id.cancel_picture);

        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        if (cancelFab != null) {
            cancelFab.setOnClickListener(mOnClickListener);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            mCameraView.start();
            if (bottomSheetDialogFragment == null || !bottomSheetDialogFragment.isVisible()) {
//                if (pickerType.equalsIgnoreCase("single")){
//                    showBottomPicker();
//                }else
//                    if (pickerType.equalsIgnoreCase("multi")) {
//                    showMultiBottomPicker();
//                }
                showMultiBottomPicker();
            }

        } else {
            requestPermission();
        }

    }

    private void showMultiBottomPicker() {
        bottomSheetDialogFragment = new TedBottomPicker.Builder(ImagePickerDemo.this)
                .setOnMultiImageSelectedListener(
                        new TedBottomPicker.OnMultiImageSelectedListener() {
                            @Override
                            public void onImagesSelected(ArrayList<Uri> uriList) {
                                selectedUriList = uriList;
                                // TODO: 1/22/2019 add your class name here
                                String folder_id = String.valueOf(f_id);
                                startActivity(new Intent(getBaseContext(), ShowFileActivity.class)
                                        .putExtra("img_list", selectedUriList)
                                        .putExtra("isFrom","1")
                                        .putExtra("f_id", folder_id));

                                finish();
                            }
                        })
                .setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2)
                //.setPeekHeight(300)
                .showCameraTile(false)
                // .showGalleryTile(true)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .setSelectedUriList(selectedUriList)
                .create();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, bottomSheetDialogFragment);
        ft.commitAllowingStateLoss();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                bottomSheetDialogFragment.setStateOpen(newState != BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBottomPicker() {
        bottomSheetDialogFragment = new TedBottomPicker.Builder(ImagePickerDemo.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        imagePreview(uri);
                    }
                })
                .setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2)
                //.setPeekHeight(300)
                .showCameraTile(false)
                // .showGalleryTile(true)
                .setCompleteButtonText(getString(R.string.done))
                .setEmptySelectionText(getString(R.string.no_select))
                .setSelectedUriList(selectedUriList)
                .create();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, bottomSheetDialogFragment);
        ft.commitAllowingStateLoss();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                bottomSheetDialogFragment.setStateOpen(newState != BottomSheetBehavior.STATE_COLLAPSED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        //
    }

    @Override
    protected void onPause() {
        if (mCameraView != null)
            mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }


    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
//            mCameraView.setAspectRatio(ratio);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private final CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);

            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File direct = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
                    Bitmap bitmap = null;
                    if (!direct.exists()) {
                        File wallpaperDirectory = new File("/sdcard/" + getString(R.string.app_name) + "/");
                        wallpaperDirectory.mkdirs();
                    }

                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
                            Locale.getDefault()).format(
                            new Date());
                    String imageFileName = "JPEG_" + timeStamp + ".jpg";
                    file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            imageFileName);
                    if (file.exists()) {
                        file.delete();

                    }

                    OutputStream os = null;

                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            cropedImage.setVisibility(View.VISIBLE);
                            mCameraView.setVisibility(View.GONE);
                            cropedImage.setImageURI(Uri.fromFile(file));
                            startCrop(Uri.fromFile(file));
                            bottomSheetDialogFragment.iv_crop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startCrop(Uri.fromFile(file));
                                }
                            });

                            bottomSheetDialogFragment.iv_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cropedImage.setVisibility(View.GONE);
                                    mCameraView.setVisibility(View.VISIBLE);
                                    mCameraView.start();
                                    if (image_path != null) {
                                        File file = new File(image_path);
                                        bottomSheetDialogFragment.onActivityResultCamera(Uri.fromFile(file));
                                    } else {
                                        bottomSheetDialogFragment.onActivityResultCamera(Uri.fromFile(file));
                                    }
                                }
                            });
                        }
                    });
                    bottomSheetDialogFragment.onResultCamera(file);
                }
            });
        }

    };

    private void startCrop(@NonNull Uri uri) {
        Log.e("uriiiiiiii", uri.toString());
        String destinationFileName = uri.toString();
        destinationFileName += ".jpg";
        Log.e("destinationFileName", destinationFileName);
        UCrop uCrop = UCrop.of(uri, Uri.parse(destinationFileName));
        uCrop.useSourceImageAspectRatio();
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(ImagePickerDemo.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
                Log.e("dataaaaaaaaa",data.toString());
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    @Override
    public void setImage(File file) {
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        cropedImage.setImageBitmap(myBitmap);
    }


    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {


        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
//            image_path.add(resultUri.getPath());
            image_path = resultUri.getPath();
            cropedImage.setImageURI(Uri.fromFile(new File(image_path)));
//            adapter.updateList(image_path); // add this
//            adapter.notifyDataSetChanged();

            Log.e("resultUri", String.valueOf(resultUri));
        } else {
            Toast.makeText(ImagePickerDemo.this, "toast_cannot_retrieve_cropped_image", Toast.LENGTH_SHORT).show();
        }
    }

    private static void startWithUri(ImageGridActivity imageGridActivity, Uri resultUri) {
        Intent intent = new Intent(imageGridActivity, ImageGridActivity.class);
        intent.setData(resultUri);
        imageGridActivity.startActivity(intent);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(ImagePickerDemo.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ImagePickerDemo.this, "toast_unexpected_error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void loadingProgress(boolean showLoader) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }

    private void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        galleryView.setLayoutManager(gridLayoutManager);
        galleryView.addItemDecoration(
                new GridSpacingItemDecoration(gridLayoutManager.getSpanCount(), 10, false));
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(ImagePickerDemo.this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:

                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean StoragePermission =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && StoragePermission) {
                        // Permission Granted
                        mCameraView.start();
                        showMultiBottomPicker();
                        Toast.makeText(ImagePickerDemo.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ImagePickerDemo.this, "Permission Denied",
                                Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    public void imagePreview(final Uri uri) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_preview);

        ImageView previewImage = (ImageView) dialog.findViewById(R.id.img_preview_image);
        Button saveButton = (Button) dialog.findViewById(R.id.btn_image_preview_save);
        Button cancelButton = (Button) dialog.findViewById(R.id.btn_image_preview_canel);

        Glide.with(this).load(uri).into(previewImage);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        String folder_id = String.valueOf(f_id);
        startActivity(new Intent(getBaseContext(), ShowFileActivity.class)
                .putExtra("f_id", folder_id)
                .putExtra("isFrom","1"));

        finish();
    }
}
