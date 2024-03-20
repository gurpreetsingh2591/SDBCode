package expense.exp.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.adapter.RecyclerViewAdapter;
import expense.exp.components.Compressor;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.UploadActicityBinding;
import expense.exp.helper.FileUtils;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.toast.CustomToast;

import com.bumptech.glide.Glide;
import com.kyanogen.signatureview.SignatureView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 30-07-2018.
 */

public class UploadFileActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private SharedPrefManager sharedPrefManager;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_SIGNATURE_IMAGE = 15;
    private static final int INTENT_REQUEST_GET_SIGNATURE = 14;

    RecyclerViewAdapter adapter = null;

    //@BindView(R.id.select_file_recyclerview)
    RecyclerView recyclerView;

    //@BindView(R.id.select_file)
    Button select_file;

    //@BindView(R.id.loading_view)
    RelativeLayout loading_view;

   // @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    //@BindView(R.id.edit_cost)
    EditText edit_cost;

    //@BindView(R.id.edit_fileName)
    EditText edit_filename;

    //@BindView(R.id.check_singleDoc)
    CheckBox check_singleDoc;

    //@BindView(R.id.btn_container)
    LinearLayout btn_container;
    String f_id;

    String folder_id = "";
    String type = "";
   // Unbinder unbinder;
    private Uri mCropImageUri;
    public static ArrayList<String> image_path = new ArrayList<>();
    int counter = 0;
    private Button addSignature,uploadSignature,btnAdd,btnClear;
    private ImageView signatureImage;
    private SignatureView signatureView;
    private String signature_path;
    Uri imageUri;
    File image_file = null;
    Bitmap bitmap;
    UploadActicityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.upload_acticity);
       // unbinder = ButterKnife.bind(this);
        binding = UploadActicityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager = new SharedPrefManager(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        binding.selectFileRecyclerview.setLayoutManager(layoutManager);


        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        binding.signatureView.setPenColor(colorPrimary);


        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signatureView.clearCanvas();//Clear SignatureView
            }
        });
        binding.backIcon.setOnClickListener(v -> back(v));
        binding.uploadDoc.setOnClickListener(v -> upload_doc(v));
        binding.selectFile.setOnClickListener(v -> setSelect_file(v));


        binding.btnAdd.setOnClickListener(v -> {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(directory, System.currentTimeMillis() + ".png");

            FileOutputStream out = null;
            Bitmap bitmap = binding.signatureView.getSignatureBitmap();
            try {
                out = new FileOutputStream(file);
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } else {
                    throw new FileNotFoundException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();

                        if (bitmap != null) {
                            Uri yourUri = Uri.fromFile(file);

//                                signature_path = getRealPathFromURI(yourUri);
                            image_file = new File(yourUri.toString());
                            binding.signatureImage.setImageURI(yourUri);

                            Toast.makeText(getApplicationContext(), "Image saved successfully at " + file.getPath(), Toast.LENGTH_LONG).show();

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                new  MyMediaScanner(UploadFileActivity.this, file);
                            } else {
                                ArrayList<String> toBeScanned = new ArrayList<String>();
                                toBeScanned.add(file.getAbsolutePath());
                                String[] toBeScannedStr = new String[toBeScanned.size()];
                                toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
                                MediaScannerConnection.scanFile(UploadFileActivity.this, toBeScannedStr, null,
                                        null);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        f_id = getIntent().getStringExtra("f_id");
        type = getIntent().getStringExtra("type");

        folder_id = String.valueOf(f_id);

        Log.e("folder_id", String.valueOf(f_id));
        if (getIntent().hasExtra("via_camera")) {
            if (image_path != null)
                image_path = getIntent().getStringArrayListExtra("via_camera");
            else
                Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
            if (image_path.size() > 1) {
                binding.checkSingleDoc.setChecked(true);
                binding.checkSingleDoc.setVisibility(View.VISIBLE);
                binding.btnContainer.setVisibility(View.VISIBLE);
                adapter = new RecyclerViewAdapter(getApplicationContext(), image_path);
                binding.selectFileRecyclerview.setAdapter(adapter);
            } else {

                binding.checkSingleDoc.setVisibility(View.GONE);
                binding.btnContainer.setVisibility(View.VISIBLE);
                binding.editCost.setVisibility(View.VISIBLE);
                binding.editFileName.setVisibility(View.VISIBLE);
            }
            binding.btnContainer.setVisibility(View.VISIBLE);
            binding.editCost.setVisibility(View.VISIBLE);
            binding.editFileName.setVisibility(View.VISIBLE);
            adapter = new RecyclerViewAdapter(getApplicationContext(), image_path);
            binding.selectFileRecyclerview.setAdapter(adapter);

        } else {
            binding.btnContainer.setVisibility(View.VISIBLE);
            Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
            pickPhoto.setType("image/*");
            pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(pickPhoto, INTENT_REQUEST_GET_IMAGES);

            adapter = new RecyclerViewAdapter(getApplicationContext(), image_path);
            binding.selectFileRecyclerview.setAdapter(adapter);

        }

        binding.checkSingleDoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    binding.editCost.setVisibility(View.GONE);
                    binding.editFileName.setVisibility(View.GONE);
                } else {
                    binding.editCost.setVisibility(View.VISIBLE);
                    binding.editFileName.setVisibility(View.VISIBLE);
                }
            }
        });


        binding.addSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(UploadFileActivity.this,SignatureActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_SIGNATURE);
            }
        });

        binding.uploadSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, INTENT_REQUEST_GET_SIGNATURE_IMAGE);

            }
        });
    }



   // @OnClick(R.id.select_file)
    public void setSelect_file(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
        pickPhoto.setType("image/*");
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pickPhoto, INTENT_REQUEST_GET_IMAGES);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();

    }

    private class MyMediaScanner implements
            MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mSC;
        private File file;

        MyMediaScanner(Context context, File file) {
            this.file = file;
            mSC = new MediaScannerConnection(context, this);
            mSC.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mSC.scanFile(file.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mSC.disconnect();
        }
    }


    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent data) {
        super.onActivityResult(requestCode, resuleCode, data);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == AppCompatActivity.RESULT_OK) {
//            image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//            Log.e("data_img", String.valueOf(image_uris));
            if (data != null) {
                image_path = new ArrayList<>(getSelectedImage(data));

                if (image_path.size() > 1) {
                    binding.checkSingleDoc.setChecked(true);
                    binding.checkSingleDoc.setVisibility(View.VISIBLE);
                    binding.btnContainer.setVisibility(View.VISIBLE);
                    adapter = new RecyclerViewAdapter(getApplicationContext(), image_path);
                    binding.selectFileRecyclerview.setAdapter(adapter);
                } else {
                    binding.checkSingleDoc.setChecked(false);
                    binding.checkSingleDoc.setVisibility(View.GONE);
                }
            }
            binding.btnContainer.setVisibility(View.VISIBLE);
            binding.editCost.setVisibility(View.VISIBLE);
            binding.editFileName.setVisibility(View.VISIBLE);
            adapter = new RecyclerViewAdapter(getApplicationContext(), image_path);
            binding.selectFileRecyclerview.setAdapter(adapter);
        }

        else  if (requestCode == INTENT_REQUEST_GET_SIGNATURE && resuleCode == AppCompatActivity.RESULT_OK) {

            if (data != null) {
                String  image = data.getStringExtra("image");

                Log.e("****************8SIGNATURE************8",image.toString());

                imageUri = Uri.parse(image);
                image_file = new File(imageUri.getPath());
                Glide.with(UploadFileActivity.this).load(image.toString()).into(signatureImage);
            }
        }
        else  if (requestCode == INTENT_REQUEST_GET_SIGNATURE_IMAGE && resuleCode == AppCompatActivity.RESULT_OK) {

            if (data != null) {
                Bundle extras = data.getExtras();

                imageUri = data.getData();

                signature_path = getRealPathFromURI(imageUri);
                image_file = new File(signature_path);

                binding.signatureImage.setImageURI(imageUri);

            }
        }
        else {
            call_Back();
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
             bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/" + getString(R.string.app_name) + "/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/" + getString(R.string.app_name) + "/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            image_file = file;


        } catch (Exception e) {
            e.printStackTrace();
            image_file = null;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<String> getSelectedImage(Intent data) {

        ArrayList<String> result = new ArrayList<>();

        ClipData clipData;
        clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = FileUtils.getPath(this, videoURI);
            result.add(filePath);
        }

        return result;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };

        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    void startAnim() {
        loading_view.setVisibility(View.VISIBLE);
        binding.avi.setVisibility(View.VISIBLE);
        binding.avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {

        if (binding.avi.getIndicator() != null) {

            if (binding.avi.isShown()) {

                binding.avi.hide();
                binding.avi.setVisibility(View.GONE);
                binding.loadingView.setVisibility(View.GONE);
            }

        }


        // or avi.smoothToHide();
    }

   // @OnClick(R.id.upload_doc)
    public void upload_doc(View view) {

        String getcost = binding.editCost.getText().toString().trim();
        String getDocName = binding.editFileName.getText().toString().trim();

//        signature_path = getRealPathFromURI(imageUri);
//        image_file = new File(signature_path);

//        if (getcost.isEmpty()){
//            new CustomToast().Show_Toast(this, view, "Please Add Cost.");
//        }
//        else
//            if (getDocName.isEmpty()){
//            new CustomToast().Show_Toast(this, view, "Please Add File name.");
//
//        }
//        else if (image_file == null){
//            new CustomToast().Show_Toast(this, view, "Please Add Signature Image.");
//
//        }
//        else {
            if (image_path != null) {
                if (image_path.size() == 0) {
                    new CustomToast().Show_Toast(this, view, "Please select File.");
                } else if (image_path.size() < 2) {
//            } else if (image_path.size() < 2 || check_singleDoc.isChecked()) {
//                if (getcost.equals("") || getcost.length() == 0) {
//                    new CustomToast().Show_Toast(this, view,
//                            "Please Enter Cost.");
//                } else if (getDocName.equals("")) {
//                    new CustomToast().Show_Toast(this, view,
//                            "Please Enter Filename.");
//                } else {
//
                    Log.e("upload_doc", String.valueOf(UploadFileActivity.image_path.get(0)));
                    for (int i = 0; i < image_path.size(); i++) {
                        File file = new File(String.valueOf(UploadFileActivity.image_path.get(i)));
                        upload_file(Integer.parseInt(sharedPrefManager.getuserinfo().getId()), f_id, 0, getcost, getDocName, Pref.getStringValue(this, Utils.selected_year, Utils.this_year), "image", file, image_file);
                    }
                } else {
                    int same = binding.checkSingleDoc.isChecked() ? 1 : 0;
                    Log.e("upload_doc: ", "Checked: " + same);
//                for (int i = 0; i < image_path.size(); i++) {
                    File file = new File(String.valueOf(UploadFileActivity.image_path.get(0)));
                    upload_file(Integer.parseInt(sharedPrefManager.getuserinfo().getId()), f_id, same, getcost,
                            getDocName, Pref.getStringValue(this, Utils.selected_year, Utils.this_year), "image", file, image_file);
//                }

                }
            }
//        }
    }


    @SuppressLint("CheckResult")
    public void upload_file(int o_id, String f_id, int same, String cost, String doc_name, String year, String type, File file,File signature) {
        Log.e("upload_file: ", file.getAbsolutePath());
        startAnim();

//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        List<MultipartBody.Part> multiparts = new ArrayList<>();
        for (int i = 0; i < image_path.size(); i++) {
            File f = new File(UploadFileActivity.image_path.get(i));
//            if (doc_name.equals(""))
//                doc_name = f.getName();
            Log.d("debugging", "name = " + f.exists());
            Log.d("debugging", "name = " + type);
            try {
                File compressedImageBitmap = new Compressor(this).compressToFile(f);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), compressedImageBitmap);
                multiparts.add(MultipartBody.Part.createFormData("uploadFile[" + i + "]", f.getName(), surveyBody));
            } catch (IOException e) {
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), f);
                multiparts.add(MultipartBody.Part.createFormData("uploadFile[" + i + "]", f.getName(), surveyBody));
                e.printStackTrace();
            }
        }

        MultipartBody.Part multipartBody = null;


        if (signature == null) {


//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
//
//             multipartBody = MultipartBody.Part.createFormData("uploadFile","", requestFile);

        } else {

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), signature);
            multipartBody = MultipartBody.Part.createFormData("signature", signature.getName(), requestFile);

        }

        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);

        apiService.uploadFile(o_id, f_id, same, cost, doc_name, year, type, multiparts,multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {
                        if (status.getStatus().matches("1")) {
//                            Log.e("size", String.valueOf(image_path.size()));
//                            counter++;
//                            if (counter == image_path.size()) {
                            stopAnim();
                            binding.btnContainer.setVisibility(View.GONE);
                            Toast.makeText(getApplication(), status.getMessage(), Toast.LENGTH_LONG).show();
                            call_Back();
//                            }

                        } else {
                            Toast.makeText(UploadFileActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();
                            stopAnim();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError: ", e.toString());
                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                        stopAnim();

                    }
                });
    }
//        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("uploadFile", doc_name, requestFile);


    private void call_Back() {
        if (binding.editCost.getText() != null) {
            binding.editCost.setText("");
        }
        if (binding.editFileName.getText() != null) {
            binding.editFileName.setText("");
        }
        binding.checkSingleDoc.setChecked(true);
        if (adapter != null) {
            adapter.clear();
        }
        if (image_path != null) {
            image_path.clear();
        }
        counter = 0;
        Intent intent = new Intent(this, ShowFileActivity.class);
        String folder_id = String.valueOf(f_id);
        intent.putExtra("f_id", folder_id);
        intent.putExtra("isFrom","0");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        call_Back();
    }

    //@OnClick(R.id.back_icon)
    public void back(View view) {
        onBackPressed();
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
}
