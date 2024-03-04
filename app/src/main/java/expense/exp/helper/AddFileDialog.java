package expense.exp.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.activity.UploadFileActivity;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by admin on 30-06-2018.
 */

public class AddFileDialog extends Dialog {


    String _path;
    File file = null;
    String file_path = "";
    AppCompatActivity activity;
    AVLoadingIndicatorView avi;
    RelativeLayout loading_view;
    MyDialogListener myDialogListener;
    SharedPrefManager sharedPrefManager;
    EditText edit_file;
    EditText edit_cost;
    Button button,add_signature_btn;
    String f_id;

    ImageView dialogButton;
    Bitmap bitmap;

    ImageView img_file;


    public AddFileDialog(AppCompatActivity activity, MyDialogListener myDialogListener, String f_id, Bitmap bitmap, File file) {
        super(activity);
        this.activity = activity;
        this.myDialogListener = myDialogListener;
        this.f_id = f_id;
        this.bitmap = bitmap;
        this.file = file;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_file_layout);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setCancelable(false);

        sharedPrefManager = new SharedPrefManager(activity);

        edit_file = findViewById(R.id.edit_file);
        avi = findViewById(R.id.avi);
        loading_view = findViewById(R.id.loading_view);
        edit_cost = findViewById(R.id.edit_cost);
        button = findViewById(R.id.add_file_btn);

        img_file = findViewById(R.id.img_file);

        img_file.setImageBitmap(bitmap);

//        edit_file.setText(file.getName());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getcost = edit_cost.getText().toString();
                String getfile = edit_file.getText().toString();

                if (getfile.equals("") || getfile.length() == 0)
                    getfile = file.getName();
//                {
//
//                    new CustomToast().Show_Toast(activity, view,
//                            "Please select File.");

//                } else if (getcost.equals("") || getcost.length() == 0) {
//
//                    new CustomToast().Show_Toast(activity, view,
//                            "Please Enter Cost.");
//                } else {
//                    button.setEnabled(false);
                upload_file(Integer.parseInt(sharedPrefManager.getuserinfo().getId()), f_id, 0, getcost, getfile,"", file);
// }
            }
        });

        dialogButton = findViewById(R.id.cancel_icon);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogListener.OnCloseDialog();
                dismiss();
            }
        });


    }


    @SuppressLint("CheckResult")
    public void upload_file(int o_id, String f_id, int same, String cost, String doc_name,String type, File file) {

        startAnim();
        if (doc_name.equals(""))
            doc_name = file.getName();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("uploadFile", doc_name, requestFile);
        ApiInterface apiService = ApiClient.getClient(activity)
                .create(ApiInterface.class);
        List<MultipartBody.Part> multiparts = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            File f = new File(UploadFileActivity.image_path.get(i));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), f);
            multiparts.add(MultipartBody.Part.createFormData("uploadFile[]", doc_name, surveyBody));
        }

        apiService.uploadFile(o_id, f_id, same, cost, doc_name, Utils.this_year,type, multiparts,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1")) {
                            Toast.makeText(activity, status.getMessage(), Toast.LENGTH_LONG).show();
                            stopAnim();
                            myDialogListener.OnCloseDialog();
                            dismiss();

                        } else {

                            Toast.makeText(activity, status.getMessage(), Toast.LENGTH_LONG).show();
                            stopAnim();

                            myDialogListener.OnCloseDialog();
                            dismiss();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        stopAnim();
                        myDialogListener.OnCloseDialog();
                        dismiss();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    void startAnim() {
        loading_view.setVisibility(View.VISIBLE);

        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        avi.setVisibility(View.GONE);
        loading_view.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }

    //==============================================================================


}