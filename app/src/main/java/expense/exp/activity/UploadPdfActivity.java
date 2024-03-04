package expense.exp.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import expense.exp.R;
import expense.exp.components.Compressor;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityUploadPdfBinding;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.toast.CustomToast;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadPdfActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_GET_SIGNATURE = 14;
    String displayName;
    List<String> file;
    String f_id;
    String folder_id = "";
    String type = "";
    private SharedPrefManager sharedPrefManager;
    private String filePath,signature_path="";
    Uri imageUri;
    File image_file = null;

    private static final int INTENT_REQUEST_GET_SIGNATURE_IMAGE = 15;
ActivityUploadPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_upload_pdf);
        sharedPrefManager = new SharedPrefManager(this);
        binding = ActivityUploadPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Type listOfdoctorType = new TypeToken<List<String>>() {
        }.getType();

        if (getIntent().getStringExtra("from")!=null && !getIntent().getStringExtra("from").equalsIgnoreCase("Folder_Fragment")) {
            try {

                file = new Gson().fromJson(getIntent().getStringExtra("file"), listOfdoctorType);
            } catch (Exception e) {

                Log.e("#########exception",e.toString());
            }
        } else {
            filePath = getIntent().getStringExtra("file");
            file = new ArrayList<>();
            file.add(filePath);
        }



        displayName = getIntent().getStringExtra("displayName");
        f_id = getIntent().getStringExtra("f_id");
        type = getIntent().getStringExtra("type");
        folder_id = String.valueOf(f_id);


        File[] fileNew = new File[file.size()];
        for (int index = 0; index < fileNew.length; index++) {
            fileNew[index] = new File(file.get(index));
            try {
                fileNew[index].createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        binding.addSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadPdfActivity.this,SignatureActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_SIGNATURE);
            }
        });

        binding.uploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (edit_cost.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Please Add Cost ", Toast.LENGTH_LONG).show();
//
////                    new CustomToast().Show_Toast(this, view, "Please Add Cost.");
//                }
//                else
//                    if (edit_fileName.getText().toString().isEmpty()){
////                    new CustomToast().Show_Toast(this, view, "Please Add File name.");
//                    Toast.makeText(getApplicationContext(), "Please Add File name. ", Toast.LENGTH_LONG).show();
//
//                }
//                else if (image_file == null){
////                    new CustomToast().Show_Toast(this, v, "Please Add Signature Image.");
//                    Toast.makeText(getApplicationContext(), "Please Add Signature Image. ", Toast.LENGTH_LONG).show();
//                }
//                else{
                    upload_file(Integer.parseInt(sharedPrefManager.getuserinfo().getId()), f_id, 0, binding.editCost.getText().toString(),
                            binding.editFileName.getText().toString(), Pref.getStringValue(UploadPdfActivity.this,
                                    Utils.selected_year, Utils.this_year), type, fileNew,signature_path);

//                }

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

    public void upload_file(int o_id, String f_id, int same, String cost, String doc_name, String year, String type, File[] file,String signature) {
//        startAnim();

//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        List<MultipartBody.Part> multiparts = new ArrayList<>();

        try {
            if (type.equals("pdf")) {
                for (int i = 0; i < file.length; i++) {
                    RequestBody surveyBody = RequestBody.create(MediaType.parse("application/pdf"), new File(file[i].getPath()));
                    multiparts.add(MultipartBody.Part.createFormData("uploadFile[" + i + "]", file[i].getName(), surveyBody));
                }
            } else {
                File compressedImageBitmap = new Compressor(this).compressToFile(file[0]);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), compressedImageBitmap);
                multiparts.add(MultipartBody.Part.createFormData("uploadFile[0]", file[0].getName(), surveyBody));
            }
        } catch (IOException e) {
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file[0]);
            multiparts.add(MultipartBody.Part.createFormData("uploadFile[0]", file[0].getName(), surveyBody));
            e.printStackTrace();
        }

        MultipartBody.Part multipartBody = null;

        if (signature.equals("") ){

        }
        else {
            File image_file = new File(signature);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image_file);

            multipartBody = MultipartBody.Part.createFormData("signature", image_file.getName(), requestFile);

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

                            Toast.makeText(getApplication(), status.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UploadPdfActivity.this, ShowFileActivity.class);
                            String folder_id = String.valueOf(f_id);
                            intent.putExtra("f_id", folder_id);
                            intent.putExtra("isFrom","0");
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(UploadPdfActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();
//                            stopAnim();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError: ", e.toString());
                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
//                        stopAnim();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_SIGNATURE && resultCode == AppCompatActivity.RESULT_OK) {

            if (data != null) {
                Bundle extras = data.getExtras();
//                Uri myUri = Uri.parse(extras.getString("imageUri"));
//                signature_path = data.getStringExtra("image");
                String  image = data.getStringExtra("image");
                imageUri = Uri.parse(image);
                image_file = new File(imageUri.getPath());

                Log.e("***8SIGNATURE**8",image.toString());

                Glide.with(UploadPdfActivity.this).load(image.toString()).into(binding.signatureImage);
            }
        }

        else  if (requestCode == INTENT_REQUEST_GET_SIGNATURE_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {

            if (data != null) {
                Bundle extras = data.getExtras();

                imageUri = data.getData();

                signature_path = getRealPathFromURI(imageUri);
                image_file = new File(signature_path);

                binding.signatureImage.setImageURI(imageUri);

            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}